# training-2021


# Usefull links

    http://localhost:4502/system/console/bundles
    http://localhost:4502/system/console/adapters
    http://localhost:4502/system/console/servletresolver?url=%2Fcontent%2Ftraining-2021%2Fus%2Fen%2Fcomponents-and-templates-class%2Fjcr%3Acontent%2Froot%2Fcontainer%2Fcontainer%2Fuserinfocomponent.userinfo.servlet.json&method=GET
    https://sling.apache.org/documentation/bundles/models.html
    https://sling.apache.org/
    http://localhost:4502/content/training-2021/us/en/components-and-templates-class/jcr:content/root/container/container/userinfocomponent.userinfo.userexporter.json

    
# Sling requests processing

URL: http://localhost:4502/content/training-2021/us/en/Home/jcr:content/par/userinfocomponent.userinfoservlet.servlet.json

host_port: http://localhost:4502

Path of the content to render: 
    /content/training-2021/us/en/Home/jcr:content/par/userinfocomponent
Selectors:
    userinfo.servlet
Extension:
    json

-> SlingDefaultGetServlet
    -> URL: http://localhost:4502/content/training-2021/us/en/Home/jcr:content/par/userinfocomponent.userinfoservlet.servlet.json
    -> Process
        -> Filter Candidates [......]
            -> Best Match!
                --> UserServlet!!


