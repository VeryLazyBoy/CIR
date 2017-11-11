package com.cir.controllers;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Helper class for debugging 
 **/
public class JsonLogic {
    private JsonDatasetsIR datasetsIR = new JsonDatasetsIR();
    public static void main(String[] args) {
        new JsonLogic().run();
    }
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
//        try {
////            mapper.writeValue(new File("resources/author_bar.json"), 
////                    datasetsIR.getTopAuthorsWithVenue(10, "arXiv"));
////            mapper.writeValue(new File("resources/article_bar.json"),
////                    datasetsIR.getTopArticlesWithVenue(5, "arXiv"));
////            mapper.writeValue(new File("resources/year_line.json"),
////                    datasetsIR.getPublicationNumAcrossYearWithVenue("arXiv"));
////            mapper.writeValue(new File("resources/network.json"),
////                    datasetsIR.getBaseArticleNetwork("Low-density parity check codes over GF(q)"));
//////            datasetsIR.getAuthors();
////            mapper.writeValue(new File("resources/venue_sector.json"),
////                    datasetsIR.getPublicationNumForVenuesInYear(2016));
//        } catch (JsonGenerationException e) {
//            e.printStackTrace();
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
