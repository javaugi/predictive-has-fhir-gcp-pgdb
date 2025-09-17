/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author javau
 */
public class CommonUtils {
    
    public static String listToString(List<String> list) {
        return listToString(list, ",");
    }
    
    public static String listToString(List<String> list, String delim) {
        return String.join(delim, list);
    }

    public static List<String> stringToList(String str) {
        return stringToList(str, ",");
    }
    
    public static List<String> stringToList(String str, String delim) {
        return List.of(str.split(delim));
    }
    
    public static int stringTokensize(String str) {
        return stringToList(str).size();
    }
    
    public static int stringTokensize(String str, String delim) {
        return stringToList(str, delim).size();
    }
    
    public static String convertMapToJson(Map<String, Object> map) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(map);
        }catch(JsonProcessingException ex) {
        }
        return null;
    }
    
    public static Map<String, Object> convertJsonToMap(String json) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        }catch(JsonProcessingException ex) {
        }

        return new HashMap<String, Object>();
    }    
}
