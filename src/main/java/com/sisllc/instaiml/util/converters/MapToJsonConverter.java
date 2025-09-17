/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.AttributeConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

//@Converter(autoApply = false) // Optional: set to true to apply globally
@Component
//ChatGPT says 
// 2. Don’t Annotate It with @Component or Try to Autowire Anything
public class MapToJsonConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper;
    private static final ObjectMapper objectMapperNew = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .registerModule(new JavaTimeModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    
    public MapToJsonConverter() {
        objectMapper = new ObjectMapper();
        System.out.println("MapToJsonConverter instantiated!"); // Debug line
        // Required public no-arg constructor
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Object> map) {
        try {
            return map == null ? null : objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not convert map to JSON", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String json) {
        try {
             return json == null ? null : 
                objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {});
            //return objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not convert JSON to map", e);
        }
    }
}
/*
This first part is from DeepSeek and the second part if from ChatGPT    

Solving the AttributeConverter Instantiation Error
The error you're encountering (BeanCreationException: Unable to instantiate AttributeConverter) typically occurs when Spring cannot properly instantiate your MapToJsonConverter class. Let's analyze and fix this issue.

Root Causes
    Missing Spring Component Annotation: Your converter isn't being recognized as a Spring bean
    Serialization/Deserialization Issues: Potential problems with the ObjectMapper configuration
    Incorrect Registration: The converter might not be properly registered with JPA

Solution 1: Add @Component Annotation

import org.springframework.stereotype.Component;
import javax.persistence.Converter;

@Component
@Converter(autoApply = false)
public class MapToJsonConverter implements AttributeConverter<Map<String, Object>, String> {
    // ... existing implementation ...
}
Solution 2: Explicit Bean Configuration
If you prefer not to use @Component, register the converter in your JPA configuration:

@Configuration
public class JpaConfig {
    
    @Bean
    public MapToJsonConverter mapToJsonConverter() {
        return new MapToJsonConverter();
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource, 
            MapToJsonConverter mapToJsonConverter) {
        
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.your.package");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        // Register the converter
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.attribute-converters", mapToJsonConverter);
        em.setJpaPropertyMap(properties);
        
        return em;
    }
}

Solution 3: Check ObjectMapper Configuration
Sometimes the issue is with JSON serialization. Enhance your converter with proper type handling:

@Converter
public class MapToJsonConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .registerModule(new JavaTimeModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON writing error", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : 
                objectMapper.readValue(dbData, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException("JSON reading error", e);
        }
    }
}
Solution 4: Verify Entity Mapping
Ensure your entity properly references the converter:

@Entity
public class MyEntity {
    
    @Convert(converter = MapToJsonConverter.class)
    @Column(columnDefinition = "jsonb") // or "text" depending on your DB
    private Map<String, Object> properties;
    
    // ... other fields and methods ...
}
Solution 5: Debugging Steps
If the above solutions don't work, add debugging:

java
Copy
public class MapToJsonConverter implements AttributeConverter<Map<String, Object>, String> {
    
    public MapToJsonConverter() {
        System.out.println("MapToJsonConverter instantiated!"); // Debug line
    }
    
    // ... rest of the implementation ...
}
Then check your logs to see if the converter is being instantiated.

Common Pitfalls
Missing Dependencies: Ensure you have these in your pom.xml or build.gradle:

xml
Copy
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.13.3</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
    <version>2.13.3</version>
</dependency>
Run HTML
Incorrect Auto-Apply: If using @Converter(autoApply = true), remove any explicit @Convert annotations from entity fields.

Database Column Type Mismatch: Ensure your database column type matches what you're storing (JSON, TEXT, etc.)

Would you like me to elaborate on any particular aspect of these solutions or provide additional debugging techniques?


*/

/* This part ifs from ChatGPT
I'm still getting this error after the change  org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in com.spring5.MyApplication: Unable to instantiate AttributeConverter [com.spring5.utils.MapToJsonConverter]


ChatGPT said:
That error means Spring/Hibernate is trying to instantiate your MapToJsonConverter but failing — likely because it can't create an instance of it due to some misconfiguration.

Here’s a checklist to help you fix this:

✅ 1. Ensure It’s a public Class with a public No-Arg Constructor
Hibernate uses reflection to instantiate converters. So:

java
Copy
Edit
package com.spring5.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;

@Converter(autoApply = false) // Optional: set to true to apply globally
public class MapToJsonConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public MapToJsonConverter() {
        // Required public no-arg constructor
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting map to JSON", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error reading JSON into map", e);
        }
    }
}
✅ 2. Don’t Annotate It with @Component or Try to Autowire Anything
@Converter is a JPA annotation, not a Spring bean, so don’t use @Component, and don’t try to @Autowired anything inside it.

Hibernate instantiates @Converter classes with reflection and doesn’t support Spring DI in them unless you use a more advanced setup (e.g., @Configurable or registering converters manually).

✅ 3. Clean & Rebuild the Project
Sometimes, stale bytecode in your IDE or build system (e.g., Maven/Gradle) can cause issues like this.

Run:

bash
Copy
Edit
./mvnw clean install
Or in Gradle:

bash
Copy
Edit
./gradlew clean build
✅ 4. Check for Classpath Issues
If MapToJsonConverter uses Jackson, make sure you have Jackson databind in your classpath:

xml
Copy
Edit
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
✅ 5. Bonus: Register the Converter Manually (if needed)
If the problem persists, you can explicitly register the converter via @Converter(autoApply = false) and use @Convert on your field to avoid global application.

✅ Example Entity Usage
java
Copy
Edit
@Entity
public class AuditEntry {

    @Id
    @GeneratedValue
    private Long id;

    private String action;

    @Convert(converter = MapToJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> metadata;

    // Getters and setters
}

*/