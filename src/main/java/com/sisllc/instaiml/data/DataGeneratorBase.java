/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class DataGeneratorBase {

    public static final Random rand = new Random();
    public static final Faker FAKER = new Faker();

    public static final List<String> MED_SPECIALTIES = List.of("Allergy and immunology", "Anesthesiology", "Dermatology", "Diagnostic radiology",
        "Emergency medicine", "Family medicine", "Internal medicine", "Medical genetics", "Neurology", "Nuclear medicine", "Obstetrics and gynecology",
        "Ophthalmology", "Pathology", "Pediatrics", "Physical medicine and rehabilitation", "Preventive medicine", "Psychiatry", "Radiation oncology",
        "Surgery", "Urology");

    protected static String generateAgeGroupBracket() {
        StringBuilder sb = new StringBuilder();
        int ndx = FAKER.number().numberBetween(0, 6);
        if (ndx >= 6) {
            sb.append("60+");
        } else {
            sb.append(ndx);
            if (ndx > 0) {
                sb.append(0);
            }
            sb.append("-");

            if (ndx > 0) {
                sb.append(ndx);
            }
            sb.append(9);
        }
        return sb.toString();
    }
   
    protected static String getStateAbbr() {
        String stateAbbr = FAKER.address().stateAbbr();
        if (StringUtils.isNotEmpty(stateAbbr)) {
            return stateAbbr;
        }
        
        return "MI";
    }
        
    protected static String getZipCodeByStateAbbr(String stateAbbr) {
        String zipCode = FAKER.address().zipCodeByState(stateAbbr);
        if (StringUtils.isNotEmpty(zipCode)) {
            return zipCode;
        }        
        return "48198";
    }

    protected static float[] getEmbeddingFloatArray() {
        List<Double> embeddingList = new Random().doubles(1536, 0, 1) // Assuming 1536-dimension vector
            .boxed()
            .collect(Collectors.toList());

        float[] floatArray = new float[embeddingList.size()];
        for (int i = 0; i < embeddingList.size(); i++) {
            floatArray[i] = embeddingList.get(i).floatValue(); // Or (float) embeddingList.get(i)
        }

        return floatArray;
    }

    protected static byte[] generateFakePdfContent() {
        // Create a simple PDF structure with random content
        String pdfHeader = "%PDF-1.4\n";
        String content = "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n";
        content += "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n";
        content += "3 0 obj\n<< /Type /Page /Parent 2 0 R /Contents 4 0 R >>\nendobj\n";
        content += "4 0 obj\n<< /Length 55 >>\nstream\nBT\n/F1 12 Tf\n72 720 Td\n";
        content += "(" + FAKER.lorem().paragraph() + ") Tj\nET\nendstream\nendobj\n";
        String xref = "xref\n0 5\n0000000000 65535 f \n";
        String trailer = "trailer\n<< /Size 5 /Root 1 0 R >>\nstartxref\n" + (pdfHeader.length() + content.length()) + "\n%%EOF";

        String fullPdf = pdfHeader + content + xref + trailer;
        return fullPdf.getBytes(StandardCharsets.UTF_8);
    }

    protected static byte[] generateFromTemplate() throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText(FAKER.lorem().paragraph());
                contentStream.endText();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }
}
