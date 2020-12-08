package com.jnu.sudokudos.dataprocess;

import java.util.ArrayList;
import java.util.Random;

public class Qbank {
    static private String defaultMap = "005406000000000201007380000062700090050023804704109060823590010490867020576031948";
    static private ArrayList<String> simpleMaps = new ArrayList<>();

    static public void Load(){
        simpleMaps.add(defaultMap);
        simpleMaps.add("123456789"+"456789123"+"789123456"+
                "231564978"+"564807312"+"897231645"+
                "310645897"+"645978231"+"978312564");
    }

    static public String getSimpleMap(){
        Random rand = new Random();
        return simpleMaps.get(rand.nextInt(simpleMaps.size()));
    }

    static public String getDefaultMap(){
        return defaultMap;
    }
}
