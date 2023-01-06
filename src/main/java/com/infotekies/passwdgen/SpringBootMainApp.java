/*
 * Copyright (c) 2004 InfoTekies Corp., All rights reserved.
 *
 * This software is the confidential and proprietary information of InfoTekies.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with InfoTekies.
 */

package com.infotekies.passwdgen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootMainApp implements CommandLineRunner {

  private final Logger LOG = LoggerFactory.getLogger(this.getClass());

  @Override
  public void run(String... args) throws Exception {
    LOG.info("run() method is being called");
    try {

    } finally {
      LOG.info("run() method has been completed.");
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringBootMainApp.class, args);
  }

}
