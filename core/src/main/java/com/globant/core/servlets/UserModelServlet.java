package com.globant.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.component.annotations.Component;

import com.globant.core.models.UserModel;
import com.globant.core.services.HelloWorldService;
import com.google.gson.JsonObject;

@Component(
service = { Servlet.class },
property = { 
    "sling.servlet.resourceTypes=/apps/training-2021/components/userinfocomponent",
    "sling.servlet.methods=GET",
    "sling.servlet.extensions=json",
    "sling.servlet.selectors=userinfo.servlet.model",
  }
)
public class UserModelServlet extends SlingSafeMethodsServlet {
    
    private static final String AVATAR = "avatar";
    private static final String CELLPHONE = "cellphone";
    private static final String EMAIL = "email";
    private static final String SURNAME = "surname";
    private static final String NAME = "name";
    @OSGiService
    private HelloWorldService trainingAppHelloWorldService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        
        UserModel userModel = request.getResource().adaptTo(UserModel.class);
        
        JsonObject obj = new JsonObject();
        obj.addProperty(NAME, userModel.getName());
        obj.addProperty(SURNAME, userModel.getSurname());
        obj.addProperty(EMAIL, userModel.getEmail());
        obj.addProperty(CELLPHONE, userModel.getCellphone());
        obj.addProperty(AVATAR, userModel.getAvatar());
        obj.addProperty("renderer", "servlet-v2-model");
        
        response.setContentType("application/json");
        response.getWriter().write(obj.toString());
    }
}