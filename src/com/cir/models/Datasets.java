package com.cir.models;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Datasets {

	Collection<Dataset> datasets = new ArrayList<Dataset>();

	private File DIRECTORY = new File("resources/datasets");

	public Datasets() {
		populate();
	}

	private void populate() {
		// Loop through the datasets and add the AlgorithmsType to the dataset
		Collection<File> files = listXMLFileTree(DIRECTORY);
		int counter = 0;
		for (File f : files) {
			Dataset ds = unmarshal(f);
			datasets.add(ds);
			counter++;
			System.out.println("Populating Dataset: " + counter + "/" + files.size());
		}
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
		return datasets;
	}

	public Collection<File> listXMLFileTree(File dir) {
		Collection<File> fileTree = new ArrayList<File>();

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
}
