/*
 * Copyright (c) 2004 InfoTekies Corp., All rights reserved.
 *
 * This software is the confidential and proprietary information of InfoTekies.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with InfoTekies.
 */

package com.infotekies.passwdgen;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PasswordGenerator implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(PasswordGenerator.class);

  public static final String CONFIG_FILENAME = "passwordgen.properties";

  public static final String CONFIG_PREFIX = "generate.password.";

  public static final String GEN_PASSWORD_COUNT_CONFIG_NAME = "count";
  public static final String MIN_LEN_CONFIG_NAME = "minLen";
  public static final String MAX_LEN_CONFIG_NAME = "maxLen";
  public static final String NUMERIC_NEEDED_CONFIG_NAME = "numericNeeded";
  public static final String BOTH_CASE_NEEDED_CONFIG_NAME = "bothCaseNeeded";
  public static final String SPECIAL_CHAR_NEEDED_CONFIG_NAME = "includeSpecialChars";
  public static final String SPECIAL_CHAR_LIST_CONFIG_NAME = "acceptedSpecialChars";
  public static final String SHOW_LINE_NUMBER_CONFIG_NAME = "showLineNumber";

  public static final int DEFAULT_GEN_PASSWORD_COUNT = 10;

  public static final int DEFAULT_GEN_PASSWORD_MIN_LEN = 8;
  public static final int DEFAULT_GEN_PASSWORD_MAX_LEN = 12;
  public static final boolean DEFAULT_GEN_PASSWORD_NEED_NUMERIC = true;
  public static final boolean DEFAULT_GEN_PASSWORD_NEED_BOTH_CASE = true;
  public static final boolean DEFAULT_GEN_PASSWORD_NEED_SPECIAL_CHARS = false;
  public static final boolean DEFAULT_SHOW_LINE_NUMBER_CONFIG_NAME = false;

  public static final String DEFAULT_GEN_PASSWORD_SPECIAL_CHAR_LIST = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

  private static final ArrayList<Character> alphaLetters = new ArrayList<Character>();
  private static final ArrayList<Character> alphaUpperLetters = new ArrayList<Character>();
  private static final ArrayList<Character> numericLetters = new ArrayList<Character>();
  private static final int MAX_NUMBER_OF_SEED_GENERATION = 20;

  static {
    for (int x = 'a'; x <= 'z'; x++) {
      char v = (char) x;
      alphaLetters.add(Character.toLowerCase(v));
      alphaUpperLetters.add(Character.toUpperCase(v));
    }
    for (int i = 0; i < 10; i++) {
      numericLetters.add(Character.forDigit(i, 10));
    }
  }

  private String profileName = null;
  private final Properties prop = new Properties();
  private int noOfPasswordToGenerate = DEFAULT_GEN_PASSWORD_COUNT;
  private int minimumPasswordLength = DEFAULT_GEN_PASSWORD_MIN_LEN;
  private int maximumPasswordLength = DEFAULT_GEN_PASSWORD_MAX_LEN;
  private boolean isExpectedNumberic = DEFAULT_GEN_PASSWORD_NEED_NUMERIC;
  private boolean isExpectedBothCase = DEFAULT_GEN_PASSWORD_NEED_BOTH_CASE;
  private boolean isSpecialCharNeeded = DEFAULT_GEN_PASSWORD_NEED_SPECIAL_CHARS;
  private String specialCharList = DEFAULT_GEN_PASSWORD_SPECIAL_CHAR_LIST;
  private boolean isShowLineNumber = DEFAULT_SHOW_LINE_NUMBER_CONFIG_NAME;
  private Random random = null;
  private ArrayList<Character> all = null;
  private int setSz = 0;

  @Override
  public void run(String... args) throws Exception {

    String loaderMain = System.getProperty("loader.main");

    if (loaderMain != null && loaderMain.equals(PasswordGenerator.class.getName())) {

      PasswordGenerator pg = null;

      if (args.length == 0) {
        pg = new PasswordGenerator();
      } else {
        pg = new PasswordGenerator(args[0]);
      }

      if (args.length > 0) {
        for (String arg : args) {
          if (arg.startsWith("--")) {
            if (arg.startsWith("--special=")) {
              pg.setSpecialCharNeeded(true);
              int start = "--special=".length();
              int end = arg.length();
              if (end > start) {
                pg.setSpecialCharList(arg.substring(start, end));
              } else {
                pg.setSpecialCharList(DEFAULT_GEN_PASSWORD_SPECIAL_CHAR_LIST);
              }
            }
          }
        }
      }

      pg.init();
      pg.printGeneratedPasswords();
    }

  }

  private void preInit() {
    String fn = CONFIG_FILENAME;
    InputStream in = getClass().getResourceAsStream(fn);
    if (in == null) {
      fn = "/" + CONFIG_FILENAME;
      in = getClass().getResourceAsStream(fn);
    }
    if (in != null) {
      // System.out.println("FILENAME: [" + fn + "]") ;
      try {
        prop.load(in);

      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    } else {
      // System.err.println("Using default value as file [" + CONFIG_FILENAME + "] is not found in the classpath") ;
    }

    noOfPasswordToGenerate = getIntConfigValue(GEN_PASSWORD_COUNT_CONFIG_NAME, DEFAULT_GEN_PASSWORD_COUNT);
    minimumPasswordLength = getIntConfigValue(MIN_LEN_CONFIG_NAME, DEFAULT_GEN_PASSWORD_MIN_LEN);
    maximumPasswordLength = getIntConfigValue(MAX_LEN_CONFIG_NAME, DEFAULT_GEN_PASSWORD_MAX_LEN);
    isExpectedNumberic = "TRUE".equalsIgnoreCase(
        getConfigValue(NUMERIC_NEEDED_CONFIG_NAME, Boolean.toString(DEFAULT_GEN_PASSWORD_NEED_NUMERIC)));
    isExpectedBothCase = "TRUE".equalsIgnoreCase(
        getConfigValue(BOTH_CASE_NEEDED_CONFIG_NAME, Boolean.toString(DEFAULT_GEN_PASSWORD_NEED_BOTH_CASE)));
    isSpecialCharNeeded = "TRUE".equalsIgnoreCase(
        getConfigValue(SPECIAL_CHAR_NEEDED_CONFIG_NAME, Boolean.toString(DEFAULT_GEN_PASSWORD_NEED_SPECIAL_CHARS)));
    specialCharList = getConfigValue(SPECIAL_CHAR_LIST_CONFIG_NAME, DEFAULT_GEN_PASSWORD_SPECIAL_CHAR_LIST);

    isShowLineNumber = "TRUE".equalsIgnoreCase(
        getConfigValue(SHOW_LINE_NUMBER_CONFIG_NAME, Boolean.toString(DEFAULT_SHOW_LINE_NUMBER_CONFIG_NAME)));

  }

  public boolean isSpecialCharNeeded() {
    return isSpecialCharNeeded;
  }

  public void setSpecialCharNeeded(boolean isSpecialCharNeeded) {
    this.isSpecialCharNeeded = isSpecialCharNeeded;
  }

  public String getSpecialCharList() {
    return specialCharList;
  }

  public void setSpecialCharList(String specialCharList) {
    StringBuilder sb = new StringBuilder();
    for (char c : specialCharList.toCharArray()) {
      if (!(Character.isAlphabetic(c) || Character.isDigit(c))) {
        sb.append(c);
      }
    }
    this.setSpecialCharNeeded((sb.length() > 0));
    this.specialCharList = sb.toString();
  }

  public PasswordGenerator() {
    preInit();
  }

  public PasswordGenerator(String profileName) {
    this.profileName = profileName;
    preInit();
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

  public void init() {
    all = new ArrayList<Character>();
    all.addAll(alphaLetters);
    all.addAll(alphaUpperLetters);
    all.addAll(numericLetters);

    if (isSpecialCharNeeded) {
      if (specialCharList != null && specialCharList.length() > 0) {
        for (char c : specialCharList.toCharArray()) {
          Character ch = Character.valueOf(c);
          if (!all.contains(ch)) {
            all.add(ch);
          }
        }
      }
    }
    setSz = all.size();
    try {
      random = SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      random = new Random();
    }
    random.setSeed(System.currentTimeMillis());
    long nextSeed = random.nextLong();

    int numberOfSeedCycle = random.nextInt(MAX_NUMBER_OF_SEED_GENERATION);
    for (int i = 0; i < numberOfSeedCycle; i++) {
      random.setSeed(nextSeed);
      nextSeed = random.nextLong();
    }
  }

  private int getPasswordLength() {
    int ret = 0;

    if (minimumPasswordLength == maximumPasswordLength) {
      ret = minimumPasswordLength;
    } else {

      int diff = Math.abs(maximumPasswordLength - minimumPasswordLength) + 1;
      ret = minimumPasswordLength + new Random().nextInt(diff);
    }
    return (ret);
  }

  private String generatorPassword() {

    String password = null;
    int len = getPasswordLength();

    do {
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i < len; i++) {
        int index = random.nextInt(setSz);
        Character c = all.get(index);
        while ((i == 0) && !Character.isAlphabetic(c)) {
          index = random.nextInt(setSz);
          c = all.get(index);
        }
        sb.append(all.get(index));
      }
      password = sb.toString();
    } while (!isValidPassword(password));

    return password;

  }

  public Set<String> generatePassword() {
    return generatePassword(noOfPasswordToGenerate);
  }

  public void printGeneratedPasswords() {
    init();
    for (int i = 1; i <= noOfPasswordToGenerate; i++) {
      if (isShowLineNumber) {
        System.out.printf("[%d]: %s\n", i, generatorPassword());
      } else {
        System.out.printf("%s\n", generatorPassword());
      }
    }
  }

  public Set<String> generatePassword(int noOfPasswords) {
    Set<String> ret = new TreeSet<String>();
    init();
    for (int i = 0; i < noOfPasswords; i++) {
      ret.add(generatorPassword());
    }
    return ret;
  }

  private boolean isValidPassword(String pass) {
    boolean ret = true;

    if (isExpectedNumberic || isExpectedBothCase || isSpecialCharNeeded) {
      boolean lowerCaseFound = false;
      boolean digitFound = false;
      boolean upperCaseFound = false;
      boolean specialCharFound = false;

      for (char c : pass.toCharArray()) {
        if (!digitFound && Character.isDigit(c)) {
          digitFound = true;
        } else if (!lowerCaseFound && Character.isLowerCase(c)) {
          lowerCaseFound = true;
        } else if (!upperCaseFound && Character.isUpperCase(c)) {
          upperCaseFound = true;
        } else if (!specialCharFound && specialCharList.indexOf(c) >= 0) {
          specialCharFound = true;
        }
      }

      if (isExpectedNumberic && !digitFound) {
        // System.err.println("Password failed to have digit:" + pass) ;
        ret = false;
      }

      if (isExpectedBothCase && (!lowerCaseFound || !upperCaseFound)) {
        // System.err.println("Password failed to have mixed case:" + pass) ;
        ret = false;
      }

      if (isSpecialCharNeeded && !specialCharFound) {
        ret = false;
      }
    }

    return ret;
  }

  public String getProfileName() {
    return profileName;
  }

  public void setProfileName(String profileName) {
    this.profileName = profileName;
  }

  public String getConfigValue(String key) {
    return getConfigValue(key, null);
  }

  public int getIntConfigValue(String key) {
    return getIntConfigValue(key, 0);
  }

  public int getIntConfigValue(String key, int defaultValue) {
    int retVal = defaultValue;
    String ret = getConfigValue(key);
    if (ret != null) {
      retVal = Integer.parseInt(ret);
    }
    return retVal;
  }

  public String getConfigValue(String key, String defaultValue) {
    String ret = null;

    String kn = key;

    if (!key.startsWith(CONFIG_PREFIX)) {
      kn = CONFIG_PREFIX + key;
    }

    ret = getProfileBasedConfig(kn);

    if (ret == null) {
      ret = defaultValue;
    }

    return ret;
  }

  private String getProfileBasedConfig(String key) {

    String kn = key;
    String ret = null;

    if (profileName != null) {
      kn = profileName + "." + key;
    }

    while (kn != null && ret == null) {

      ret = getConfigLookup(kn);

      if (ret == null) {
        int foundAt = kn.indexOf(".");
        if (foundAt == -1) {
          break;
        }
        kn = kn.substring(foundAt + 1);
      }

    }

    return ret;

  }

  private String getConfigLookup(String key) {
    String ret = System.getProperty(key);
    if (ret == null) {
      ret = prop.getProperty(key);
    }
    LOG.debug("getConfig [" + key + "] => [" + ret + "]");
    return ret;
  }

  public static void main(String[] args) {
    SpringApplication.run(PasswordGenerator.class, args);
  }

}
