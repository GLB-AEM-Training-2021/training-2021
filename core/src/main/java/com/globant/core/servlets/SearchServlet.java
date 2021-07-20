package com.globant.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.qom.Constraint;
import javax.jcr.query.qom.QueryObjectModel;
import javax.jcr.query.qom.QueryObjectModelFactory;
import javax.jcr.query.qom.Selector;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

@Component(
    name = "[AEM Training] Querying Servlet",
    service = { Servlet.class },
    property = { 
        "sling.servlet.paths=/bin/aem-training/search",
        "sling.servlet.methods=GET",
        "sling.servlet.extensions=json",
        "sling.servlet.selectors=userinfo.servlet",
    }
)
public class SearchServlet extends SlingSafeMethodsServlet {
    
    public static final String SEARCH_ROOT_PATH = "/content/training-2021";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServlet.class);

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Content-Type", "application/json");
        JSONArray resultArray = new JSONArray();

        try (PrintWriter writer = response.getWriter()){
            String queryTerm = request.getParameter("q");
            String type = StringUtils.defaultIfBlank(request.getParameter("type"), "qom");
            if (queryTerm != null) {
                Node searchRoot = request.getResourceResolver().adaptTo(Session.class).getNode(SEARCH_ROOT_PATH);
                NodeIterator searchResults = null;
                
                
                switch (type) {
                case "qom":
                    searchResults = performQomSearch(searchRoot, queryTerm);
                    break;
                case "querybuilder":
                    
                    Map<String, String> properties =  new HashMap<String, String>();
                    Map<String, String> propertiesOperations =  new HashMap<String, String>();
                    searchResults = (NodeIterator)performQueryBuilderSearch(
                        request.getResourceResolver(), searchRoot.getPath(), properties, propertiesOperations, "10", false, false);
                    break;
                case "sql":
                    searchResults = performSqlSearch(searchRoot, queryTerm);
                    break;
                default:
                    break;
                }
                
                while (searchResults.hasNext()) {
                    resultArray.put(searchResults.nextNode().getPath());
                }
            }
            writer.print(resultArray.toString());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    private NodeIterator performSqlSearch(Node queryRoot, String queryTerm) throws RepositoryException {
        QueryManager queryManager = queryRoot.getSession().getWorkspace().getQueryManager();
        Query query = queryManager.createQuery(
            "SELECT * FROM [nt:unstructured] AS node WHERE ISDESCENDANTNODE('" + 
                    queryRoot.getPath() + "') AND CONTAINS(node.*,'" + queryTerm + "')", Query.JCR_SQL2);
        return query.execute().getNodes();
    }

    private NodeIterator performQomSearch(Node queryRoot, String queryTerm) throws RepositoryException {
        //JQOM Infrastructure.
        QueryObjectModelFactory queryObjectModelFactory = queryRoot.getSession().getWorkspace().getQueryManager().getQOMFactory();
        ValueFactory valueFactory = queryRoot.getSession().getValueFactory();

        final String SELECTOR_NAME = "all results";
        final String SELECTOR_NT_UNSTRUCTURED = "nt:unstructured";

        //select all unstructured nodes
        Selector selector = queryObjectModelFactory.selector(SELECTOR_NT_UNSTRUCTURED, SELECTOR_NAME);

        //full text constraint
        Constraint constraint = 
                queryObjectModelFactory.fullTextSearch(SELECTOR_NAME, null, queryObjectModelFactory.literal(valueFactory.createValue(queryTerm)));

        //path constraint
        constraint = queryObjectModelFactory.and(constraint, queryObjectModelFactory.descendantNode(SELECTOR_NAME, queryRoot.getPath()));

        //execute the query without explicit order and columns
        QueryObjectModel query = queryObjectModelFactory.createQuery(selector, constraint, null, null);

        return query.execute().getNodes();
    }
    
    /**
     * Method for generating a basic search query which can support multiple properties and needs
     * a path to start from.
     *
     * @param resourceResolver  An active resource resolver.
     * @param path              String containing the path where the query will begin searching from
     *                          (e.g. /content/hboweb/en/movies)
     * @param properties        Map<String, String> which includes a key/value pair of any number of properties.
     *                          that will be filtered.
     * @param limit             String with the maximum number of items that will be returned on the query,
     *                          "-1" should be used to return them all.
     * @param exact             Boolean which determines if the query will search using in an exact manner.
     *                          (as in, will only search in the current path and no children)
     * @param flat              Boolean which determines if the query will search using in a flat manner.
     *                          (as in, will only search in the next child level and not further down the tree)
     * @return SearchResult with the results of the generated query.
     **/
    public static Iterator<Node> performQueryBuilderSearch(ResourceResolver resourceResolver, String path, 
            Map<String, String> properties, Map<String, String> propertiesOperations, String limit, Boolean exact, Boolean flat) {
        Map<String, String> map = new HashMap<>();
        map.put("path", path);
        if(exact) {
            map.put("path.exact", "true");
        }
        if(flat) {
            map.put("path.flat", "true");
        }
        map.put("p.limit", limit);

        int propNum = 0;
        for(Map.Entry<String, String> entry : properties.entrySet()) {
            propNum = propNum + 1;
            map.put(propNum + "_property", entry.getKey());
            map.put(propNum + "_property.value", entry.getValue());
            if (propertiesOperations != null && propertiesOperations.containsKey(entry.getKey())) {
                map.put(propNum + "_property.operation", propertiesOperations.get(entry.getKey()));
            }
        }

        com.day.cq.search.Query query = resourceResolver.adaptTo(QueryBuilder.class)
            .createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
        LOGGER.debug("Executing query: {}", query);
        LOGGER.debug("Query properties: {}", map.toString());
        SearchResult results = query.getResult();
        return results.getNodes();
    }

}
