package com.jnu.sudokudos.dataprocess;

import java.util.ArrayList;
import java.util.Random;

public class Qbank {
    static private String defaultMap =
            "005406000"+"000000201"+"007380000"+
            "062700090"+"050023804"+"704109060"+
            "823590010"+"490867020"+"576031948";
    static private ArrayList<String> simpleMaps = new ArrayList<>();
    static private ArrayList<String> mediumMaps = new ArrayList<>();
    static private ArrayList<String> hardMaps = new ArrayList<>();

    static public void loadMap(){
        add(defaultMap);
        add("123456789"+"456789123"+"789123456"+
                "231564978"+"564807312"+"897231645"+
                "310645897"+"645978231"+"978312564");
    }

    static public void add(String map){
        add(map, MapTool.getDiff(map));
    }

    static public void add(String map, String diff){
        if(!MapTool.isMap(map))
            return ;
        switch (diff){
            case "s": simpleMaps.add(map); break;
            case "m": mediumMaps.add(map); break;
            case "h": hardMaps.add(map); break;
            default: break;
        }
    }

    static public String getSimpleMap(){
        if(mediumMaps.isEmpty())
            return defaultMap;

        Random rand = new Random();
        return simpleMaps.get(rand.nextInt(simpleMaps.size()));
    }

    static public String getSimpleMap(int idx){
        if(idx >= simpleMaps.size() || idx < 0)
            return defaultMap;

        Random rand = new Random();
        return simpleMaps.get(rand.nextInt(simpleMaps.size()));
    }

    static public String getMediumMap(){
        if(mediumMaps.isEmpty())
            return defaultMap;

        Random rand = new Random();
        return mediumMaps.get(rand.nextInt(mediumMaps.size()));
    }

    static public String getMediumMap(int idx){
        if(idx >= mediumMaps.size() || idx < 0)
            return defaultMap;

        Random rand = new Random();
        return mediumMaps.get(rand.nextInt(mediumMaps.size()));
    }

    static public String getHardMap(){
        if(hardMaps.isEmpty())
            return defaultMap;

        Random rand = new Random();
        return hardMaps.get(rand.nextInt(hardMaps.size()));
    }

    static public String getHardMap(int idx){
        if(idx >= hardMaps.size() || idx < 0)
            return defaultMap;

        Random rand = new Random();
        return hardMaps.get(rand.nextInt(hardMaps.size()));
    }


    static public String getDefaultMap(){
        return defaultMap;
    }
}
