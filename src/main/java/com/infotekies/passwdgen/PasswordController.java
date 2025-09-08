/*
 * Copyright (c) 2004 InfoTekies Corp., All rights reserved.
 *
 * This software is the confidential and proprietary information of InfoTekies.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with InfoTekies.
 */

package com.infotekies.passwdgen;

import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordController {

  private static final Logger LOG = LoggerFactory.getLogger(PasswordController.class);

  @GetMapping(value = "/v1/{profileName}/passwords")
  public Set<String> getPasswords(@PathVariable String profileName) {
    Set<String> ret = null;
    LOG.info("getPasswords called with profileName : [" + profileName + "]");
    try {
      PasswordGenerator pg = (profileName == null ? new PasswordGenerator() : new PasswordGenerator(profileName));
      pg.init();
      ret = pg.generatePassword();
    } catch (Throwable t) {
      LOG.error("Unable to generate passwords due to exception: ", t);
    } finally {
      int len = (ret == null ? 0 : ret.size());
      LOG.info("getPasswords called with profileName : [" + profileName + "] returned [" + len + "] passwords.");
    }
    return ret;
  }

  @GetMapping(value = { "/", "/passwords" })
  public Set<String> getPasswordsWithDefaultProfile() {
    return getPasswords(null);
  }

  @GetMapping("/error")
  public String handleError(HttpServletRequest request) {
    String ret = null;
    try {
      Object status = null;
      if (request != null) {
        status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
      }
      ret = (status != null ? ("error-" + status) : "error-unknown-status");
    } finally {
      LOG.error("handleError : " + ret);
    }
    return ret;
  }
}
