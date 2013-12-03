/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.board.analyze.view;

import com.board.analyze.controller.StylometricAnalysisMain;
import java.sql.SQLException;
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
    StylometricAnalysisMain init = new StylometricAnalysisMain();
    
    @GET
    @Path("/returnStyloJSON/{UserID}")
    @Produces(MediaType.APPLICATION_JSON)    
    public Response returnStylometric(@PathParam("UserID") int ID) throws SQLException{
        rb = Response.ok(ID).build();
        
        init.executeStylo(null);
        
        return rb;
    }
    
    
    
}
