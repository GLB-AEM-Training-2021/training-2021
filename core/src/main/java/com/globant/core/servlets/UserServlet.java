package com.globant.core.servlets;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.component.annotations.Component;

import com.globant.core.services.HelloWorldService;
import com.google.gson.JsonObject;

@Component(
service = { Servlet.class },
property = { 
    "sling.servlet.resourceTypes=/apps/training-2021/components/userinfocomponent",
    "sling.servlet.methods=GET",
    "sling.servlet.extensions=json",
    "sling.servlet.selectors=userinfo.servlet",
  }
)
public class UserServlet extends SlingSafeMethodsServlet {
    
    private static final String AVATAR = "avatar";
    private static final String CELLPHONE = "cellphone";
    private static final String EMAIL = "email";
    private static final String SURNAME = "surname";
    private static final String NAME = "name";
    @OSGiService
    private HelloWorldService trainingAppHelloWorldService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        
        String name = "";
        String surname = "";
        String email = "";
        String cellphone = "";
        String avatar = "";
        if (request.getRequestPathInfo().getSelectorString().contains("jcr")) {
            try {
                Node componentNode = request.getResourceResolver().adaptTo(Session.class).getNode(request.getResource().getPath());
                
                name = componentNode.hasProperty(NAME) ? componentNode.getProperty(NAME).getString() : "";
                surname = componentNode.hasProperty(SURNAME) ? componentNode.getProperty(SURNAME).getString() : "";
                email = componentNode.hasProperty(EMAIL) ? componentNode.getProperty(EMAIL).getString() : "";
                cellphone = componentNode.hasProperty(CELLPHONE) ? componentNode.getProperty(CELLPHONE).getString() : "";
                avatar = componentNode.hasProperty(AVATAR) ? componentNode.getProperty(AVATAR).getString() : "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ValueMap properties = request.getResource().getValueMap();
            
            name = (String)properties.getOrDefault(NAME, "");
            surname = (String)properties.getOrDefault(SURNAME, "");
            email = (String)properties.getOrDefault(EMAIL, "");
            cellphone = (String)properties.getOrDefault(CELLPHONE, "");
            avatar = (String)properties.getOrDefault(AVATAR, "");
        }
        
        JsonObject obj = new JsonObject();
        obj.addProperty(NAME, name);
        obj.addProperty(SURNAME, surname);
        obj.addProperty(EMAIL, email);
        obj.addProperty(CELLPHONE, cellphone);
        obj.addProperty(AVATAR, avatar);
        
        response.setContentType("application/json");
        response.getWriter().write(obj.toString());
    }
}