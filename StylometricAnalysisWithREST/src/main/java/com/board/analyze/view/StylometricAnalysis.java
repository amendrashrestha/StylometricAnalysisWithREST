/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.board.analyze.view;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ITE
 */

@Path("/stylometric/")
public class StylometricAnalysis {
    
    Response rb = null;
    
    static{
        System.out.println("This is test");
    }
    
    @GET
    @Path("/returnStyloJSON/{UserID}")
    @Produces(MediaType.APPLICATION_JSON)
    
    public Response returnStylometric(@PathParam("UserID") int ID){
        rb = Response.ok(ID).build();        
        return rb;
    }
    
}
