//William Diedrick
//CS-467 Machine Learning
// 12/6/17
//Wikipedia Vandalism Detection

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class Bayesian {
	String insertion;
	int wordCount;
	double bayVal;
	double threshold;
	String throwInsr;
	String[] wordArray = new String[30];
	int[] checkArray = new int[30];
	int[] vandalArray = new int[30];
	int[] constrArray = new int[30];
	double[] probArray = new double[30];
	
	public double getBayVal(String insr, double thresh) throws FileNotFoundException {
		insertion = insr;
		threshold = thresh;
		splitWords(insertion);
		bayVal = calcBayVal(wordArray);
		
		if(bayVal<threshold) {
			addVandal();
		}
		else {
			addConstr();
		}
		
		return bayVal;
	}
	
	private void splitWords(String insr) { // splits insertion into String array of words
		wordArray = insr.split("\\s+");
		wordCount = wordArray.length;
	}
	
	private int vandalStatus(String wordCheck, int count) throws FileNotFoundException { // accesses files and compares words with file to see if vandal/constr
		int statusNum = 0;
		int vandalCount = 0;
		int constrCount = 0;
		String temp = "";
		
		Scanner vandalIn = new Scanner(new File("VandalWords.txt"));
		while(vandalIn.hasNextLine()) {
			temp = vandalIn.nextLine();
			if(wordCheck.toLowerCase().equals(temp.toLowerCase())) {
				vandalCount++;
			}
		}
		vandalIn.close();
		
		Scanner constrIn = new Scanner(new File("ConstrWords.txt"));
		while(constrIn.hasNextLine()) {
			temp = constrIn.nextLine();
			if(wordCheck.toLowerCase().equals(temp.toLowerCase())) {
				constrCount++;
			}
		}
		constrIn.close();
		
		vandalArray[count] = vandalCount;
		constrArray[count] = constrCount;
		
		return statusNum;
	}
	
	private double calcBayVal(String[] strArray) throws FileNotFoundException { // constructs bool array w/ vandalStatus() and calculates bayVal

		double tempBayVal = 0;
		
		for(int i = 0; i < wordCount; i++) {
			checkArray[i] = vandalStatus(strArray[i], i);
			
			if(wordArray[i].toLowerCase().equals("is")||wordArray[i].toLowerCase().equals("the")||
				wordArray[i].toLowerCase().equals("and")||wordArray[i].toLowerCase().equals("a")||
				wordArray[i].toLowerCase().equals("at")||wordArray[i].toLowerCase().equals("is")||
				wordArray[i].toLowerCase().equals("an")||wordArray[i].toLowerCase().equals("in")) {
				probArray[i] = 0.5;
			} else {
				if(constrArray[i] == 0 && vandalArray[i] == 0) {
					probArray[i] = .3;
				} else {
					probArray[i] = (double)constrArray[i]/(double)(constrArray[i]+vandalArray[i]);
				}
			}	
		}
		
		for(int i = 0; i < wordCount; i++) {
			tempBayVal += probArray[i];
		}
		tempBayVal = tempBayVal/wordCount;
		
		return tempBayVal;
	}
	
	private void addVandal() { //add wordArray -> VandalWords
		try {
			for(int i = 0; i < wordCount; i++) {
				Files.write(Paths.get("VandalWords.txt"), ("\n" + wordArray[i]).getBytes(), StandardOpenOption.APPEND);
			}
		}catch (IOException e) {}
	}
	
	private void addConstr() { //add wordArray -> ConstrWords
		try {
			for(int i = 0; i < wordCount; i++) {
				Files.write(Paths.get("ConstrWords.txt"), ("\n" + wordArray[i]).getBytes(), StandardOpenOption.APPEND);
			}
		}catch (IOException e) {}
	}
	

}
