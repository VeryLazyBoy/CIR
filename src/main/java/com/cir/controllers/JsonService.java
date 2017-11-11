package com.cir.controllers;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
    public List<AuthorBar> getTopAuthorsWithVenueInJson(@QueryParam("venue") String venue,
            @QueryParam("top") int top) {
        return datasetsIR.getTopAuthorsWithVenue(top, venue);
    }

    @GET
    @Path("/articles")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Sorted by times being cited; if numbers of in citations are
     * the same, the one whose title alphabetically smaller ranks first; if title still same,
     * the one whose id alphabetically smaller ranks first.
     **/
    public List<ArticleBar> getTopArticlesWithVenueInJson(@QueryParam("venue") String venue,
            @QueryParam("top") int top) {
        return datasetsIR.getTopArticlesWithVenue(top, venue);
    }

    @GET
    @Path("/publications")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Sorted by year.
     **/
    public List<YearLine> getPublicationNumAcrossYearWithVenueInJson(@QueryParam("venue") String venue){
        return datasetsIR.getPublicationNumAcrossYearWithVenue(venue);
    }

    @GET
    @Path("/networks")
    @Produces(MediaType.APPLICATION_JSON)
    public ArticleNetwork getBaseArticleNetwork(@QueryParam("base") String base) {
        return datasetsIR.getBaseArticleNetwork(base);
    }

    @GET
    @Path("/venues")
    @Produces(MediaType.APPLICATION_JSON)
    public List<VenueSector> getPublciationNumForVenuesInYear(@QueryParam("year") int year) {
        return datasetsIR.getPublicationNumForVenuesInYear(year);
    }
}
