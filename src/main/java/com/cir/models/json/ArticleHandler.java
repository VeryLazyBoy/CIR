package com.cir.models.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArticleHandler {

	List<Article> allArticles = new ArrayList<>();
	Map<String, Article> idToArticleMap = new HashMap<>();

	private String filePath = "datasets/json/dataset.json";

	public ArticleHandler() {
//		unmarshal();
//		populateIdToArticleMap();
	}

	private void populateIdToArticleMap() {
	    for(Article a : allArticles) {
	        idToArticleMap.put(a.getId(), a);
	    }
	}

	private void unmarshal() {
		ObjectMapper mapper = new ObjectMapper();
		try {
		    allArticles = mapper.readValue(new File(filePath), new TypeReference<List<Article>>(){});
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public List<Article> getAllArticles() {
		return allArticles;
	}

	public Set<Author> getAllUniqueAuthors() {
	    return new HashSet<Author> (getAuthorsInArticles(allArticles));
	}

    public List<Article> filterArticles(List<Article> articles,
            Predicate<Article> filter) {

        return articles.stream().filter(filter).collect(Collectors.toList());
    }

    public List<Article> getArticlesWithVenue(String venue) {
        return filterArticles(allArticles, a->a.isWithVenue(venue));
    }

    public List<Author> getAuthorsInArticles(List<Article> articles) {
        List<Author> authors = new ArrayList<>();
        for (Article a : articles) {
            authors.addAll(a.getAuthors());
        }
        return authors;
    }

    public List<Integer> getYearsInArticles(List<Article> articles) {
        List<Integer> years = new ArrayList<>();
        for (Article a : articles) {
            years.add(a.getYear());
        }
        return years;
    }

    public List<String> getVenuesInArticles(List<Article> articles) {
        List<String> venues = new ArrayList<>();
        for (Article a : articles) {
            venues.add(a.getVenue());
        }
        return venues;
    }

    public List<Article> getArticlesWithTitle(String title) {
        List<Article> articles = filterArticles(allArticles, a->a.isWithTitle(title));
        return articles;
    }

    public Article getArticleById(String id) {
        if (idToArticleMap.containsKey(id)) {
            return idToArticleMap.get(id);
        } else {
            return null;
        }
    }

    public List<Article> getArticlesInYear(int year) {
        List<Article> articles = filterArticles(allArticles, a->a.isInYear(year));
        return articles;
    }
}
