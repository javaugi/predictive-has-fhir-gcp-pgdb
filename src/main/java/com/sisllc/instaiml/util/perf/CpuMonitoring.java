/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util.perf;

//This creates a metric like system_cpu_usage you can view at:
import io.micrometer.core.instrument.MeterRegistry;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import org.springframework.stereotype.Component;

//http://localhost:8080/actuator/prometheus
@Component
public class CpuMonitoring {
    final MeterRegistry registry;
    final OperatingSystemMXBean osBean;

    public CpuMonitoring(MeterRegistry registry) {
        this.registry = registry;
        osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // Register custom CPU usage metric
        registry.gauge("system.cpu.usage", osBean,
            os -> os.getSystemLoadAverage() * 100);
    }
}
