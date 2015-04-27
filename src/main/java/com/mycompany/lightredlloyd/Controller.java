/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lightredlloyd;

import freemarker.template.Configuration;
import java.io.File;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.*;
import spark.template.freemarker.FreeMarkerEngine;

/**
 *
 * @author johncox
 */
public class Controller {
    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        File dir;
        MsApi api = new MsApi("");
        
        dir = new File("src/main/resources/spark/template/freemarker");
        try{
            cfg.setDirectoryForTemplateLoading(dir);
        } catch (Exception e){
            System.out.println("directory not found");
        }
        
        get("/", (request, response) -> {
            HashMap<String, Object> attributes = new HashMap<>();

            // The freemarker files are located in directory:
            // src/main/resources/spark/template/freemarker
            return new ModelAndView(attributes, "home.ftl");
        }, new FreeMarkerEngine());
        
        
        
        get("/login", (request, response) -> {
            HashMap<String, Object> attributes = new HashMap<>();
            return new ModelAndView(attributes, "login.ftl");
        }, new FreeMarkerEngine());
        
        post("/auth", (Request request, Response response) ->{
            
            // Create the JSON post body for authentication.
            HashMap<String, Object> attributes;
            attributes = new HashMap<>();
            attributes.put("email", request.queryMap("email").value());
            attributes.put("password", request.queryMap("pw").value());
             
            // Make the api call and set the api user.
            // NOTE: This approach will not work for a multi-user system.
            //       Find somewhere else to persist the user like the session.
            api.user = api.makeCall(api.mapToJson(attributes), "POST", "https://services.machineshop.io/api/v1/platform/user/authenticate");
            
            // Create the obejct needed for the view 
            HashMap<String, Object> viewModel = new HashMap<>();
            viewModel.put("user", api.mapToJson(api.user));
            
            return new ModelAndView(viewModel, "user.ftl");
            
        }, new FreeMarkerEngine());
    }
    
}
