package com.cir.controllers;

public class Logic {

	public static void main(String[] args) {
		DatasetsIR dIR = new DatasetsIR();
		
		System.out.println("Q1: How many documents are there in all datasets put together? " + dIR.getTotalNumOfDatasets());
		System.out.println("Q2: How many citations are there in all datasets put together? " + dIR.getTotalNumOfCitations());
		System.out.println("Q3: How many unique citations are there in all datasets put together? ");
		System.out.println("Q4: How many author are mentioned in the citations in all datasets put together?");
		System.out.println("Q5: What is the range of the years of the cited documents in all datasets put together?");
		System.out.println("Q6: For the conference D12 give number of cited documents published in each of the years 2000 to 2015.");
		System.out.println("Q7: Repeat the above step for conferences ‘EMNLP’ and ‘CoNLL’ (instead of years) for the con-ference D13.");
		System.out.println("Q8: For an author ‘Yoshua Bengio’ (also check for Y. Bengio) the number of times he is cited for his work authored in each of the years 2000 to 2015.");
		System.out.println("Q9: For the conference J14,W14 find number of cited documents published in each of the years from 2010 to 2015.");
		System.out.println("Q10: Repeat the above step for conference ‘NAACL’ for conference Q14,D14");


	}

}
