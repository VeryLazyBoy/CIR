package com.cir.models;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Datasets {

	Collection<Dataset> allDatasets = new ArrayList<>();

	private final File directory = new File("resources/datasets/");

	public Datasets() {
		populate();
		// DEBUGGING
		// this.getDatasetsFromConferenceName("D");
		// this.getDatasetsFromConferenceYear("14");
		// this.getDatasetsFromConferenceCode("D14");
	}

	private void populate() {
		// Loop through the datasets and add the AlgorithmsType to the dataset
		allDatasets = unmarshalDatasetsInDirectory(directory);
	}

	private Collection<Dataset> unmarshalDatasetsInDirectory(File dir) {
		Collection<Dataset> resultingDatasets = new ArrayList<>();
		Collection<File> files = listXMLFileTree(dir);

		System.out.println("Unmarshalling from: " + dir.getAbsolutePath());
		int counter = 0;
		for (File f : files) {
			Dataset ds = unmarshal(f);
			resultingDatasets.add(ds);
			counter++;
			System.out.println("Unmarshalling Dataset: " + counter + "/" + files.size());
		}
		System.out.println("Finished unmarshalling from: " + dir.getAbsolutePath());

		return resultingDatasets;
	}

	private Dataset unmarshal(File file) {
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

	public Collection<Dataset> getDatasets() {
		return allDatasets;
	}

	public Collection<Dataset> getDatasetsFromConferenceName(String conferenceName) {
		File confDirectory = new File(directory.getPath() + "/" + conferenceName.trim().toUpperCase());
		return unmarshalDatasetsInDirectory(confDirectory);
	}

	public Collection<Dataset> getDatasetsFromConferenceYear(String year) {
		Collection<File> allDirectories = listDirectoryTree(directory);
		Collection<File> directoriesFromYear = new ArrayList<>();
		for (File dir : allDirectories) {
			String dirYear = dir.getName().substring(1);
			if (year.equalsIgnoreCase(dirYear)) {
				directoriesFromYear.add(dir);
			}
		}

		Collection<Dataset> datasetsFromYear = new ArrayList<>();
		for (File dirYear : directoriesFromYear) {
			datasetsFromYear.addAll(this.unmarshalDatasetsInDirectory(dirYear));
		}
		return datasetsFromYear;
	}

	public Collection<Dataset> getDatasetsFromConferenceCode(String conferenceCode) {
		Collection<File> allDirectories = listDirectoryTree(directory);
		Collection<Dataset> datasetsFromConference = new ArrayList<>();
		for (File dir : allDirectories) {
			if (dir.getName().equalsIgnoreCase(conferenceCode)) {
				datasetsFromConference = this.unmarshalDatasetsInDirectory(dir);
				break;
			}
		}
		return datasetsFromConference;
	}

	public Collection<File> listDirectoryTree(File dir) {
		Collection<File> fileTree = new ArrayList<>();

		if (dir == null || dir.listFiles() == null) {
			return fileTree;
		}

		for (File entry : dir.listFiles()) {
			if (entry.isDirectory()) {
				fileTree.add(entry);
				fileTree.addAll(listDirectoryTree(entry));
			}
		}

		return fileTree;
	}

	public Collection<File> listXMLFileTree(File dir) {
		Collection<File> fileTree = new ArrayList<>();

		if (dir == null || dir.listFiles() == null) {
			return fileTree;
		}

		for (File entry : dir.listFiles()) {
			if (entry.isFile() && isXmlFile(entry)) {
				fileTree.add(entry);
			} else {
				fileTree.addAll(listXMLFileTree(entry));
			}
		}

		return fileTree;
	}

	private boolean isXmlFile(File entry) {
		String extension = "";
		String fileName = entry.getName();

		int i = fileName.lastIndexOf('.');
		if (i >= 0) {
			extension = fileName.substring(i + 1);
		}

		return extension.equalsIgnoreCase("xml");
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
}
