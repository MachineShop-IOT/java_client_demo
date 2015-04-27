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
        
        
        // The home page.
        get("/", (request, response) -> {
            HashMap<String, Object> attributes = new HashMap<>();

            // The freemarker files are located in directory:
            // src/main/resources/spark/template/freemarker
            return new ModelAndView(attributes, "home.ftl");
        }, new FreeMarkerEngine());
        
        
        // A form for authenticating.
        get("/login", (request, response) -> {
            HashMap<String, Object> attributes = new HashMap<>();
            return new ModelAndView(attributes, "login.ftl");
        }, new FreeMarkerEngine());
        
        
        // Our first API call to authenticate!
        post("/auth", (Request request, Response response) ->{
            
            // Create the JSON post body for authentication.
            HashMap<String, Object> attributes;
            attributes = new HashMap<>();
            attributes.put("email", request.queryMap("email").value());
            attributes.put("password", request.queryMap("pw").value());
             
            // Make the api call and set the api user.
            HashMap<String, Object> user = api.makeCall(api.mapToJson(attributes), "POST", "https://services.machineshop.io/api/v1/platform/user/authenticate");
            
            // Put the user object into the session for later use.
            request.session(true);
            request.session().attribute("user", user);
            
            // Create the obejct needed for the view 
            HashMap<String, Object> viewModel = new HashMap<>();
            viewModel.put("user", api.mapToJson(user));
            
            return new ModelAndView(viewModel, "user.ftl");
            
        }, new FreeMarkerEngine());
        
        // List data sources.
        get("/data_sources", (Request request, Response response) -> {
            HashMap<String,Object> user = (HashMap<String,Object>)request.session().attribute("user");
            String token = (String)user.get("authentication_token");
            HashMap<String,Object> dataSources = new MsApi(token).makeCall("", "GET", "https://services.machineshop.io/api/v1/platform/data_sources?page_meta=true");
            
            // Create the obejct needed for the view 
            HashMap<String, Object> viewModel = new HashMap<>();
            viewModel.put("data_sources", dataSources.get("resources"));
            return new ModelAndView(viewModel, "data_sources.ftl");
        }, new FreeMarkerEngine());
        
        // Retrieve a data source.
        
        // Create a data source.
        
        // Create a report.
    }
    
}
