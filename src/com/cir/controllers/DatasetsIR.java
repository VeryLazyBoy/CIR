package com.cir.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
			if (i < citationsByYear.size() - 1) {
				print += yearToPrint + " " + numOfCitations + ", ";
			} else {
				print += yearToPrint + " " + numOfCitations;
			}
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

}
