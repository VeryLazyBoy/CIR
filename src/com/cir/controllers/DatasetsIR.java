package com.cir.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.cir.models.*;

public class DatasetsIR {

	Datasets datasets;
	Collection<Dataset> allDatasets = new ArrayList<>();
	Collection<Citation> allCitations = new ArrayList<>();

	public DatasetsIR() {
		init();
	}

	private void init() {
		datasets = new Datasets();
		allDatasets = datasets.getDatasets();
		allCitations = getAllCitations();
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

	public Collection<Citation> getAllUniqueCitations() {
		Set<Citation> uniqueCitations = new HashSet<>();
		return allCitations;
	}

	public Collection<Citation> getCitationsFromConference(String confCode) {
		Collection<Dataset> dsFromConference = datasets.getDatasetsFromConferenceCode(confCode);
		return datasets.getCitationsInDatasets(dsFromConference);
	}

	public String printCitationsBetweenYears(String confCode, String startYear, String endYear) {
		Collection<Citation> citations = new ArrayList<>();
		ArrayList<ArrayList<Citation>> citationsByYear = new ArrayList<>();

		citations = getCitationsBetweenYears(confCode, startYear, endYear);
		citationsByYear = groupCitationsByYear(citations, startYear, endYear);

		String print = "";
		for (int i = 0; i < citationsByYear.size(); i++) {
			int yearToPrint = Short.valueOf(startYear) + i;
			int numOfCitations = citationsByYear.get(i).size();
				print += yearToPrint + " " + numOfCitations + "\n";
		}
		return print;
	}

	private ArrayList<ArrayList<Citation>> groupCitationsByYear(Collection<Citation> citations, String startYear,
			String endYear) {

		ArrayList<ArrayList<Citation>> results = new ArrayList<ArrayList<Citation>>();

		int numOfYears = Short.valueOf(endYear) - Short.valueOf(startYear) + 1;

		// intialize initial lists
		for (int i = 0; i < numOfYears; i++) {
			results.add(new ArrayList<Citation>());
		}

		// add citations into lists according to their year. The indexes serve as the
		// year
		// first year will be index 0, second year index 1 and so on.
		for (Citation c : citations) {
			short date = c.getDate();
			int indexToPush = date - Short.valueOf(startYear);
			results.get(indexToPush).add(c);
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
		HashMap<String, Collection<Citation>> matchingCitations = new HashMap<>();
		matchingCitations = getCitationsWhoseTitlesContain(conferenceNames, citations);
		return printMap_String_CollectionSize(matchingCitations);
	}

	private String printMap_String_CollectionSize(HashMap<String, Collection<Citation>> matchingCitations) {
		String print = "";
		Iterator<Entry<String, Collection<Citation>>> it = matchingCitations.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry<String, Collection<Citation>> pair = (HashMap.Entry<String, Collection<Citation>>) it.next();
			print += pair.getKey() + " " + pair.getValue().size() + "\n";
			it.remove(); // avoids a ConcurrentModificationException
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
						if (!results.containsKey(shortname)) {
							Collection<Citation> citationsForThisName = new ArrayList<>();
							citationsForThisName.add(c);
							results.put(shortname, citationsForThisName);
						} else {
							results.get(shortname).add(c);
						}
					}
				}
			}
		}
		return results;
	}

}
