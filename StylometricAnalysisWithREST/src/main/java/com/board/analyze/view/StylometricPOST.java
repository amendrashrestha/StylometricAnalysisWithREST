/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.board.analyze.view;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
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

        String input = "This is test";

        ClientResponse cr = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, input);
        System.out.println("Output");
        String output = cr.getEntity(String.class);
        System.out.println(output);
    }
    
}
