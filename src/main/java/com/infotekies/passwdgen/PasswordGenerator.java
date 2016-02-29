package com.infotekies.passwdgen;

/*
 * (c) 2011, InfoTekies Corporation
 */

import java.util.ArrayList;
import java.util.Random;

public class PasswordGenerator {

	/**
	 * @param args
	 */
	
	private static final int DEFAULT_NO_PASSWORD_TO_GENRATE = 100 ;
	
	private int minimumPasswordLength = 8 ;
	
	private int maximumPasswordLength = 12 ;
	
	private boolean isExpectedNumberic = true ;
	
	private boolean isExpectedBothCase = true ;
	
	private static final ArrayList<Character> alphaLetters = new ArrayList<Character>() ;

	private static final ArrayList<Character> alphaUpperLetters = new ArrayList<Character>() ;

	private static final ArrayList<Character> numericLetters = new ArrayList<Character>() ;
	
	
	static {
		for(int x = 'a' ; x <= 'z' ; x++) {
			char v = (char)x ;
			// System.err.println(v) ;
			alphaLetters.add(Character.toLowerCase(v)) ;
			alphaUpperLetters.add(Character.toUpperCase(v)) ;
		}
		for(int i = 0 ; i < 10 ; i++) {
			numericLetters.add(Character.forDigit(i,10)) ;
 		}
	}
	

	
	public static void main(String[] args) {
		
		int noOfPasswordToGenerate = DEFAULT_NO_PASSWORD_TO_GENRATE ;
		
		if (args.length > 0) {
			noOfPasswordToGenerate = Integer.parseInt(args[0]) ;
		}
		
		PasswordGenerator pg = new PasswordGenerator() ;
		
		for(int i = 0 ; i < noOfPasswordToGenerate; i++) {
			System.out.println(pg.generatorPassword()) ;
		}
		
	}
	
	
	private int getPasswordLength() {
		int ret = 0;
		
		if (minimumPasswordLength == maximumPasswordLength) {
			ret = minimumPasswordLength ;
		}
		else {
			
			int diff = Math.abs(maximumPasswordLength - minimumPasswordLength) + 1 ;
			ret = minimumPasswordLength + new Random().nextInt(diff) ;
		}
		return (ret) ;
	}
	
	
	public String generatorPassword() {
	
		String password = null ;
		
		ArrayList<Character> all = new ArrayList<Character>() ;
		
		all.addAll(alphaLetters) ;
		all.addAll(alphaUpperLetters) ;
		all.addAll(numericLetters) ;
 				
		int len = getPasswordLength() ;
		
		Random random = new Random() ;
		
		int setSz = all.size();
		
		do
		{
			StringBuilder sb = new StringBuilder();
			
			for(int i = 0 ; i < len ; i++) {
				int index = random.nextInt(setSz) ;
				Character c = all.get(index) ;
				while ((i == 0) && Character.isDigit(c)) {
					index = random.nextInt(setSz) ;
					c = all.get(index) ;
				}
				sb.append(all.get(index)) ;
			}
			password = sb.toString() ;
		} while (! isValidPassword(password)) ;
		
		
		return password ;
		
	}
	
	private boolean isValidPassword(String pass) {
		boolean ret = true ;
		
		if (isExpectedNumberic || isExpectedBothCase) {
			boolean lowerCaseFound = false ;
			boolean digitFound = false ;
			boolean upperCaseFound = false ;
			for(char c : pass.toCharArray()) {
				if (!digitFound && Character.isDigit(c)) {
					digitFound = true ;
				}
				else if (!lowerCaseFound && Character.isLowerCase(c)) {
					lowerCaseFound = true ;
				}
				else if (!upperCaseFound && Character.isUpperCase(c) ) {
					upperCaseFound = true ;
				}
			}
			
			if (isExpectedNumberic && !digitFound) {
				// System.err.println("Password failed to have digit:" + pass) ;
				ret = false  ;
			}
			
			if (isExpectedBothCase && (!lowerCaseFound || !upperCaseFound)) {
				// System.err.println("Password failed to have mixed case:" + pass) ;
				ret = false ;
			}
		}
		
		return ret ;
	}

}
