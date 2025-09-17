/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.config;

import java.io.Serializable;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(ProfileDevConfig.DEV_PROFILE)
public class ProfileDevConfig implements Serializable {
    private static final long serialVersionUID = 321357244048L;
    protected static final String DEV_PROFILE = "dev";

}
