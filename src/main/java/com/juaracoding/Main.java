package com.juaracoding;

import com.juaracoding.TA.model.Divisi;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Properties properties = System.getProperties();
        properties.forEach((k,v)-> System.out.println(k+" : "+v));
    }
}