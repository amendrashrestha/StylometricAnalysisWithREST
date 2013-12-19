/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.board.analyze.view;

import com.board.analyze.controller.StylometricAnalysisMain;
import com.user.cluster.clustering.Cluster;
import com.user.cluster.parser.model.Posts;
import com.user.cluster.parser.model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
    public Float returnStylometricJSON(@QueryParam("text1") String text1, @QueryParam("text2") String text2) throws SQLException, IOException{
        List<String> firstList = new ArrayList<String>();
        firstList.add(text1);
        List<String> secondList = new ArrayList<String>();
        secondList.add(text2);
        double styloResult = init.returnStylo(firstList, secondList);
        return (float) styloResult;
    } 
    
    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)       
    public List<Float> postStylometric(List input) throws SQLException, IOException{
        List<Float> stylo = init.executePostAnalysis(input);       
        return stylo;
    }  
    
    @POST
    @Path("/cluster")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)       
    public HashMap<Integer, int[]> clusterUser(List<User> input) throws SQLException, IOException{
        Cluster init = new Cluster();
        HashMap<Integer, int[]> stylo = init.getAllClusterizedUser(input);       
        return stylo;
    }
}
