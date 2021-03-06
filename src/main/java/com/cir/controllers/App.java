package com.cir.controllers;

import java.io.IOException;
import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class App {
 // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";
//    public static final String BASE_URI = "http://0.0.0.0:8080/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.cir.controllers package
        final ResourceConfig rc = new ResourceConfig().packages("com.cir.controllers");

        Logger l = Logger.getLogger("org.glassfish.grizzly.http.server.HttpHandler");
        l.setLevel(Level.FINE);
        l.setUseParentHandlers(false);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        l.addHandler(ch);
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        DbHandler.connectToDb();
//        DbHandler.createYearLine("PERSUASIVE", 2010, 0, 2010);
//        DbHandler.createVenueLine("PERSUASIVE", 2010, "CHI Extended Abstracts", "Behaviour research and therapy");
//        DbHandler.close();
//        DbHandler.createTopAuthorBar("arxiv", 2003, 5);
//        DbHandler.createTopAuthorBar("arxiv", 5);
//        DbHandler.createTopAuthorBar(2003, 5);
//        DbHandler.createTopCitedAuthorBar("arxiv", 2003, 5);
//        DbHandler.createTopCitedAuthorBar("arxiv", 5);
//        DbHandler.createTopCitedAuthorBar(2003, 100);
//        DbHandler.getBaseArticleNetwork("Low-density parity check codes over GF(q)");
        DbHandler.createWordCloud("arxiv", 2008);
        
        System.in.read();
        server.shutdown();
    }
}
