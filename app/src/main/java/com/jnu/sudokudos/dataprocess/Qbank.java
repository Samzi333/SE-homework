package com.jnu.sudokudos.dataprocess;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    static private Map<String, String> currentMaps = new HashMap<String, String>();
    static private final String SAVE_FILE_NAME = "date.txt";

    @NotNull
    static public String loadMap(Context context, String diff){
        //反序列化
        ObjectInputStream inputStream = null;
        currentMaps = new HashMap<String, String>();
        try {
            inputStream = new ObjectInputStream(context.openFileInput(SAVE_FILE_NAME));
            currentMaps = (Map<String, String>) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (currentMaps.get(diff) == null){
            return MapTool.genMap(diff);
        }
        String map = currentMaps.get(diff);
        if(map == null)
            map = MapTool.genMap(diff);
        return map;
    }

    static public void saveMap(Context context, String diff, String map){
        currentMaps.put(diff, map);
        //序列化
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(context.openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE));
            outputStream.writeObject(currentMaps);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
