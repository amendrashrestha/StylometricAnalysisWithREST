/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.board.analyze.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ITE
 */
public class Alias {

    private List<Float> featureVector;
    private int nrOfFeatures;
    private String name;
    private ArrayList<ArrayList<Float>> featureVectorPostList;
    private List<String> posts;

    public Alias(String name) {
        this.name = name;
        setNrOfFeatures(293 + 20 + 26 + 21 + 6);
        posts = new ArrayList<String>();
        featureVector = new ArrayList<Float>();
        featureVectorPostList = new ArrayList<ArrayList<Float>>();
    }

    public ArrayList<ArrayList<Float>> initializeFeatureVectorPostList() {
        ArrayList<ArrayList<Float>> list = new ArrayList<ArrayList<Float>>();
        for (int j = 0; j < posts.size(); j++) {
            ArrayList<Float> featList = new ArrayList<Float>();
            for (int i = 0; i < nrOfFeatures; i++) {
                featList.add(0.0f);
            }
            list.add(featList);
        }
        return list;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

    public void addPost(String post) {
        posts.add(post);
    }

    public void addToFeatureVectorPostList(ArrayList<Float> freqDist, int start, int index) {
        for (int i = 0; i < freqDist.size(); i++) {
            featureVectorPostList.get(index).set(start, freqDist.get(i));
            start++;
        }
    }

    public List<Float> getFeatureVector() {
        return featureVector;
    }

    public void setFeatureVector(List<Float> featureVector) {
        this.featureVector = featureVector;
    }

    public ArrayList<ArrayList<Float>> getFeatureVectorPosList() {
        return featureVectorPostList;
    }

    public void setFeatureVectorPosList(ArrayList<ArrayList<Float>> list) {
        featureVectorPostList = list;
    }

    public void setFeatureValue(int index, float newValue) {
        featureVector.set(index, newValue);
    }

    public int getNrOfFeatures() {
        return nrOfFeatures;
    }

    public void setNrOfFeatures(int nrOfFeatures) {
        this.nrOfFeatures = nrOfFeatures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
