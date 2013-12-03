/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.board.analyze.view;

import com.board.analyze.controller.StylometricAnalysisMain;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    public Response returnStylometric(@PathParam("UserID") String ID) throws SQLException, IOException{
        rb = Response.ok(ID).build();
        init.executeAnalysis(ID);
        return rb;
    }
    
    
    
}
