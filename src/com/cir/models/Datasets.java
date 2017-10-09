package com.cir.models;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.cir.models.citation.Citation;
import com.cir.models.citation.CitationList;
import com.cir.utility.ArrayUtility;
import com.cir.utility.FileUtility;

//TODO good architecture
//TODO more extendable api or architecture to deal with, besides group by and select only one type
public class Datasets {
    private static Datasets instance;
    private Map<String, Collection<Dataset>> confToDatasetsMap;
    private Collection<Dataset> allDatasets;
    private Collection<Citation> allCitations;
    private Set<Citation> uniqueCitations;

//    private File directory = new File("resources/dummy");
    private File directory = new File("resources/datasets");

    private Datasets() {
        populateMap();
        populateAllDatasets();
        populateAllCitations();
        populateUniqueCitations();
    }

    public static Datasets getDatasetsInstance() {
        if (instance == null) {
            instance = new Datasets();
        }
        return instance;
    }

    private void populateMap() {
        // Loop through the datasets and add the AlgorithmsType to the dataset
        confToDatasetsMap = unmarshalAll(directory);
    }

    private void populateAllDatasets() {
        allDatasets = confToDatasetsMap.values()
                                       .stream()
                                       .flatMap(Collection::stream)
                                       .collect(Collectors.toList());
    }

    private void populateAllCitations() {
        allCitations = getCitationsInDatasets(allDatasets);
    }

    private void populateUniqueCitations() {
        uniqueCitations = new HashSet<>(allCitations);
    }

    private Map<String, Collection<Dataset>> unmarshalAll(File dir) {
        HashMap<String, Collection<Dataset>> results = new HashMap<>();
        ArrayList<File> allDirs = FileUtility.getAllDirsContainingFiles(dir);
        for (File d : allDirs) {
            results.put(d.getName().toUpperCase(), unmarshalDatasetsInDirectory(d));
        }
        return results;
    }

    private Collection<Dataset> unmarshalDatasetsInDirectory(File dir) {
        Collection<Dataset> resultingDatasets = new ArrayList<>();
        Collection<File> files = FileUtility.listXMLFileTree(dir);
        for (File f : files) {
            Dataset ds = unmarshalFile(f);
            resultingDatasets.add(ds);
        }
        return resultingDatasets;
    }

    private Dataset unmarshalFile(File file) {
        JAXBContext jaxbContext;
        Dataset ds = null;
        try {
            jaxbContext = JAXBContext.newInstance(Algorithms.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Algorithms algos = (Algorithms) jaxbUnmarshaller.unmarshal(file);
            ds = new Dataset(algos);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return ds;
    }

    public Collection<Dataset> getAllDatasets() {
        return allDatasets;
    }

    public int getTotalNumOfDatasets() {
        return allDatasets.size();
    }


    private Collection<Dataset> filterDatasets(String keyFilter) {
        Collection<String> targetConfCodes = confToDatasetsMap.keySet().stream()
                .filter(k -> k.contains(keyFilter.toUpperCase())).collect(Collectors.toList());
        Collection<Dataset> results = new ArrayList<Dataset>();
        for (String confCode : targetConfCodes) {
            results.addAll(confToDatasetsMap.get(confCode));
        }
        return results;
    }

    public Collection<Dataset> getDatasetsFromConferenceName(String conferenceName) {
        return filterDatasets(conferenceName);
    }

    public Collection<Dataset> getDatasetsFromConferenceYear(String year) {
        return filterDatasets(year);
    }

    public Collection<Dataset> getDatasetsFromConferenceCode(String conferenceCode) {
        if (confToDatasetsMap.containsKey(conferenceCode)) {
            return confToDatasetsMap.get(conferenceCode.toUpperCase());
        } else {
            return new ArrayList<Dataset>();
        }
        
    }

    public Collection<Citation> getAllCitations() {
        return allCitations;
    }

    public int getTotalNumOfCitations() {
        return allCitations.size();
    }

    public Set<Citation> getAllUniqueCitations() {
        return uniqueCitations;
    }

    public List<Citation> getCitationsSortedByYear() {
        Comparator<Citation> comp = Comparator.comparingInt(Citation::getDate);
        List<Citation> result = allCitations.stream().filter(Citation::hasDate).sorted(comp)
                .collect(Collectors.toList());
        return result;
    }

    public Collection<Citation> getCitationsInDatasets(Collection<Dataset> datasets) {
        Collection<Citation> citations = new ArrayList<>();
        for (Dataset ds : datasets) {
            List<Algorithm> algos = ds.getAlgos().getAlgorithm();
            for (Algorithm algo : algos) {
                CitationList citLs = algo.getCitationList();
                if (citLs != null && !citLs.getCitation().isEmpty()) {
                    citations.addAll(citLs.getCitation());
                }
            }
        }
        return citations;
    }

    public Collection<Citation> getCitationsFromConference(String confCode) {
        Collection<Dataset> dsFromConference = getDatasetsFromConferenceCode(confCode);
        return getCitationsInDatasets(dsFromConference);
    }

    public Collection<Citation> filterCitations(Collection<Citation> citations,
            Predicate<Citation> filter) {

        return citations.stream().filter(filter).collect(Collectors.toList());
    }

    public Collection<Citation> getCitationsBetweenYears(String startYear, String endYear) {
        return filterCitations(allCitations, c->c.isWrittenBetweenYear(startYear, endYear));
    }

    public Collection<Citation> getCitationsBetweenYearsFromConf(String confCode, String startYear, String endYear) {
        Collection<Citation> citations = getCitationsFromConference(confCode);
        return filterCitations(citations, c->c.isWrittenBetweenYear(startYear, endYear));
    }

    public Collection<Citation> getCitationsByYearFromConf(String confCode, String year) {
        Collection<Citation> citations = getCitationsFromConference(confCode);
        return filterCitations(citations, c->c.isDateEqual(year));
    }

    public Collection<Citation> getCitationsWithAuthors(Collection<Alias> allAuthors) {
        return filterCitations(allCitations, c->c.isWrittenByAnyAuthor(allAuthors));
    }

    private void padMapWithAllKeys(HashMap<String, Collection<Citation>> mapToPad, String... keys) {
        for (String key : keys) {
            if (!mapToPad.containsKey(key)) {
                Collection<Citation> citations = new ArrayList<>();
                mapToPad.put(key, citations);
            }
        }
    }

    public Map<String, Collection<Citation>> groupCitations(Collection<Citation> citations,
            Map<String, Predicate<Citation>> groupBy) {

        HashMap<String, Collection<Citation>> results = new HashMap<String, Collection<Citation>>();
        String[] keys = groupBy.keySet().toArray(new String[0]);
        padMapWithAllKeys(results, keys);

        for (Citation c : citations) {
            for (String key : keys) {
                if (groupBy.get(key).test(c)) {
                    results.get(key).add(c);
                }
            }
        }

        return results;
    }

    public Map<String, Collection<Citation>> groupCitationsByYear(Collection<Citation> citations,
            String startYear, String endYear) {

        HashMap<String, Predicate<Citation>> groupByYear = new HashMap<>();
        String[] yearArray = ArrayUtility.getYearArray(startYear, endYear);
        for (String year : yearArray) {
            groupByYear.put(year, c->c.isDateEqual(year));
        }

        return groupCitations(citations, groupByYear);
    }

    public Map<String, Collection<Citation>> groupCitationsByTitle(Collection<Citation> citations,
            Collection<Alias> conferenceNames) {

        HashMap<String, Predicate<Citation>> groupByTitle = new HashMap<>();
        for (Alias alias : conferenceNames) {
            groupByTitle.put(alias.getShortName(), c->c.isBookTitleContainingIgnoreCase(alias));
        }

        return groupCitations(citations, groupByTitle);
    }

    public Map<String, Collection<Citation>> groupCitationsFromConfByTitle(String confCode,
            Collection<Alias> conferenceNames) {

        Collection<Citation> citations = getCitationsFromConference(confCode);

        HashMap<String, Predicate<Citation>> groupByTitle = new HashMap<>();
        for (Alias alias : conferenceNames) {
            groupByTitle.put(alias.getShortName(), c->c.isBookTitleContainingIgnoreCase(alias));
        }

        return groupCitations(citations, groupByTitle);
    }

    public Map<String, Collection<Citation>> groupCitationsFromConfByYear(String confCode, String startYear,
            String endYear) {

        Collection<Citation> citations = getCitationsFromConference(confCode);

        HashMap<String, Predicate<Citation>> groupByYear = new HashMap<>();
        String[] yearArray = ArrayUtility.getYearArray(startYear, endYear);
        for (String year : yearArray) {
            groupByYear.put(year, c->c.isDateEqual(year));
        }

        return groupCitations(citations, groupByYear);
    }

    public Map<String, Collection<Citation>> groupCitationsWithAuthorsByYear(Collection<Alias> allAuthors,
            String startYear, String endYear) {

        Collection<Citation> cs = getCitationsWithAuthors(allAuthors);

        HashMap<String, Predicate<Citation>> groupByYear = new HashMap<>();
        String[] yearArray = ArrayUtility.getYearArray(startYear, endYear);
        for (String year : yearArray) {
            groupByYear.put(year, c->c.isDateEqual(year));
        }

        return groupCitations(cs, groupByYear);
    }
}
