package com.cir.controllers;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonLogic {
    private JsonDatasetsIR datasetsIR = new JsonDatasetsIR();
    public static void main(String[] args) {
        new JsonLogic().run();
    }
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("resources/author_bar.json"), 
                    datasetsIR.getTopTenAuthorsWithVenue("abc"));
            mapper.writeValue(new File("resources/article_bar.json"),
                    datasetsIR.getTopFiveArticlesWithVenue("abc"));
            mapper.writeValue(new File("resources/year_line.json"),
                    datasetsIR.getPublicationNumAcrossYearWithVenue("abc"));
            mapper.writeValue(new File("resources/network.json"),
                    datasetsIR.getBaseArticleNetwork("rocks"));
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // To sort by number of publications descendingly, if same, then author with small name ranks first.
        
//        datasetsIR.getTopFiveArticlesWithVenue("abc");
//        datasetsIR.getPublicationNumAcrossYearWithVenue("abc");
    }
}
