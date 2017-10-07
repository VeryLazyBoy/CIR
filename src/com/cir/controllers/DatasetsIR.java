package com.cir.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.cir.models.Alias;
import com.cir.models.Citation;
import com.cir.models.Dataset;
import com.cir.models.Datasets;

public class DatasetsIR {

	Datasets datasets;
	Collection<Dataset> allDatasets = new ArrayList<>();
	Collection<Citation> allCitations = new ArrayList<>();
	Set<Citation> uniqueCitations = new HashSet<>();

	public DatasetsIR() {
		init();
	}

	private void init() {
		datasets = new Datasets();
		allDatasets = datasets.getDatasets();
		allCitations = getAllCitations();
		uniqueCitations = getAllUniqueCitations();
	}

	public int getTotalNumOfDatasets() {
		return allDatasets.size();
	}

	public Collection<Dataset> getAllDatasets() {
		return allDatasets;
	}

	public int getTotalNumOfCitations() {
		return allCitations.size();
	}

	public Collection<Citation> getAllCitations() {
		return datasets.getCitationsInDatasets(allDatasets);
	}

	public Collection<Citation> getCitationsFromConference(String confCode) {
		Collection<Dataset> dsFromConference = datasets.getDatasetsFromConferenceCode(confCode);
		return datasets.getCitationsInDatasets(dsFromConference);
	}

	public String printCitationsBetweenYears(String confCode, String startYear, String endYear) {
		Collection<Citation> citations = new ArrayList<>();
		HashMap<String, Collection<Citation>> citationsByYear = new HashMap<>();

		citations = getCitationsBetweenYears(confCode, startYear, endYear);
		citationsByYear = groupCitationsByYear(citations, startYear, endYear);

		return this.printMap_String_CollectionSize(citationsByYear);
	}

	private HashMap<String, Collection<Citation>> groupCitationsByYear(Collection<Citation> citations, String startYear,
			String endYear) {

		HashMap<String, Collection<Citation>> results = new HashMap<>();
		for (Citation c : citations) {
			String date = Short.toString(c.getDate());
			if (results.containsKey(date)) {
				results.get(date).add(c);
			} else {
				Collection<Citation> citationsForThisYear = new ArrayList<>();
				citationsForThisYear.add(c);
				results.put(date, citationsForThisYear);
			}
		}
		return results;
	}

	private Collection<Citation> getCitationsBetweenYears(String confCode, String startYear, String endYear) {
		Collection<Citation> citations = getCitationsFromConference(confCode);
		Collection<Citation> results = new ArrayList<>();
		for (Citation c : citations) {
			short startDate = Short.valueOf(startYear);
			short endDate = Short.valueOf(endYear);
			short date = c.getDate();

			if (date >= startDate && date <= endDate) {
				results.add(c);
			}
		}
		return results;
	}

	private Collection<Citation> getCitationsByYear(String confCode, String year) {
		Collection<Citation> citations = getCitationsFromConference(confCode);
		Collection<Citation> results = new ArrayList<>();
		for (Citation c : citations) {
			short inputYear = Short.valueOf(year);
			short date = c.getDate();

			if (date == inputYear) {
				results.add(c);
			}
		}
		return results;
	}

	public String printCitationsWhoseTitlesContain(ArrayList<Alias> conferenceNames, String confCode) {
		Collection<Citation> citations = getCitationsFromConference(confCode);
		HashMap<String, Collection<Citation>> matchingCitations = getCitationsWhoseTitlesContain(conferenceNames,
				citations);
		return printMap_String_CollectionSize(matchingCitations);
	}

	private String printMap_String_CollectionSize(HashMap<String, Collection<Citation>> matchingCitations) {
		List<String> printList = new ArrayList<String>();

		Iterator<Entry<String, Collection<Citation>>> it = matchingCitations.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry<String, Collection<Citation>> pair = (HashMap.Entry<String, Collection<Citation>>) it.next();
			printList.add(pair.getKey() + " " + pair.getValue().size() + "\n");
			it.remove(); // avoids a ConcurrentModificationException
		}

		Collections.sort(printList);

		String print = "";
		for (String s : printList) {
			print += s;
		}
		return print;
	}

	private HashMap<String, Collection<Citation>> getCitationsWhoseTitlesContain(ArrayList<Alias> conferenceNames,
			Collection<Citation> citations) {
		HashMap<String, Collection<Citation>> results = new HashMap<String, Collection<Citation>>();
		for (Citation c : citations) {
			if (c.getBooktitle() != null) {
				for (Alias alias : conferenceNames) {
					String fullname = alias.getFullName().toUpperCase();
					String shortname = alias.getShortName().toUpperCase();
					// If book title matches short or long form of the name, add to hashmap
					if (c.getBooktitle().toUpperCase().contains(fullname)
							|| c.getBooktitle().toUpperCase().contains(shortname)) {
						// if not added before, create entry
						if (!results.containsKey(alias.getShortName())) {
							Collection<Citation> citationsForThisName = new ArrayList<>();
							citationsForThisName.add(c);
							results.put(alias.getShortName(), citationsForThisName);
						} else {
							results.get(alias.getShortName()).add(c);
						}
					}
				}
			}
		}
		return results;
	}

	public Set<Citation> getAllUniqueCitations() {
		Set<Citation> uniqueCitations = new HashSet<>(allCitations);
		return uniqueCitations;
	}

	public int getTotalNumOfUniqueCitations() {
	    return uniqueCitations.size();
	}

	public int getTotalNumOfUniqueAuthorsInCitation() {
	    Comparator<String> comp = Comparator.comparingInt(String::hashCode);
	    List<String> authors = uniqueCitations.stream()
	                                          .flatMap(c->c.getAllAuthorIdentifiers().stream())
	                                          .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(comp)), ArrayList::new));
	    BufferedWriter writer;
	    Collections.sort(authors);
        try {
            writer = new BufferedWriter(new FileWriter("123.txt"));
            for(String s: authors) {
                writer.write(s);
                writer.write("\n");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    return authors.size();
	}

	}

	public String printCitationsWithAuthorBetweenYears(ArrayList<Alias> authorNames, String startYear, String endYear) {
		Collection<Citation> citationsBetweenYears = this.getCitationsBetweenYears(startYear, endYear);
		HashMap<String, Collection<Citation>> matchingCitations = getCitationsByAuthorsByYear(citationsBetweenYears,
				authorNames);
		return this.printMap_String_CollectionSize(matchingCitations);
	}

	private HashMap<String, Collection<Citation>> getCitationsByAuthorsByYear(Collection<Citation> citations,
			ArrayList<Alias> authorNames) {
		HashMap<String, Collection<Citation>> results = new HashMap<String, Collection<Citation>>();

		for (Citation c : citations) {
			String date = Short.toString(c.getDate());
			if (c.getAuthors() != null) {
				for (String auth : c.getAuthors().getAuthor()) {
					for (Alias alias : authorNames) {
						String fullname = alias.getFullName().toUpperCase();
						String shortname = alias.getShortName().toUpperCase();
						if (auth.toUpperCase().contains(fullname) || auth.toUpperCase().contains(shortname)) {

							if (!results.containsKey(date)) {
								Collection<Citation> citationsForThisYear = new ArrayList<>();
								citationsForThisYear.add(c);
								results.put(date, citationsForThisYear);
							} else {
								results.get(date).add(c);
							}
						}
					}
				}
			}
		}
		return results;
	}

	private Collection<Citation> getCitationsBetweenYears(String startYear, String endYear) {
		Collection<Citation> citations = this.allCitations;
		Collection<Citation> results = new ArrayList<>();
		for (Citation c : citations) {
			short startDate = Short.valueOf(startYear);
			short endDate = Short.valueOf(endYear);
			short date = c.getDate();

			if (date >= startDate && date <= endDate) {
				results.add(c);
			}
		}
		return results;
	}

	public String printCitationsBetweenYears(ArrayList<String> confCodes, String startYear, String endYear) {
		String result = "";
		for (String confCode : confCodes) {
			Collection<Citation> confCitations = this.getCitationsBetweenYears(confCode, startYear, endYear);
			HashMap<String, Collection<Citation>> map = this.groupCitationsByYear(confCitations, startYear, endYear);
			result += printMap_perConference(confCode, map);
		}
		return result;
	}

	private String printMap_perConference(String confCode, HashMap<String, Collection<Citation>> map) {
		List<String> printList = new ArrayList<String>();

		Iterator<Entry<String, Collection<Citation>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry<String, Collection<Citation>> pair = (HashMap.Entry<String, Collection<Citation>>) it.next();
			printList.add(confCode + " " + pair.getKey() + " " + pair.getValue().size() + "\n");
			it.remove(); // avoids a ConcurrentModificationException
		}

		Collections.sort(printList);

		String result = "";
		for (String s : printList) {
			result += s;
		}
		return result;
	}

	public String printCitationsWhoseTitlesContain(ArrayList<Alias> conferenceNames, ArrayList<String> confCodes) {
		String result = "";
		for (String confCode : confCodes) {
			Collection<Citation> citations = getCitationsFromConference(confCode);
			HashMap<String, Collection<Citation>> matchingCitations = getCitationsWhoseTitlesContain(conferenceNames,
					citations);
			result += printMap_perConference(confCode, matchingCitations);
		}
		
		return result;
	}

}
