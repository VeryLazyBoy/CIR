package com.cir.controllers;

import java.util.ArrayList;

import com.cir.models.Alias;

public class Logic {

	public static void main(String[] args) {
		DatasetsIR dIR = new DatasetsIR();
		
		System.out.println("\nQ1: How many documents are there in all datasets put together?\n" + dIR.getTotalNumOfDatasets());
		System.out.println("\nQ2: How many citations are there in all datasets put together?\n" + dIR.getTotalNumOfCitations());
		System.out.println("\nQ3: How many unique citations are there in all datasets put together?\n");
		System.out.println("\nQ4: How many author are mentioned in the citations in all datasets put together?\n");
		System.out.println("\nQ5: What is the range of the years of the cited documents in all datasets put together?\n");
		System.out.println("\nQ6: For the conference D12 give number of cited documents published in each of the years 2000 to 2015.\n" + dIR.printCitationsBetweenYears("D12", "2000", "2015"));
		ArrayList<Alias> conferenceNames = new ArrayList<Alias>();
		conferenceNames.add(new Alias("Empirical Methods in Natural Language Processing","EMNLP"));
		conferenceNames.add(new Alias("Computational Natural Language Learning", "CoNLL"));
		
		System.out.println("\nQ7: Repeat the above step for conferences ‘EMNLP’ and ‘CoNLL’ (instead of years) for the con-ference D13.\n" + dIR.printCitationsWhoseTitlesContain(conferenceNames, "D13"));
		System.out.println("\nQ8: For an author ‘Yoshua Bengio’ (also check for Y. Bengio) the number of times he is cited for his work authored in each of the years 2000 to 2015.\n");
		System.out.println("\nQ9: For the conference J14,W14 find number of cited documents published in each of the years from 2010 to 2015.\n");
		System.out.println("\nQ10: Repeat the above step for conference ‘NAACL’ for conference Q14,D14\n");


	}

}
