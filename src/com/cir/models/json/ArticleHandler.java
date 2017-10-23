package com.cir.models.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArticleHandler {

	List<Article> allArticles = new ArrayList<>();

	private File file = new File("resources/json/dummy.json");
//	private File file = new File("resources/json/dataset.json");

	public ArticleHandler() {
		unmarshal();
		// DEBUGGING
		// this.getDatasetsFromConferenceName("D");
		// this.getDatasetsFromConferenceYear("14");
		// this.getDatasetsFromConferenceCode("D14");
	}

	private void unmarshal() {
		ObjectMapper mapper = new ObjectMapper();
		try {
            allArticles = mapper.readValue(file, new TypeReference<List<Article>>(){});
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

    public List<Article> getArticlesWithTitle(String title) {
        List<Article> articles = filterArticles(allArticles, a->a.isWithTitle(title));
        return articles;
    }

    public Article getArticleById(String id) {
        List<Article> articles = filterArticles(allArticles, a->a.isWithId(id));
        if (articles.isEmpty()) {
            return null;
        } else if (articles.size() > 1){
            throw new RuntimeException("Id is corrupted!");
        } else {
            return articles.get(0);
        }
    }
//	public Collection<ArticleToSend> getDatasetsFromConferenceName(String conferenceName) {
//		File confDirectory = new File(directory.getPath() + "/" + conferenceName.trim().toUpperCase());
//		return unmarshalDatasets(confDirectory);
//	}
//
//	public Collection<ArticleToSend> getDatasetsFromConferenceYear(String year) {
//		Collection<File> allDirectories = listDirectoryTree(directory);
//		Collection<File> directoriesFromYear = new ArrayList<>();
//		for (File dir : allDirectories) {
//			String dirYear = dir.getName().substring(1);
//			if (year.equalsIgnoreCase(dirYear)) {
//				directoriesFromYear.add(dir);
//			}
//		}
//
//		Collection<ArticleToSend> datasetsFromYear = new ArrayList<>();
//		for (File dirYear : directoriesFromYear) {
//			datasetsFromYear.addAll(this.unmarshalDatasets(dirYear));
//		}
//		return datasetsFromYear;
//	}
//
//
//
//	public Collection<Citation> getCitationsInDatasets(Collection<ArticleToSend> datasets) {
//		Collection<Citation> citations = new ArrayList<>();
//		for (ArticleToSend ds : datasets) {
//			List<Algorithm> algos = ds.getAlgos().getAlgorithm();
//			for (Algorithm algo : algos) {
//				CitationList citLs = algo.getCitationList();
//				if (citLs != null && !citLs.getCitation().isEmpty()) {
//					citations.addAll(citLs.getCitation());
//				}
//			}
//		}
//		return citations;
//	}
}
