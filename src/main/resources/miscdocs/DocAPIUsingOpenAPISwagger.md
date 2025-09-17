Documenting your API with OpenAPI/Swagger is a critical skill, especially for a Technical Lead role where clarity, communication, and maintainability are paramount.

Here is a detailed code example integrating OpenAPI (Swagger) documentation into a Spring Boot application, complete with explanations.

1. Dependencies (Maven pom.xml)
First, you need to add the necessary dependency. Springdoc OpenAPI is the modern, popular choice for Spring Boot 3+ (it supports both Spring Boot 2 and 3). It automatically generates documentation from your code.

xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version> <!-- Check for the latest version -->
</dependency>
For a reactive application (WebFlux), you would use springdoc-openapi-starter-webflux-ui.

2. The Spring Boot Application with a Simple REST Controller
Let's create a simple ProjectController for our "Fulfillment Orchestrator" example.

Project.java (Entity)

java
package com.example.oms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private LocalDateTime dateCreated = LocalDateTime.now();

    // Standard getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }
}
ProjectDTO.java (Data Transfer Object)

java
package com.example.oms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

// Using @Schema annotations to document the DTO
public class ProjectDTO {
    @Schema(description = "Unique identifier of the project", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name of the project", example = "Enterprise Fulfillment Dashboard", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Schema(description = "Detailed description of the project", example = "A microservice to handle all fulfillment operations.")
    private String description;

    @Schema(description = "Timestamp when the project was created", example = "2024-10-27T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dateCreated;

    // Standard getters and setters
    // ... 
}
3. The Documented REST Controller
This is where the magic happens. We use annotations from the io.swagger.v3.oas.annotations package.

ProjectController.java

java
package com.example.oms.controller;

import com.example.oms.dto.ProjectDTO;
import com.example.oms.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
// @Tag gives a name to this group of endpoints in the UI
@Tag(name = "Project Management", description = "APIs for managing development projects within the Order Management System")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation( // @Operation describes a single API operation
            summary = "Retrieve all projects",
            description = "Fetches a list of all project entities from the database."
    )
    @ApiResponse( // @ApiResponse describes a possible response
            responseCode = "200",
            description = "Successfully retrieved list of projects",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDTO[].class))
    )
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a project by its ID")
    @ApiResponses(value = { // Use @ApiResponses for multiple possible responses
            @ApiResponse(responseCode = "200", description = "Found the project", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content) // No content schema for 404
    })
    public ResponseEntity<ProjectDTO> getProjectById(
            @Parameter( // @Parameter describes a method parameter
                    description = "ID of the project to be retrieved",
                    required = true,
                    example = "123"
            )
            @PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new project")
    @ApiResponse(
            responseCode = "201",
            description = "Project created successfully"
    )
    public ResponseEntity<ProjectDTO> createProject(
            @Parameter(
                    description = "Project object to be created. The ID and dateCreated will be generated automatically and can be ignored.",
                    required = true
            )
            @Valid @RequestBody ProjectDTO projectDto) {
        ProjectDTO savedProject = projectService.save(projectDto);
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<ProjectDTO> updateProject(
            @Parameter(description = "ID of the project to be updated", required = true, example = "123") @PathVariable Long id,
            @Parameter(description = "Updated project object", required = true) @Valid @RequestBody ProjectDTO projectDto) {
        return ResponseEntity.ok(projectService.update(id, projectDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project by its ID")
    @ApiResponse(responseCode = "204", description = "Project deleted successfully, no content to return")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of the project to be deleted", required = true, example = "123") @PathVariable Long id) {
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
4. Global API Configuration (Optional but Recommended)
You can create a configuration bean to add global metadata like the API title, version, and description. This is much better than having it auto-generated from application properties.

OpenApiConfig.java

package com.example.oms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Management System API")
                        .description("""
                            This is the API documentation for the Next-Gen Order Management System (OMS).
                            It provides endpoints for managing orders, fulfillment processes, and related projects.
                            **Important:** All endpoints require JWT authentication.
                            """)
                        .version("v1.0")
                        .contact(new Contact()
                                .name("OMS Development Team")
                                .email("oms-support@company.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
                // You can also add global security schemes here (e.g., JWT)
                // .components(new Components().addSecuritySchemes("bearer-jwt", ...))
                // .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}
5. Viewing the Documentation
Once you start your Spring Boot application:
    The interactive Swagger UI will be available at: http://localhost:8080/swagger-ui.html
    The raw OpenAPI JSON specification will be available at: http://localhost:8080/v3/api-docs
What you get in the Swagger UI:
    A list of all your tagged controllers (e.g., "Project Management").
    A collapsible list of all endpoints for each controller.
    For each endpoint:
        The HTTP verb and path.
        A description from the @Operation annotation.
        A list of parameters with their descriptions, examples, and whether they are required.
        The structure of the request body (for POST/PUT), generated from your ProjectDTO schema.
        All possible HTTP responses and the schema of the response body for each.
    A "Try it out" button to execute calls directly from the browser against your running API.

Key Benefits for a Technical Lead
    Single Source of Truth: The code is the documentation. It can't get out of sync.
    Improved Collaboration: Product owners, QA engineers, and frontend developers can all explore the API contract without needing to ask developers.
    Onboarding: New team members can quickly understand what the API does.
    Automated Client Generation: Tools can use the /v3/api-docs endpoint to automatically generate client SDKs in various languages.
    Quality Enforcement: The act of documenting forces you to think about the API contract, leading to better design.
