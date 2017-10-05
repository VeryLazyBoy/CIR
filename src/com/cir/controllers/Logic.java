package com.cir.controllers;

import com.cir.models.Datasets;

public class Logic {

	public static void main(String[] args) {
		DatasetsIR dIR = new DatasetsIR();
		
		System.out.println("How many documents are there in all datasets put together? " + dIR.getTotalNumOfDocs());
		
		System.out.println("How many citations are there in all datasets put together? " + dIR.getTotalNumOfCitations());
	}

}
