/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "spring.r2dbc")
@Component
public class DatabaseProperties {
   
    private String devUrl;
    private String devUsername;
    private String devPassword;
    private String devDdlSchemaDir;
    private String devHost;
    private String devPort;
    private String devDatabase;
    
    private String testUrl;
    private String testUsername;
    private String testPassword;
    private String testDdlSchemaDir;
    private String testHost;
    private String testPort;
    private String testDatabase;
    
    private String prodUrl;
    private String prodUsername;
    private String prodPassword;
    private String prodDdlSchemaDir;
    private String prodHost;
    private String prodPort;
    private String prodDatabase;

    private ProfileSetting profileSetting;
    private Boolean setupMockUserOnly;
    private Boolean truncateMockData;

    private String url;
    private String username;
    private String password;
    private String host;
    private String port;
    private String ddlSchemaDir;
    private String database;

    private Integer poolInitialSize = 8;
    private Integer poolMaxSize = 20;
    private Integer poolMinSize = 5;
    private Integer connTimeout = 2000;

    public static enum ProfileSetting {
        DEV, TEST, PROD
    }
    
    public void setupBaseDbProps(ProfileSetting ps) {
        profileSetting = ps;
        
        switch(profileSetting) {
            case ProfileSetting.PROD -> {
                this.url = this.prodUrl;
                this.host = this.prodHost;
                this.port = this.prodPort;
                this.username = this.prodUsername;
                this.password = this.prodPassword;
                this.database = this.prodDatabase;
                this.ddlSchemaDir = this.prodDdlSchemaDir;
            }
            case ProfileSetting.DEV -> {
                this.url = this.devUrl;
                this.host = this.devHost;
                this.port = this.devPort;
                this.username = this.devUsername;
                this.password = this.devPassword;
                this.database = this.devDatabase;
                this.ddlSchemaDir = this.devDdlSchemaDir;
            }
            default -> {
                this.url = this.testUrl;
                this.host = this.testHost;
                this.port = this.testPort;
                this.username = this.testUsername;
                this.password = this.testPassword;
                this.database = this.testDatabase;
                this.ddlSchemaDir = this.testDdlSchemaDir;
            }

        }
    }

    @Override
    public String toString() {
        return "DatabaseProperties{" + "url=" + url + ", username=" + username + ", host=" + host + ", port=" + port + ", ddlSchemaDir=" + ddlSchemaDir + ", databaseUsed=" + database + '}';
    }

}
