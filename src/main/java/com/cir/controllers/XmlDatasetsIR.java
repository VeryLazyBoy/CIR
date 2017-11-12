package com.cir.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.cir.models.Alias;
import com.cir.models.xml.Datasets;
import com.cir.models.xml.citation.Citation;

public class XmlDatasetsIR {

    Datasets datasets;

    public XmlDatasetsIR() {
        datasets = Datasets.getDatasetsInstance();
    }

    public int getTotalNumOfDatasets() {
        return datasets.getTotalNumOfDatasets();
    }

    public int getTotalNumOfCitations() {
        return datasets.getTotalNumOfCitations();
    }

    public String groupCitationFromConfByYear(String confCode, String startYear, String endYear) {
        Collection<Citation> citations = datasets.getCitationsFromConference(confCode);
        Map<String, Collection<Citation>> citationsByYear = datasets.groupCitationsByYear(citations,
                                                                                          startYear,
                                                                                          endYear);
        return convertMapToSortedString("", citationsByYear);
    }

    public String getCitationNumWhoseTitlesContain(ArrayList<Alias> conferenceNames, String confCode) {
        Map<String, Collection<Citation>> matchingCitations = datasets.groupCitationsFromConfByTitle(confCode,
                                                                                                     conferenceNames);
        return convertMapToSortedString("", matchingCitations);
    }

    private String convertMapToSortedString(String prefix, Map<String, Collection<Citation>> mapToConvert) {
        List<String> printList = new ArrayList<String>();
        Iterator<Entry<String, Collection<Citation>>> it = mapToConvert.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry<String, Collection<Citation>> pair = (HashMap.Entry<String, Collection<Citation>>) it.next();
            printList.add(prefix + " " + pair.getKey() + " " + pair.getValue().size());
            //it.remove(); // avoids a ConcurrentModificationException
        }
        return printList.stream().sorted().collect(Collectors.joining(", "));
    }

    public Set<Citation> getAllUniqueCitations() {
        return datasets.getAllUniqueCitations();
    }

    public int getTotalNumOfUniqueCitations() {
        return datasets.getAllUniqueCitations().size();
    }

    public int getTotalNumOfUniqueAuthorsInCitation() {
        Comparator<String> stringComparator = Comparator.comparingInt(String::hashCode);
        List<String> authors = datasets.getAllUniqueCitations()
                                       .stream()
                                       .flatMap(c->c.getAllAuthorIdentifiers().stream())
                                       .collect(Collectors.toCollection(() -> new TreeSet<>(stringComparator)))
                                       .stream()
                                       .collect(Collectors.toList());
        BufferedWriter writer;
        Collections.sort(authors);
        try {
            writer = new BufferedWriter(new FileWriter("123.txt"));
            for (String s: authors) {
                writer.write(s);
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authors.size();
    }

    public int getEarliestCitationYear() {
        return datasets.getCitationsSortedByYear().get(0).getDate();
    }

    public int getLatestCitationYear() {
        List<Citation> sortedCL = datasets.getCitationsSortedByYear();
        return sortedCL.get(sortedCL.size() - 1).getDate();
    }

    public int getCitationYearRange() {
        return getLatestCitationYear() - getEarliestCitationYear();
    }

    public String groupCitationsWithAuthorsByYear(ArrayList<Alias> authorNames, String startYear, String endYear) {
        Map<String, Collection<Citation>> matchingCitations = datasets.groupCitationsWithAuthorsByYear(authorNames,
                                                                                                       startYear,
                                                                                                       endYear);
        return this.convertMapToSortedString("", matchingCitations);
    }

    public String groupCitationsFromConfsByYear(ArrayList<String> confCodes, String startYear, String endYear) {
        String result = "";
        Collections.sort(confCodes);
        for (String confCode : confCodes) {
            Collection<Citation> confCitations = datasets.getCitationsFromConference(confCode);
            Map<String, Collection<Citation>> map = datasets.groupCitationsByYear(confCitations, startYear, endYear);
            if (result.isEmpty()) {
                result = convertMapToSortedString(confCode, map);
            } else {
                result = result + ", " + convertMapToSortedString(confCode, map);
            }
        }
        return result;
    }

    public String groupCitationsFromConfsByTitle(ArrayList<Alias> conferenceNames, ArrayList<String> confCodes) {
        String result = "";
        Map<String, Collection<Citation>> matchingCitations;
        for (String confCode : confCodes) {
            matchingCitations = datasets.groupCitationsFromConfByTitle(confCode, conferenceNames);
            if (result.isEmpty()) {
                result = convertMapToSortedString(confCode, matchingCitations);
            } else {
                result = result + ", " +  convertMapToSortedString(confCode, matchingCitations);
            }
        }
        return result;
    }
}
