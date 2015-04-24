/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lightredlloyd;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ssl.SslContextFactory;
/**
 *
 * @author johncox
 */
public class MsApi {
    private final String token;
    
    public MsApi(String token){
        this.token = token;
    }
    
    public HashMap<String, Object> makeCall(String payload, String method, String url){
        
        try {
            SslContextFactory sslContextFactory = new SslContextFactory(true);
            sslContextFactory.setEndpointIdentificationAlgorithm(null);
            HttpClient httpClient = new HttpClient(sslContextFactory);
            httpClient.setFollowRedirects(true);
            httpClient.start();

            StringContentProvider strProvider = new StringContentProvider(payload);

            Request request = httpClient.newRequest(url)
                         .method(HttpMethod.fromString(method))
                         .content(strProvider, "application/json");

            if (this.headers() != null) {
              this.headers().forEach((k, v) -> request.header(k, v));
            }

            ContentResponse r = request.send();

            System.out.println(r.getContentAsString());
            HashMap<String,Object> result = new ObjectMapper().readValue(r.getContentAsString(), HashMap.class);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(MsApi.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap();
        }
    }
    
    private Map<String, String> headers(){
        Map<String, String> ret = new HashMap<String, String>();
        ret.put("Content-Type", "application/json");
        ret.put("Accept", "application/json");
        if(this.token != null){
            ret.put("Authorization",this.authHeader());
        }
        return ret;
    }
    
    private String authHeader(){
        String fullAuth = this.token + ":X";
        String encoded = Base64.getEncoder().encodeToString(fullAuth.getBytes());
        return "Basic " + encoded;
    }
}
