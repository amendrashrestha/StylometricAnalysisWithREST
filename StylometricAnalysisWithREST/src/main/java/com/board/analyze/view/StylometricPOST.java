/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.board.analyze.view;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author ITE
 */
public class StylometricPOST {
    
    public static void main(String args[]) {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource wr = client.resource("http://localhost:8080/StylometricAnalysisWithREST/rest/stylometric/post");

        String input = "This is test1";
        String input1 = "This is test2";
        
        List<String> inputList = new ArrayList<String>();
        inputList.add(input);
        inputList.add(input1);
        String jsonList = new Gson().toJson(inputList);
        

        ClientResponse cr = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonList);
        System.out.println("Output");
        String output = cr.getEntity(String.class);
        System.out.println(output);
    }
    
}
