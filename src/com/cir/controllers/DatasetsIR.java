package com.cir.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.cir.models.Algorithm;
import com.cir.models.Citation;
import com.cir.models.CitationList;
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
		Collection<Citation> citations = new ArrayList<>();
		for (Dataset ds : allDatasets) {
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
	
	public Set<Citation> getAllUniqueCitations() {
		Set<Citation> uniqueCitations = new HashSet<>(allCitations);
		return uniqueCitations;
	}

	public int getTotalNumOfUniqueCitations() {
	    List<String> dummy = uniqueCitations.stream().map(c->c.getRawString()).collect(Collectors.toList());
	    Collections.sort(dummy);
	    BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("123.txt"));
            for(String s: dummy) {
                writer.write(s);
                writer.write("\n");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    
	    return uniqueCitations.size();
	}
}
