/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.board.analyze.view;

import com.board.analyze.controller.StylometricAnalysisMain;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
/**
 *
 * @author ITE
 */

@Path("/stylometric/")
public class StylometricAnalysis {

    StylometricAnalysisMain init = new StylometricAnalysisMain();
    
    @GET
    @Path("/returnStyloJSON/{UserID}")
    @Produces(MediaType.APPLICATION_JSON)    
    public List<Float> returnStylometric(@PathParam("UserID") String ID) throws SQLException, IOException{
        List<Float> styloResult = init.executeAnalysis(ID);
        return styloResult;
    }  
}
