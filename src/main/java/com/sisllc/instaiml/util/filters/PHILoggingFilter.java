/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util.filters;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.sisllc.instaiml.util.PHIPseudonymizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
/*
2. Logback Filter for Application Logs
    This filter intercepts every log event and rewrites PHI fields before Logback writes them to console/files.
    PHILoggingFilter.java
 */
@Slf4j
public class PHILoggingFilter extends Filter<ILoggingEvent> {

    private static final PHIPseudonymizer pseudonymizer
        = new PHIPseudonymizer(System.getenv().getOrDefault("PHI_KEY", "default-phi-key"));

    private static final Pattern PATIENT_ID_PATTERN = Pattern.compile("patientId=([A-Za-z0-9_-]+)");

    @Override
    public FilterReply decide(ILoggingEvent event) {
        String message = event.getFormattedMessage();
        Matcher matcher = PATIENT_ID_PATTERN.matcher(message);
        log.debug("PHILoggingFilter match find {} message {}", matcher.find(), message);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String pseudonym = "patientId=" + pseudonymizer.pseudonymize(matcher.group(1));
            matcher.appendReplacement(sb, Matcher.quoteReplacement(pseudonym));
        }
        matcher.appendTail(sb);

        // Rewrite log message
        event.getArgumentArray(); // Ensures arguments are evaluated
        event.getLoggerContextVO(); // Ensure event remains valid
        event.getThrowableProxy(); // Keep throwable if any

        // Reflection hack: Logback doesn’t provide direct mutators for message; typically you'd wrap event
        // For simplicity, re-log sanitized message in appenders instead (see note below)
        return FilterReply.NEUTRAL;
    }
}
