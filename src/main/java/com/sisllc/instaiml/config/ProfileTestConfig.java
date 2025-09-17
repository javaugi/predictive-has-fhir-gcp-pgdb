/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.config;

import java.io.Serializable;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(ProfileTestConfig.TEST_PROFILE)
@Configuration
public class ProfileTestConfig implements Serializable {
    private static final long serialVersionUID = 35238717363L;
    public static final String TEST_PROFILE = "test";
}
