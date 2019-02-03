package com.cir.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cir.controllers.jsonobj.ArticleNetwork;
import com.cir.controllers.jsonobj.AuthorBar;
import com.cir.controllers.jsonobj.CitedAuthorBar;
import com.cir.controllers.jsonobj.ConfLineWithLabel;
import com.cir.controllers.jsonobj.PaperBar;
import com.cir.controllers.jsonobj.Places;
import com.cir.controllers.jsonobj.WordCloud;
import com.cir.controllers.jsonobj.YearLineWithLabel;

@Path("/json")
public class JsonService {
    @GET
    @Path("/authors")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Sorted by number of papers written; if numbers of papers are
     * the same, the one whose name alphabetically smaller ranks first; if name still same,
     * the one whose id alphabetically smaller ranks first.
     **/
    public Response getTopAuthorsWithVenueInJson(@QueryParam("conf") String conf,
            @QueryParam("top") Integer top, @QueryParam("year") Integer year) {
        List<AuthorBar> result = new ArrayList<>();
        if (top != null) {
            if (conf != null && year != null) {
                result = DbHandler.createTopAuthorBar(conf, year, top);
            } else if (year != null) {
                result = DbHandler.createTopAuthorBar(year, top);
            } else if (conf != null) {
                result = DbHandler.createTopAuthorBar(conf, top);
            } else {
                ;
            }
        }
        return Response.ok()
                       .entity(result)
                       .header("Access-Control-Allow-Origin", "*")
                       .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                       .allow("OPTIONS")
                       .build();
    }
    
    @GET
    @Path("/citedauthors")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Sorted by number of papers cited by others; if numbers of papers are
     * the same, the one whose name alphabetically smaller ranks first; if name still same,
     * the one whose id alphabetically smaller ranks first.
     **/
    public Response getTopCitedAuthorsWithVenueInJson(@QueryParam("conf") String conf,
            @QueryParam("top") Integer top, 
            @QueryParam("year") Integer year) {
        List<CitedAuthorBar> result = new ArrayList<>();
        if (top != null) {
            if (conf != null && year != null) {
                result = DbHandler.createTopCitedAuthorBar(conf, year, top);
            } else if (year != null) {
                result = DbHandler.createTopCitedAuthorBar(year, top);
            } else if (conf != null) {
                result = DbHandler.createTopCitedAuthorBar(conf, top);
            } else {
                ;
            }
        }
        return Response.ok()
                       .entity(result)
                       .header("Access-Control-Allow-Origin", "*")
                       .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                       .allow("OPTIONS")
                       .build();
    }

    @GET
    @Path("/citations")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Sorted by times being cited; if numbers of in citations are
     * the same, the one whose title alphabetically smaller ranks first; if title still same,
     * the one whose id alphabetically smaller ranks first.
     **/
    public Response getTopCitationsWithVenueInJson(@QueryParam("conf") String conf,
            @QueryParam("top") Integer top, 
            @QueryParam("year") Integer year) {
        List<PaperBar> result = new ArrayList<>();
        if (top != null) {
            if (conf != null && year != null) {
                result = DbHandler.createTopPaperBar(conf, year, top);
            } else if (year != null) {
                result = DbHandler.createTopPaperBar(year, top);
            } else if (conf != null) {
                result = DbHandler.createTopPaperBar(conf, top);
            } else {
                ;
            }
        }
        return Response.ok()
                       .entity(result)
                       .header("Access-Control-Allow-Origin", "*")
                       .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                       .allow("OPTIONS")
                       .build();
    }
    
    @GET
    @Path("/authors/citations")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Sorted by times being cited; if numbers of in citations are
     * the same, the one whose title alphabetically smaller ranks first; if title still same,
     * the one whose id alphabetically smaller ranks first.
     **/
    public Response getTopCitationsWithAuthorInJson(@QueryParam("author") String author,
            @QueryParam("top") Integer top) {
        List<PaperBar> result = new ArrayList<>();
        if (top != null) {
            if (author != null) {
                result = DbHandler.createTopPaperBarForAuthor(author, top);
            } else {
                ;
            }
        }
        return Response.ok()
                       .entity(result)
                       .header("Access-Control-Allow-Origin", "*")
                       .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                       .allow("OPTIONS")
                       .build();
    }

    @GET
    @Path("/keywords")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Sorted by year.
     **/
    public Response getCiationWordFrequencyIn(@QueryParam("conf") String conf,
            @QueryParam("year") Integer year){
        List<WordCloud> result = DbHandler.createWordCloud(conf, year);
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
    public Response getBaseArticleNetwork(@QueryParam("paper1") String base, 
            @QueryParam("paper2") String anotherBase) {
        List<ArticleNetwork> result = new ArrayList<>();
        if (base != null && anotherBase != null) {
            result.add(DbHandler.getBaseArticleNetwork(base));
            result.add(DbHandler.getBaseArticleNetwork(anotherBase));
        }
        return Response.ok()
                .entity(result)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS")
                .build();
    }

    @GET
    @Path("/yeartransitions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCitedDocNumberAcrossYearForVenueWithYears(@QueryParam("conf") String conf, 
            @QueryParam("years") String yearId, 
            @QueryParam("syear") Integer sYear, 
            @QueryParam("eyear") Integer eYear) {
        try {
            Integer[] yearIds = (Integer[]) Arrays.asList(yearId.split("\\$\\$")).stream().map(Integer::valueOf).toArray(Integer[]::new);
            List<YearLineWithLabel> ylwls = new ArrayList<>();
            if (conf == null || yearId == null || sYear == null || eYear == null) {
                ;
            } else {
                for (int year : yearIds) {
                   YearLineWithLabel ylwl = DbHandler.createYearLine(conf, year, sYear, eYear);
                   ylwls.add(ylwl);
                } 
            }
            return Response.ok()
                    .entity(ylwls)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS")
                    .build();
        } catch (Exception e) {
            return Response.ok()
                    .entity("")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS")
                    .build();
        }
    }
    
    @GET
    @Path("/conftransitions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCitedDocNumberAcrossVenueForVenueWithYears(@QueryParam("conf") String conf, 
            @QueryParam("years") String yearId, 
            @QueryParam("conflist") String confString) {
        try {
            List<ConfLineWithLabel> vlwls = new ArrayList<>();
            if (conf == null || yearId == null || confString == null) {
                ;
            } else {
                Integer[] yearIds = (Integer[]) Arrays.asList(yearId.split("\\$\\$")).stream().map(Integer::valueOf).toArray(Integer[]::new);
                String[] conflist = confString.split("\\$\\$");
                for (int year : yearIds) {
                   ConfLineWithLabel vlwl = DbHandler.createPlaceLine(conf, year, conflist);
                   vlwls.add(vlwl);
                } 
            }
            return Response.ok()
                    .entity(vlwls)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS")
                    .build();
        } catch (Exception e) {
            return Response.ok()
                    .entity("")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS")
                    .build();
        }
    }
    
    @GET
    @Path("/confcontemporaries")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCitedDocNumberAcrossVenueForVenuesWithYear(@QueryParam("confs") String confId, 
            @QueryParam("year") Integer year, 
            @QueryParam("conflist") String confString) {
        try {
            List<ConfLineWithLabel> vlwls = new ArrayList<>();
            if (confId == null || year == null || confString == null) {
                ;
            } else {
                String[] confIds = confId.split("\\$\\$");
                String[] conflist = confString.split("\\$\\$");
                for (String c : confIds) {
                   ConfLineWithLabel vlwl = DbHandler.createPlaceLine(c, year, conflist);
                   vlwls.add(vlwl);
                } 
            }
            return Response.ok()
                    .entity(vlwls)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS")
                    .build();
        } catch (Exception e) {
            return Response.ok()
                    .entity("")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS")
                    .build();
        }
    }
    
    @GET
    @Path("/yearcontemporaries")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCitedDocNumberAcrossYearForVenuesWithYear(@QueryParam("confs") String confId, 
            @QueryParam("year") Integer year,
            @QueryParam("syear") Integer sYear, 
            @QueryParam("eyear") Integer eYear) {
        try {
            List<YearLineWithLabel> ylwls = new ArrayList<>();
            if (confId == null || year == null || sYear == null || eYear == null) {
                ;
            } else {
                String[] confIds = confId.split("\\$\\$");
                for (String c : confIds) {
                   YearLineWithLabel ylwl = DbHandler.createYearLine(c, year, sYear, eYear);
                   ylwls.add(ylwl);
                } 
            }
            return Response.ok()
                    .entity(ylwls)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS")
                    .build();
        } catch (Exception e) {
            return Response.ok()
                    .entity("")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS")
                    .build();
        }
    }
    
    @GET
    @Path("/places")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaces(@QueryParam("limit") int limit,
            @QueryParam("keyword") String keyword) {
        
        try {
            Places p = new Places();
            if (limit == 0 || keyword == null) {
                ;
            } else {
                p = DbHandler.getPlaces(keyword, limit);
            }
            return Response.ok()
                    .entity(p)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS")
                    .build();
        } catch (Exception e) {
            return Response.ok()
                    .entity("")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS")
                    .build();
        }
    }
}
