package com.cir.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cir.models.*;

public class DatasetsIR {

	Datasets datasets;
	Collection<Dataset> datasetList = new ArrayList<Dataset>();

	public DatasetsIR() {
		init();
	}

	private void init() {
		datasets = new Datasets();
		datasetList = datasets.getDatasets();
	}

	public int getTotalNumOfDocs() {
		return datasetList.size();
	}

	public int getTotalNumOfCitations() {
		int totalCitations = 0;
		for (Dataset ds : datasetList) {
			List<Algorithm> algos = ds.getAlgos().getAlgorithms();
			for (Algorithm algo : algos) {
				CitationList citLs = algo.getCitationList();
				if (citLs != null && citLs.getCitations().size() > 0) {
					totalCitations += citLs.getCitations().size();
				}
			}
		}
		return totalCitations;

	}
}
