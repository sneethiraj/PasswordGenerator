package com.infotekies.passwdgen;

/*
 * (c) 2011, InfoTekies Corporation
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

public class PasswordGenerator {
	
	public static final String CONFIG_FILENAME = "passwordgen.properties" ;
	
	public static final String CONFIG_PREFIX = "generate.password." ;
	
	public static final String GEN_PASSWORD_COUNT_CONFIG_NAME = "count" ;
	public static final String MIN_LEN_CONFIG_NAME = "minLen" ;
	public static final String MAX_LEN_CONFIG_NAME = "maxLen" ;
	public static final String NUMERIC_NEEDED_CONFIG_NAME = "numericNeeded" ;
	public static final String BOTH_CASE_NEEDED_CONFIG_NAME = "bothCaseNeeded" ;
	
	
	public static final int DEFAULT_GEN_PASSWORD_COUNT = 10 ;
	
	public static final int DEFAULT_GEN_PASSWORD_MIN_LEN =   8 ;
	public static final int DEFAULT_GEN_PASSWORD_MAX_LEN = 12 ;
	public static final boolean DEFAULT_GEN_PASSWORD_NEED_NUMERIC = true ;
	public static final boolean  DEFAULT_GEN_PASSWORD_NEED_BOTH_CASE = true ;
	
	
	private static final ArrayList<Character> alphaLetters = new ArrayList<Character>() ;
	private static final ArrayList<Character> alphaUpperLetters = new ArrayList<Character>() ;
	private static final ArrayList<Character> numericLetters = new ArrayList<Character>() ;

	private Properties prop = new Properties() ;
	
	private  int noOfPasswordToGenerate =  DEFAULT_GEN_PASSWORD_COUNT ;
	private int minimumPasswordLength = DEFAULT_GEN_PASSWORD_MIN_LEN ;
	private int maximumPasswordLength = DEFAULT_GEN_PASSWORD_MAX_LEN ;
	private boolean isExpectedNumberic = DEFAULT_GEN_PASSWORD_NEED_NUMERIC ;
	private boolean isExpectedBothCase = DEFAULT_GEN_PASSWORD_NEED_BOTH_CASE ;

	static {
		for(int x = 'a' ; x <= 'z' ; x++) {
			char v = (char)x ;
			alphaLetters.add(Character.toLowerCase(v)) ;
			alphaUpperLetters.add(Character.toUpperCase(v)) ;
		}
		for(int i = 0 ; i < 10 ; i++) {
			numericLetters.add(Character.forDigit(i,10)) ;
 		}
	}
	
	public void init() {
		String fn = CONFIG_FILENAME ;
		InputStream in = getClass().getResourceAsStream(fn) ;
		if (in == null) {
			fn = "/" + CONFIG_FILENAME ;
			in = getClass().getResourceAsStream(fn) ;
		}
		if (in != null) {
			// System.out.println("FILENAME: [" + fn + "]") ;
			try {
				prop.load(in);
				
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		else {
			// System.err.println("Using default value as file [" + CONFIG_FILENAME + "] is not found in the classpath") ;
		}
		noOfPasswordToGenerate = getIntConfigValue(GEN_PASSWORD_COUNT_CONFIG_NAME, DEFAULT_GEN_PASSWORD_COUNT) ;
		minimumPasswordLength = getIntConfigValue(MIN_LEN_CONFIG_NAME, DEFAULT_GEN_PASSWORD_MIN_LEN) ;
		maximumPasswordLength = getIntConfigValue(MAX_LEN_CONFIG_NAME, DEFAULT_GEN_PASSWORD_MAX_LEN) ;
		isExpectedNumberic =  "TRUE".equalsIgnoreCase(getConfigValue(NUMERIC_NEEDED_CONFIG_NAME,  Boolean.toString(DEFAULT_GEN_PASSWORD_NEED_NUMERIC))) ;
		isExpectedBothCase  =  "TRUE".equalsIgnoreCase(getConfigValue(BOTH_CASE_NEEDED_CONFIG_NAME, Boolean.toString(DEFAULT_GEN_PASSWORD_NEED_BOTH_CASE))) ;
	}
	
	public PasswordGenerator() {
		init() ;
	}

	
	public int getNoOfPasswordToGenerate() {
		return noOfPasswordToGenerate;
	}

	public void setNoOfPasswordToGenerate(int noOfPasswordToGenerate) {
		this.noOfPasswordToGenerate = noOfPasswordToGenerate;
	}

	public int getMinimumPasswordLength() {
		return minimumPasswordLength;
	}

	public void setMinimumPasswordLength(int minimumPasswordLength) {
		this.minimumPasswordLength = minimumPasswordLength;
	}

	public int getMaximumPasswordLength() {
		return maximumPasswordLength;
	}

	public void setMaximumPasswordLength(int maximumPasswordLength) {
		this.maximumPasswordLength = maximumPasswordLength;
	}

	public boolean isExpectedNumberic() {
		return isExpectedNumberic;
	}

	public void setExpectedNumberic(boolean isExpectedNumberic) {
		this.isExpectedNumberic = isExpectedNumberic;
	}

	public boolean isExpectedBothCase() {
		return isExpectedBothCase;
	}

	public void setExpectedBothCase(boolean isExpectedBothCase) {
		this.isExpectedBothCase = isExpectedBothCase;
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
	
	
	private String generatorPassword() {
	
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
	
	public void printGeneratedPasswords() {
		for(int i = 0 ; i < noOfPasswordToGenerate; i++) {
			System.out.println(generatorPassword()) ;
		}
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
	
	public String getConfigValue(String key, String defaultValue) {
		String ret = null ;
		
		ret = System.getProperty(key) ;
		
		if (ret == null) {
			ret = prop.getProperty(key) ;
		}
		
		if (! key.startsWith(CONFIG_PREFIX)) {
			
			String kn =  CONFIG_PREFIX + key ;
			
			ret = System.getProperty(kn) ;
			
			if (ret == null) {
				ret = prop.getProperty(kn) ;
			}	
		}
		
		if (ret == null) {
			ret = defaultValue ;
		}
		
		return ret ;
	}

	public String getConfigValue(String key) {
		return getConfigValue(key,null) ;
	}

	
	public int getIntConfigValue(String key) {
		return getIntConfigValue(key,0) ;
	}
	
	public int getIntConfigValue(String key, int defaultValue) {
		int retVal = defaultValue ;
		String ret = getConfigValue(key) ;
		if (ret != null) {
			retVal = Integer.parseInt(ret) ;
		}
		return retVal ;
	}
	
	public static void main(String[] args) {

		PasswordGenerator pg = new PasswordGenerator() ;
		
		if (args.length > 0) {
			pg.setNoOfPasswordToGenerate(Integer.parseInt(args[0]));
		}
		
		pg.printGeneratedPasswords() ;
		
	}
	


}
