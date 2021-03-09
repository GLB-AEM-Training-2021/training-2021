package com.globant.core.models;

import com.adobe.cq.sightly.WCMUsePojo;
import com.globant.core.services.HelloWorldService;

public class MyTrainingComponent extends WCMUsePojo {
    
    private String userName;
    
    private HelloWorldService helloWorldService;

    @Override
    public void activate() throws Exception {
        
        userName = (String)getProperties().getOrDefault("userName", "John Doe");
    }

    public String getHelloMessage() {
        helloWorldService = getSlingScriptHelper().getService(HelloWorldService.class);
        return helloWorldService.getCustomHelloWorld(userName);
    }
}