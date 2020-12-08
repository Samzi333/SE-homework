package com.jnu.sudokudos.dataprocess;

public class Map {
    private String map;

    public boolean isMap(){
        if (map.length() != 81)
            return false;

        boolean b =  map.matches("[0-9]+");
        return b;
    }

    public Map(String map) {
        this.map = map;
    }

    @Override
    public String toString() {
        if (!isMap()) {
            String s0 = "000000000";
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < 9; ++i)
                s.append(s0);
            return s.toString();
        }
        return map;
    }
}
