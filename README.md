# AEM Training Application

## Usefull commands

### Start Server for debugging

    java -agentlib:jdwp=transport=dt_socket,address=8484,server=y,suspend=n -jar aem-author-p4502.jar -gui
    
### Deploy application

```
    mvn clean install -PautoInstallPackage -PautoInstallBundle  -Padobe-public -DskipTest
    
    -PautoInstallPackage : installs ui.apps with core and configs  included
    -PautoInstallBundle: installs core bundle ONLY
    -DskipTests: optionally skipt unit testing
    -Padobe-public: enables adobe central repo for dependencies
```
## Log files

```
Error log: <aem_install_folder>/crx-quickstart/logs/error.log
Access log: <aem_install_folder>/crx-quickstart/logs/access.log
```

## Usefull links

### CRX DE: explore content, browse nodes, query content...

[http://localhost:4502/crx/de](http://localhost:4502/crx/de)

### Check that the bundle was properly installed:

[http://localhost:4502/system/console/bundles](http://localhost:4502/system/console/bundles)

### Check that the sling models and exporters was installed:

[http://localhost:4502/system/console/adapters](http://localhost:4502/system/console/adapters)

### Other links...
    http://localhost:4502/system/console/servletresolver?url=%2Fcontent%2Ftraining-2021%2Fus%2Fen%2Fcomponents-and-templates-class%2Fjcr%3Acontent%2Froot%2Fcontainer%2Fcontainer%2Fuserinfocomponent.userinfo.servlet.json&method=GET
    https://sling.apache.org/documentation/bundles/models.html
    https://sling.apache.org/

## Test Sling Servlets and Exporters

    http://localhost:4502/content/training-2021/us/en/components-and-templates-class/jcr:content/root/container/container/userinfocomponent.userinfo.userexporter.json
    
## Sling URL Structure

```
    http://localhost:4502/<path_to_content>.<selector_1>.<selector_2>...<selector_N>.<extension>
```

## Sling requests processing

    URL: http://localhost:4502/content/training-2021/us/en/Home/jcr:content/par/userinfocomponent.userinfoservlet.servlet.json
    
    host_port: http://localhost:4502
    
    Path of the content to render: 
        /content/training-2021/us/en/Home/jcr:content/par/userinfocomponent
    Selectors:
        userinfo.servlet
    Extension:
        json

### SlingDefaultGetServlet
    -> URL: http://localhost:4502/content/training-2021/us/en/Home/jcr:content/par/userinfocomponent.userinfoservlet.servlet.json
    -> Process
        -> Filter Candidates [......]
            -> Best Match!
                --> UserServlet!!

![GitHub Logo](diagrams/sling_request_resolution.png)

