/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util;

import org.springframework.stereotype.Component;

@Component
public class RegionContextHolder {
    private static final ThreadLocal<String> context = new ThreadLocal<>();

    public static void setRegion(String region) {
        context.set(region);
    }

    public static String getCurrentRegion() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
