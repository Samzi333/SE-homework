package com.jnu.sudokudos.dataprocess;

import java.util.ArrayList;
import java.util.Random;

public class Qbank {
    static private String defaultMap =
            "005406000"+"000000201"+"007380000"+
            "062700090"+"050023804"+"704109060"+
            "823590010"+"490867020"+"576031948";
    static private String teachMap =
            "123456789"+"456789123"+"789123456"+
            "231564978"+"564807312"+"897231645"+
            "310645897"+"645978231"+"978312564";
    static private String nowMap;

    static public void loadMap(){

    }

    static public void saveMap(){

    }

    static public String getSimpleMap(){
        return MapTool.genMap("s");
    }

    static public String getMediumMap(){
        return MapTool.genMap("m");
    }

    static public String getHardMap(){
        return MapTool.genMap("h");
    }

    static public String getDefaultMap(){
        return defaultMap;
    }

    public static String getTeachMap() {
        return teachMap;
    }
}
