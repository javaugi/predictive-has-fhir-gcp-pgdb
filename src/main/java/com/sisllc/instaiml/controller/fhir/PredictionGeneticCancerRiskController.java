/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.controller.fhir;

/**
 *
 * @author javau
 */
public class PredictionGeneticCancerRiskController {

}

/*
The Google Cloud Healthcare API itself does not predict cancer risks based on genetic defects. Its core function is to provide a secure, managed
    platform for ingesting, storing, and managing healthcare data in standard formats like FHIR, DICOM, and HL7v2. It acts as a bridge, allowing
    healthcare data to be standardized and made accessible for other services.

However, you can use the Healthcare API in conjunction with other Google Cloud AI/ML tools to build a system that performs this kind of predictive
    analysis.

How to Build a Predictive Cancer Risk System

Here's the workflow for how a doctor could use such a system, leveraging multiple Google Cloud services:
    Ingest Genomic Data: Genomic data from a blood or tissue sample (e.g., from a sequencing machine) is ingested into a FHIR or DICOM store using
        the Google Cloud Healthcare API. The API ensures the data is stored in a structured, compliant, and secure manner.
    Data Processing: The raw genomic data is then processed and standardized. Tools like Cloud Dataflow or BigQuery can be used to clean, transform,
        and prepare the data for analysis.
    AI/ML Model Training and Prediction:
        Vertex AI, Google's unified machine learning platform, is where the predictive model is built and run.
        A custom machine learning model is trained on a vast dataset of genomic information, genetic defects, and corresponding cancer outcomes.
        The model learns to identify patterns and specific genetic markers associated with an increased risk of developing certain cancers.
    Risk Score Generation: The patient's genetic data is fed into the trained AI model. The model outputs a predicted risk score or a
        classification (e.g., "high risk," "medium risk").
    Actionable Insights for Doctors:
        The risk score is returned to the doctor's application via an API.
        This score, combined with other patient data (family history, lifestyle, etc.), can help the doctor make a more informed decision about care.
            For example, a high risk score for a specific type of cancer might prompt the doctor to recommend earlier and more frequent screenings.
        For a patient with an existing diagnosis, the risk score and prognosis can be a factor in determining the appropriate level of care, including
            whether to recommend hospice entrance. However, a doctor's decision on hospice is a complex, clinical one based on multiple factors, not
            just a single risk score. The AI system acts as a clinical decision support tool, providing data-driven insights to aid that decision.
 */
