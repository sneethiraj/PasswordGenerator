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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordController {

  @GetMapping(value = "/v1/{profileName}/passwords")
  public Set<String> getPasswords(@PathVariable String profileName) {
    PasswordGenerator pg = new PasswordGenerator(profileName);
    pg.init();
    return pg.generatePassword();
  }

  @GetMapping("/error")
  public String handleError(HttpServletRequest request) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    return (status != null ? ("error-" + status) : "error-unknown-status");
  }

}
