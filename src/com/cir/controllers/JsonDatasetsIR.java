package com.cir.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.cir.controllers.jsonobj.ArticleBar;
import com.cir.controllers.jsonobj.ArticleNetwork;
import com.cir.controllers.jsonobj.ArticleToSend;
import com.cir.controllers.jsonobj.AuthorBar;
import com.cir.controllers.jsonobj.Link;
import com.cir.controllers.jsonobj.YearLine;
import com.cir.models.json.Article;
import com.cir.models.json.ArticleHandler;
import com.cir.models.json.Author;
import com.cir.utility.FixedSizeSortedList;

public class JsonDatasetsIR {
    ArticleHandler articleHandler = new ArticleHandler();
    
    // Q1
    public List<AuthorBar> getTopTenAuthorsWithVenue(String venue) {
        List<Article> articlesWithVenue = articleHandler.getArticlesWithVenue(venue);
        List<Author> authors = articleHandler.getAuthorsInArticles(articlesWithVenue);
        return getTopTenAuthors(authors);
    }

    private List<AuthorBar> getTopTenAuthors(List<Author> authors) {
        Map<Author, Integer> authorToPublicationMap = new HashMap<Author, Integer>();
        FixedSizeSortedList<Author> topTenAuthors = new FixedSizeSortedList<>(10,
                (a1, a2)->a1.compareByPublications(a2, authorToPublicationMap));
        for (Author a : authors) {
            if (authorToPublicationMap.containsKey(a)) {
                int newSize = authorToPublicationMap.get(a) + 1;
                authorToPublicationMap.put(a, newSize);
                topTenAuthors.add(a);
            } else {
                authorToPublicationMap.put(a, 1);
                topTenAuthors.add(a);
            }
        }
        List<AuthorBar> results = new ArrayList<>();
        for (Author a : topTenAuthors.getSortedResultList()) {
            AuthorBar ab = new AuthorBar(a.getName(), authorToPublicationMap.get(a));
            results.add(ab);
        }
        return results;
    }

    // Q2
    public List<ArticleBar> getTopFiveArticlesWithVenue(String venue) {
        List<Article> articlesWithVenue = articleHandler.getArticlesWithVenue(venue);
        return getTopFiveArticles(articlesWithVenue);
    }

    private List<ArticleBar> getTopFiveArticles(List<Article> articles) {
        FixedSizeSortedList<Article> topFiveArticles = new FixedSizeSortedList<>(5,
                Article::compareByCitations);
        System.out.println(articles.size());
        for (Article a : articles) {
            topFiveArticles.add(a);
        }
        
        List<ArticleBar> results = new ArrayList<>();
        for(Article a : topFiveArticles.getSortedResultList()) {
            ArticleBar ab = new ArticleBar(a.getTitle(), a.getCitationTimes());
            results.add(ab);
        }
        return results;
    }

    // Q3
    public List<YearLine> getPublicationNumAcrossYearWithVenue(String venue) {
        List<Article> articlesWithVenue = articleHandler.getArticlesWithVenue(venue);
        List<Integer> years = articleHandler.getYearsInArticles(articlesWithVenue);
        return getPublicationNumbAcrossYear(years);
    }

    private List<YearLine> getPublicationNumbAcrossYear(List<Integer> years) {
        Map<Integer, Integer> yearToPublicationMap = new HashMap<Integer, Integer>();
        for (Integer y : years) {
            if (yearToPublicationMap.containsKey(y)) {
                int newSize = yearToPublicationMap.get(y)  + 1;
                yearToPublicationMap.put(y, newSize);
            } else {
                yearToPublicationMap.put(y, 1);
            }
        }
        TreeSet<Integer> treeSet = new TreeSet<>(yearToPublicationMap.keySet());
        List<YearLine> results = new ArrayList<>();
        for (Integer y : treeSet) {
            YearLine yl = new YearLine(y, yearToPublicationMap.get(y));
            results.add(yl);
        }
        return results;
    }

    // Q4
    public ArticleNetwork getBaseArticleNetwork(String title) {
        ArticleNetwork result = new ArticleNetwork();
        List<ArticleToSend> articlesToSend = new ArrayList<>();
        List<Link> links = new ArrayList<>();
        List<Article> articlesWithTitle = articleHandler.getArticlesWithTitle(title);
        
        if (articlesWithTitle.isEmpty()) {
            return result;
        }
        Article base = articlesWithTitle.get(0);
        ArticleToSend baseToSend = new ArticleToSend(base.getId(),
                                                     base.getTitle(),
                                                     base.getAuthorNames(),
                                                     1);
        articlesToSend.add(baseToSend);
        List<Article> articlesCitingBase = base.getInCitations().stream().map(s->articleHandler.getArticleById(s)).collect(Collectors.toList());
        for (Article a : articlesCitingBase) {
            Link link = new Link(base.getId(), a.getId());
            links.add(link);
            ArticleToSend citation = new ArticleToSend(a.getId(), a.getTitle(), a.getAuthorNames(), 2);
            articlesToSend.add(citation);
            List<Article> secondLevelCitingBase = a.getInCitations().stream().map(s->articleHandler.getArticleById(s)).collect(Collectors.toList());
            for (Article b : secondLevelCitingBase) {
                Link sLink = new Link(a.getId(), b.getId());
                links.add(sLink);
                ArticleToSend sCitation = new ArticleToSend(b.getId(), b.getTitle(), b.getAuthorNames(), 3);
                articlesToSend.add(sCitation);
            }
        }
        result.setArticles(articlesToSend);
        result.setLinks(links);
        return result;
    }

    public Set<Author> getAuthors() {
        Set<Author> allAuthors = articleHandler.getAllUniqueAuthors();
        try {
            PrintWriter pw = new PrintWriter(new File("resources/123.txt"));
            for (Author a : allAuthors) {
                a.hashCode();
                System.out.println(a.hashCode());
                pw.println(a.getName());
            }
            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return allAuthors;
    }
}
