package com.cir.controllers;

public class Logic {

	public static void main(String[] args) {
		DatasetsIR dIR = new DatasetsIR();
		
		System.out.println("How many documents are there in all datasets put together? " + dIR.getTotalNumOfDatasets());
		System.out.println("How many citations are there in all datasets put together? " + dIR.getTotalNumOfCitations());
//		System.out.println("How many unique citations are there in all datasets put together? " + dIR.getTotalNumOfUnqiueCitations()());

	}

}
