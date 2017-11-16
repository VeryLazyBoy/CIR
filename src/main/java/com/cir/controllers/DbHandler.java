package com.cir.controllers;

import static com.mongodb.client.model.Accumulators.addToSet;
import static com.mongodb.client.model.Accumulators.first;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Sorts.descending;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.cir.controllers.jsonobj.ArticleNetwork;
import com.cir.controllers.jsonobj.ArticleToSend;
import com.cir.controllers.jsonobj.AuthorBar;
import com.cir.controllers.jsonobj.CitedAuthorBar;
import com.cir.controllers.jsonobj.ConfLineWithLabel;
import com.cir.controllers.jsonobj.ConfTransition;
import com.cir.controllers.jsonobj.Link;
import com.cir.controllers.jsonobj.PaperBar;
import com.cir.controllers.jsonobj.WordCloud;
import com.cir.controllers.jsonobj.YearLineWithLabel;
import com.cir.controllers.jsonobj.YearTransition;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UnwindOptions;

public class DbHandler {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;
    
    public static void connectToDb() {
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDatabase("test2");
        collection = database.getCollection("papers");
    }
    
    public static void close() {
        mongoClient.close();
    }
    
    private static Document getYearFilterDoc(int sYear, int eYear) {
        if (sYear != 0 && eYear != 0) {
            List<Document> andValues = new ArrayList<>();
            andValues.add(new Document("_id", new Document("$gte", sYear)));
            andValues.add(new Document("_id", new Document("$lte", eYear)));
            return new Document("$and", andValues);
        } else if (sYear != 0) {
            return new Document("_id", new Document("$gte", sYear));
        } else if (eYear !=0) {
            return new Document("_id", new Document("$lte", eYear));
        } else {
            return new Document();
        }
    }
    
    private static Document getVenueFilterDoc(String...venueList) {
        String reg = Arrays.asList(venueList).stream().map(Pattern::quote).collect(Collectors.joining( "|" ));
        return new Document("_id", new Document("$regex", reg).append("$options", "i"));
    }

    public static ConfLineWithLabel createVenueLine(String venue, int year, String...venueList) {
        List<String> setUnionValues = new ArrayList<String>();
        setUnionValues.add("$$value");
        setUnionValues.add("$$this");
        List<Document> andValues = new ArrayList<>();
        andValues.add(new Document("citationInfo.venue", 
                new Document("$exists", true).append("$ne", new ArrayList<String>())));
        andValues.add(new Document("citationInfo.venue", new Document("$ne", "")));
        AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
                new Document("$match", 
                        new Document("venue", 
                                new Document("$regex", Pattern.quote(venue))
                                .append("$options", "i"))
                        .append("year", year)),
//                new Document("$group", 
//                        new Document("_id", 
//                                new Document("venue", "$venue")
//                                .append("year", "$year"))
//                        .append("oc", 
//                                new Document("$addToSet", "$outCitations"))),
//                new Document("$addFields", 
//                        new Document("oc", 
//                                new Document("$reduce", 
//                                        new Document("input", "$oc")
//                                        .append("initialValue", 
//                                                new ArrayList<Document>())
//                                        .append("in", 
//                                                new Document("$setUnion", setUnionValues))))),
                new Document("$unwind", "$outCitations"),
                new Document("$lookup", 
                        new Document("from", "papers")
                        .append("localField", "outCitations")
                        .append("foreignField", "id")
                        .append("as", "citationInfo")),
                new Document("$unwind", "$citationInfo"),
                new Document("$match", new Document("$and", andValues)), // One
                new Document("$group", 
                        new Document("_id", "$citationInfo.venue") // Two
                        .append("count", 
                                new Document("$sum", 1))
                        .append("confWithYear", 
                                new Document("$first", "$_id"))),
                new Document("$match", getVenueFilterDoc(venueList)), // Three
                new Document("$sort", new Document("_id", 1)),
                new Document("$project", new Document("conf", "$_id").append("count", "$count"))
                ));
        List<ConfTransition> ts = new ArrayList<>();
        for (Document d : output) {
            System.out.println(d);
            ConfTransition t = new ConfTransition(d.getString("conf"), d.getInteger("count"));
            ts.add(t);
        }
        ConfLineWithLabel vlwl = new ConfLineWithLabel(venue + year, ts);
        return vlwl;
    }

    public static YearLineWithLabel createYearLine(String venue, int year, int sYear, int eYear) {
        List<String> setUnionValues = new ArrayList<String>();
        setUnionValues.add("$$value");
        setUnionValues.add("$$this");
        List<Document> andValues = new ArrayList<>();
        andValues.add(new Document("citationInfo.year", 
                new Document("$exists", true).append("$ne", new ArrayList<String>())));
        andValues.add(new Document("citationInfo.year", new Document("$ne", "")));
        AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
                new Document("$match", 
                        new Document("venue", 
                                new Document("$regex", Pattern.quote(venue))
                                .append("$options", "i"))
                        .append("year", year)),
                new Document("$unwind", "$outCitations"),
                new Document("$lookup", 
                        new Document("from", "papers")
                        .append("localField", "outCitations")
                        .append("foreignField", "id")
                        .append("as", "citationInfo")),
                new Document("$unwind", "$citationInfo"),
                new Document("$match", new Document("$and", andValues)), // One
                new Document("$group", 
                        new Document("_id", "$citationInfo.year") // Two
                        .append("count", 
                                new Document("$sum", 1))
                        .append("confWithYear", 
                                new Document("$first", "$_id"))),
                new Document("$match", getYearFilterDoc(sYear, eYear)), // Three
                new Document("$sort", new Document("_id", 1)),
                new Document("$project", new Document("year", "$_id").append("count", "$count"))
                ));

        List<YearTransition> ts = new ArrayList<>();
        for (Document d : output) {
            System.out.println(d);
            YearTransition t = new YearTransition(d.getInteger("year"), d.getInteger("count"));
            ts.add(t);
        }
        YearLineWithLabel ylwl = new YearLineWithLabel(venue + year, ts);
        return ylwl;
    }

    
    public AggregateIterable<Document> query(List<Document> docs) {
        return collection.aggregate(docs);
    }
    
    public static List<AuthorBar> createTopAuthorBar(String conf, int year, int top) {
        Bson confCondition = conf.equals("") ? exists("venue") : regex("venue", Pattern.compile(conf, Pattern.CASE_INSENSITIVE));
        Bson yearCondition = year == -1 ? exists("year") : eq("year", year);
        AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
                match(and(confCondition, yearCondition)),
                new Document("$unwind", "$authors"), 
                new Document("$group", 
                        new Document("_id", "$authors")
                        .append("count", 
                                new Document("$sum",1))),
                new Document("$sort", 
                        new Document("count", -1).append("_id.name", 1).append("_id.ids", 1)),
                new Document("$limit", top),
                new Document("$project", new Document("name", "$_id.name").append("count", "$count"))));
        
        List<AuthorBar> abs = new ArrayList<>();
        for (Document d : output) {
            System.out.println(d);
            AuthorBar ab = new AuthorBar(d.getString("name"), d.getInteger("count"));
            abs.add(ab);
        }
        return abs;
    }
    
    public static List<AuthorBar> createTopAuthorBar(String conf, int top) {
        if (conf.isEmpty()) {
            return new ArrayList<AuthorBar>();
        }
        return createTopAuthorBar(conf, -1, top);
    }
    
    public static List<AuthorBar> createTopAuthorBar(int year, int top) {
        if (year == -1) {
            return new ArrayList<AuthorBar>();
        }
        return createTopAuthorBar("", year, top);
    }
    
    public static List<CitedAuthorBar> createTopCitedAuthorBar(String conf, int year, int top) {
        Bson confCondition = conf.equals("") ? exists("venue") : regex("venue", Pattern.compile(conf, Pattern.CASE_INSENSITIVE));
        Bson yearCondition = year == -1 ? exists("year") : eq("year", year);
        
        AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
                match(and(confCondition, yearCondition)),
                new Document("$unwind", "$outCitations"),
                new Document("$lookup", 
                        new Document("from", "papers")
                        .append("localField", "outCitations")
                        .append("foreignField", "id")
                        .append("as", "citationInfo"))
                ,
                new Document("$unwind", "$citationInfo"),
                new Document("$unwind", "$citationInfo.authors"),
                new Document("$group", 
                        new Document("_id", 
                                new Document("authorId", "$citationInfo.authors.ids").append("authorName", "$citationInfo.authors.name")) // Two
                        .append("count", 
                                new Document("$sum", 1)).append("citedAuthor", new Document("$first", "$citationInfo.authors.name"))),
                new Document("$sort", new Document("count", -1).append("citedAuthor", 1).append("_id", 1)),
                new Document("$limit", top)
                ));
        List<CitedAuthorBar> cabs = new ArrayList<>();
        for (Document d : output) {
            System.out.println(d);
            CitedAuthorBar cab = new CitedAuthorBar(d.getString("citedAuthor"), d.getInteger("count"));
            cabs.add(cab);
        }
        return cabs;
    }
    
    public static List<CitedAuthorBar> createTopCitedAuthorBar(String conf, int top) {
        if (conf.isEmpty()) {
            return new ArrayList<CitedAuthorBar>();
        }
        return createTopCitedAuthorBar(conf, -1, top);
    }
    
    public static List<CitedAuthorBar> createTopCitedAuthorBar(int year, int top) {
        if (year == -1) {
            return new ArrayList<CitedAuthorBar>();
        } else {
            return createTopCitedAuthorBar("", year, top);
        }
        
    }
    
    public static List<PaperBar> createTopPaperBar(String conf, int year, int top) {
        Bson confCondition = conf.equals("") ? exists("venue") : regex("venue", Pattern.compile(conf, Pattern.CASE_INSENSITIVE));
        Bson yearCondition = year == -1 ? exists("year") : eq("year", year);
        
        AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
                match(and(confCondition, yearCondition)),
                new Document("$lookup", 
                        new Document("from", "papers")
                        .append("localField", "outCitations")
                        .append("foreignField", "id")
                        .append("as", "citationInfo")),
                new Document("$unwind", "$citationInfo"),
                new Document("$group", 
                        new Document("_id", "$citationInfo.id")
                        .append("paper", 
                                new Document("$first", "$citationInfo.title"))
                        .append("count", 
                                new Document("$sum", 1))),
                new Document("$sort", new Document("count", -1).append("paper", 1).append("_id", 1)),
                new Document("$limit", top)));
        List<PaperBar> pbs = new ArrayList<>();
        for (Document d : output) {
            System.out.println(d);
            PaperBar pb = new PaperBar(d.getString("paper"), d.getInteger("count"));
            pbs.add(pb);
        }
        return pbs;
    }
    
    public static List<PaperBar> createTopPaperBar(String conf, int top) {
        if (conf.isEmpty()) {
            return new ArrayList<PaperBar>();
        }
        return createTopPaperBar(conf, -1, top);
    }
    
    public static List<PaperBar> createTopPaperBar(int year, int top) {
        if (year == -1) {
            return new ArrayList<PaperBar>();
        }
        return createTopPaperBar("", year, top);
    }
    
    // Missing co-author, author's name is not unique; ui title
    public static List<PaperBar> createTopPaperBarForAuthor(String author, int top) {
        AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
                match(and(exists("authors.ids"), regex("authors.name", Pattern.compile(Pattern.quote(author), Pattern.CASE_INSENSITIVE)))),
                project(Document.parse("{ count: { $size:'$inCitations' }, paper:'$title'}")),
                sort(descending("count")),
                limit(top)
                ));
        
        List<PaperBar> pbs = new ArrayList<>();
        for (Document d : output) {
            System.out.println(d);
            PaperBar pb = new PaperBar(d.getString("paper"), d.getInteger("count"));
            pbs.add(pb);
        }
        return pbs;
                
    }

    @SuppressWarnings("unchecked")
    public static List<String> getAllAuthorsFromPaper(Document basePaper) {
        List<Document> docs = ((ArrayList<Document>)basePaper.get("authors"));
        List<String> names = new ArrayList<>();
        for (Document d : docs) {
            names.add(d.getString("name"));
        }
        return names;
    }

    public static AggregateIterable<Document> getSecondLevelPapers(String baseTitle) {
        List<String> setUnionValues = new ArrayList<String>();
        setUnionValues.add("$$value");
        setUnionValues.add("$$this");
        UnwindOptions uo = new UnwindOptions();
        uo.preserveNullAndEmptyArrays(true);
        AggregateIterable<Document> papersCitingBase = collection.aggregate(Arrays.asList(
                match(regex("title", Pattern.compile(Pattern.quote(baseTitle), Pattern.CASE_INSENSITIVE))),
                limit(1),
                unwind("$inCitations"),
                lookup("papers", "inCitations", "id", "citationInfo"),
                unwind("$citationInfo"),
                project(Document.parse(
                        "{id : '$citationInfo.id',"+ 
                        " authors: '$citationInfo.authors', "+
                        "title: '$citationInfo.title', "+
                        "ic: '$citationInfo.inCitations'}")),
                unwind("$ic", uo),
                lookup("papers", "ic", "id", "citationInfo"),
                group("$id", 
                      first("id", "$id"), 
                      first("authors", "$authors"), 
                      first("title","$title"), 
                      addToSet("citationInfo", "$citationInfo")),
                new Document("$addFields", 
                      new Document("citationInfo", 
                              new Document("$reduce", 
                                      new Document("input", "$citationInfo")
                                      .append("initialValue", 
                                              new ArrayList<Document>())
                                      .append("in", 
                                              new Document("$setUnion", setUnionValues)))))
                ));
        return papersCitingBase;
    }

    @SuppressWarnings("unchecked")
    public static ArticleNetwork getBaseArticleNetwork(String title) {
        ArticleNetwork result = new ArticleNetwork();
        List<ArticleToSend> articlesToSend = new ArrayList<>();
        List<Link> links = new ArrayList<>();
        
        Document basePaper = collection.find(regex("title", Pattern.compile(Pattern.quote(title), Pattern.CASE_INSENSITIVE))).first();
        if (basePaper == null) {
            return result;
        }
        ArticleToSend baseToSend = new ArticleToSend(basePaper.getString("id"), 
                basePaper.getString("title"), 
                getAllAuthorsFromPaper(basePaper), 
                1);
        articlesToSend.add(baseToSend);

       AggregateIterable<Document> papersCitingBase = getSecondLevelPapers(title);

        for (Document d : papersCitingBase) {
            Link link = new Link(basePaper.getString("id"), d.getString("id"));
            links.add(link);
            ArticleToSend citation = new ArticleToSend(d.getString("id"), 
                    d.getString("title"), 
                    getAllAuthorsFromPaper(d), 
                    2);
            articlesToSend.add(citation);
            List<Document> secondLevelCitingBase = (List<Document>) d.get("citationInfo");
            for (Document dd : secondLevelCitingBase) {
                Link slink = new Link(d.getString("id"), dd.getString("id"));
                links.add(slink);
                ArticleToSend scitation = new ArticleToSend(dd.getString("id"), 
                        dd.getString("title"), 
                        getAllAuthorsFromPaper(dd), 
                        3);
                articlesToSend.add(scitation);
            }
        }
        result.setArticles(articlesToSend);
        result.setLinks(links);
        return result;
    }
    
    public static List<WordCloud> createWordCloud(String conf, int year) {
        Bson confCondition = conf.equals("") ? exists("venue") : regex("venue", Pattern.compile(conf, Pattern.CASE_INSENSITIVE));
        Bson yearCondition = year == -1 ? exists("year") : eq("year", year);
        
        AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
                match(and(confCondition, yearCondition)),
                new Document("$lookup", 
                        new Document("from", "papers")
                        .append("localField", "outCitations")
                        .append("foreignField", "id")
                        .append("as", "citationInfo")),
                new Document("$unwind", "$citationInfo"),
                unwind("$keyPhrases"),
                group("$keyPhrases", sum("count", 1))
                ));
        List<WordCloud> wcs = new ArrayList<>();
        for (Document d : output) {
            System.out.println(d);
            WordCloud wc = new WordCloud(d.getString("_id"), d.getInteger("count"));
            wcs.add(wc);
        }
        return wcs;
    }
    
}
