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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    
    @GET
    @Path("/returnStylometricJSON")
    @Produces(MediaType.APPLICATION_JSON)    
    public List<Float> returnStylometricJSON(@QueryParam("text1") String text1, @QueryParam("text2") String text2) throws SQLException, IOException{
        List<String> posts = new ArrayList<String>();
        posts.add(text1);
        posts.add(text2);
        List<Float> styloResult = init.executePostAnalysis(posts);
        return styloResult;
    } 
    
    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)       
    public List<Float> postStylometric(List input) throws SQLException, IOException{
        List<Float> stylo = init.executePostAnalysis(input);       
        return stylo;
    }  
}
