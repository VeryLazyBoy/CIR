package com.cir.controllers;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cir.controllers.jsonobj.ArticleBar;
import com.cir.controllers.jsonobj.ArticleNetwork;
import com.cir.controllers.jsonobj.AuthorBar;
import com.cir.controllers.jsonobj.VenueSector;
import com.cir.controllers.jsonobj.YearLine;

@Path("/json")
public class JsonService {
    private JsonDatasetsIR datasetsIR = new JsonDatasetsIR();
    @GET
    @Path("/authors")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Sorted by number of articles written; if numbers of articles are
     * the same, the one whose name alphabetically smaller ranks first; if name still same,
     * the one whose id alphabetically smaller ranks first.
     **/
    public Response getTopAuthorsWithVenueInJson(@QueryParam("venue") String venue,
            @QueryParam("top") int top) {
        List<AuthorBar> result = datasetsIR.getTopAuthorsWithVenue(top, venue);
        return Response.ok()
                       .entity(result)
                       .header("Access-Control-Allow-Origin", "*")
                       .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                       .allow("OPTIONS")
                       .build();
    }

    @GET
    @Path("/articles")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Sorted by times being cited; if numbers of in citations are
     * the same, the one whose title alphabetically smaller ranks first; if title still same,
     * the one whose id alphabetically smaller ranks first.
     **/
    public Response getTopArticlesWithVenueInJson(@QueryParam("venue") String venue,
            @QueryParam("top") int top) {
        List<ArticleBar> result = datasetsIR.getTopArticlesWithVenue(top, venue);
        return Response.ok()
                       .entity(result)
                       .header("Access-Control-Allow-Origin", "*")
                       .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                       .allow("OPTIONS")
                       .build();
    }

    @GET
    @Path("/publications")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Sorted by year.
     **/
    public Response getPublicationNumAcrossYearWithVenueInJson(@QueryParam("venue") String venue){
        List<YearLine> result = datasetsIR.getPublicationNumAcrossYearWithVenue(venue);
        return Response.ok()
                       .entity(result)
                       .header("Access-Control-Allow-Origin", "*")
                       .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                       .allow("OPTIONS")
                       .build();
    }

    @GET
    @Path("/networks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBaseArticleNetwork(@QueryParam("base") String base) {
        ArticleNetwork result = datasetsIR.getBaseArticleNetwork(base);
        return Response.ok()
                .entity(result)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS")
                .build();
    }

    @GET
    @Path("/venues")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublciationNumForVenuesInYear(@QueryParam("year") int year) {
        List<VenueSector> result = datasetsIR.getPublicationNumForVenuesInYear(year);
        return Response.ok()
                .entity(result)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS")
                .build();
        
    }
}
