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
        dir = new File("src/main/resources/spark/template/freemarker");
        try{
            cfg.setDirectoryForTemplateLoading(dir);
        } catch (Exception e){
            System.out.println("directory not found");
        }
        get("/hello", (request, response) -> {
            HashMap<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");

            // The hello.ftl file is located in directory:
            // src/main/resources/spark/template/freemarker
            return new ModelAndView(attributes, "hello.ftl");
        }, new FreeMarkerEngine());
        
        post("/auth", (request, response) ->{
            HashMap<String, Object> attributes;
            attributes = new HashMap<>();
            attributes.put("message", request.queryMap("email").value());
            //System.out.println(request.queryMap("email"));
            
            MsApi api = new MsApi("qca9nn5Gpmt6DtkeJ3Vs");
            
            System.out.println(api.makeCall(null, "GET", "https://services.machineshop.io/api/v1/platform/me"));
            
            return new ModelAndView(attributes, "hello.ftl");
        }, new FreeMarkerEngine());
    }
    
}
