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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootMainApp {

  private static final Logger LOG = LoggerFactory.getLogger(SpringBootMainApp.class);

  public static void main(String[] args) {
    LOG.info("Start of SpringBoot " + SpringBootMainApp.class.getName() + " - .......");
    SpringApplication.run(SpringBootMainApp.class, args);
    LOG.info("Kicked off SpringBoot class : " + SpringBootMainApp.class.getName() + " - DONE.");
  }

}
