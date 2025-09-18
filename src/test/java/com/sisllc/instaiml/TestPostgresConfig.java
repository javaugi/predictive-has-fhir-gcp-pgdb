/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml;

import com.google.cloud.spring.autoconfigure.sql.R2dbcCloudSqlEnvironmentPostProcessor;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@TestConfiguration
@TestPropertySource(locations = {
    "file:.env",
    "classpath:application.properties"
})
/*
@SpringBootTest(classes = MyReactiveApplication.class,
    properties = {"spring.r2dbc.url=r2dbc:postgresql://localhost:5432/RPG_TEST",
        "spring.r2dbc.username=postgres",
        "spring.r2dbc.password=admin"})
@EnableAutoConfiguration(exclude = R2dbcCloudSqlEnvironmentPostProcessor.class)
//*/
public class TestPostgresConfig {

    @Value("${spring.r2dbc.url}")
    public String url;
    @Value("${spring.r2dbc.username}")
    public String uname;
    @Value("${spring.r2dbc.password}")
    public String pwd;

    @Bean
    @Primary
    public ConnectionFactory connectionFactory() {
        log.info("TestPostgresConfig conn url {}", url);
        return ConnectionFactories.get(
            String.format("%s?user=%s&password=%s", url, uname, pwd)
        );
    }
}
