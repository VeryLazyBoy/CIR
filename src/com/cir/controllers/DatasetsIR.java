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
		Collection<Citation> citations = new ArrayList<>();
		for (Dataset ds : allDatasets) {
			List<Algorithm> algos = ds.getAlgos().getAlgorithms();
			for (Algorithm algo : algos) {
				CitationList citLs = algo.getCitationList();
				if (citLs != null && !citLs.getCitations().isEmpty()) {
					citations.addAll(citLs.getCitations());
				}
			}
		}
		return citations;
	}
	
	public Collection<Citation> getAllUniqueCitations(){
		Set<Citation> uniqueCitations = new HashSet<>();
		
		return allCitations;
		
	}
}
