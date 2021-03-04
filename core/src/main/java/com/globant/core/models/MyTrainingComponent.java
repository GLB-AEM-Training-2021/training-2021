package com.globant.core.models;

import com.adobe.cq.sightly.WCMUsePojo;

public class MyTrainingComponent extends WCMUsePojo {
    
    private String userName;

    @Override
    public void activate() throws Exception {
        userName = get("userName", String.class);
    }

    public String getHelloMessage() {
        return "Hi " + userName + "!!, How are you?";
    }
}