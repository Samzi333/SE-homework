package com.jnu.sudokudos.dataprocess;

import android.text.TextUtils;
import android.util.Log;

import com.jnu.sudokudos.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * 题目工具
 * 主要是为了方便对单道题目的处理
 * 包括：判断是否合法、判断难度、生成题目以及检测冲突
 * 数字的数量划分难度：15~30为难，30~45为中，45~60为简
 */
public class MapTool {
    static public boolean isMap(String map){
        if(null == map)
            return false;
        if (map.length() != 81)
            return false;

        return map.matches("[0-9]+");
    }

    @NotNull
    static public String getDiff(String map){
        int count=0, idx;
        String str = map;
        // 统计0（空位）的数量
        while((idx = str.indexOf("0")) != -1) {
            str = str.substring(idx + 1);
            count++;
        }

        // 根据0的数量判断难度
        if(count > 65)
            return "s";
        if(count > 40)
            return "m";
        return "h";
    }

    @NotNull
    static public String genMap() {
        // 获取随机难度
        Random rand = new Random();
        String diffs[] = {"s", "m", "h"};
        String diff = diffs[rand.nextInt(diffs.length)];
        // 生成对应难度的图
        String map = genMap(diff);
        if (BuildConfig.DEBUG && !isMap(map)) {
            throw new AssertionError("生成了非法题目");
        }
        return map;
    }

    @NotNull
    static public String genMap(@NotNull String diff) {
//        int count = 0;
        Random rand = new Random();

        if (BuildConfig.DEBUG && !"smht".contains(diff)) {
            throw new AssertionError("难度请使用s、m、h、t");
        }
        // 通过难度控制给出数字的数量
        switch (diff) {
            case "s":
                return simpleMaps[rand.nextInt(simpleMaps.length)];
            case "m":
                return mediumMaps[rand.nextInt(mediumMaps.length)];
            case "h":
                return hardMaps[rand.nextInt(hardMaps.length)];
            case "t":
                return teachMap;
            default:
                return getZeroMap().toString();
        }

//        // 空题目
//        StringBuilder map = getZeroMap();
//
//        // 随机填数
//        int idx, tmp;
//        char numbers[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
//        char number;
//        // 填count个位置
//        for (int i = 0; i < count; ++i) {
//            idx = rand.nextInt(81);
//            number = numbers[rand.nextInt(numbers.length)];
//
//            // 避免重复位置
//            while (map.charAt(idx) != '0') {
//                idx = rand.nextInt(81);
//            }
//
//            // 避免冲突
//            // 或许有可能死循环？
//            tmp = 0;
//            map.setCharAt(idx, number);
//            while (tmp < 10 && checkError(map.toString(), idx)) {
//                map.setCharAt(idx, '0');
//                number = numbers[rand.nextInt(numbers.length)];
//                map.setCharAt(idx, number);
//                tmp++;
//            }
//            // tmp用于防止死循环，此次填数无效化
//            if(tmp >= 10) {
//                map.setCharAt(idx, '0');
//                i--;
//            }
//        }

//        return map.toString();
    }

    @NotNull
    private static StringBuilder getZeroMap() {
        String zero = "000000000";
        StringBuilder map = new StringBuilder();
        for (int i = 0; i < 9; ++i)
            map.append(zero);
        return map;
    }

    // 冲突检测
    static private boolean checkError(String map, int idx){
        int row = idx / 9;
        int col = idx % 9;
        return checkError(map, row, col);
    }

    static private boolean checkError(String map, int row, int col) {
        boolean result = false;
        result = checkSection(map, row, col);
        if (result) return result;
        //check row
        for (int i = 0; i < 9; i++) {
            String value = map.charAt(i*9 + col)+"";
            if ("0".equals(value)) continue;
            for (int j = i; j < 9; j++) {
                if (i == j) continue;
                if (value.equals(map.charAt(j*9 + col)+"")) {
                    result = true;
                    break;
                }
            }
        }

        if (result) return result;

        //check column
        for (int i = 0; i < 9; i++) {
            String value = map.charAt(row*9 + i)+"";
            if ("0".equals(value)) continue;
            for (int j = i; j < 9; j++) {
                if (i == j) continue;
                if (value.equals(map.charAt(row*9 + j)+"")) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    static public boolean checkSection(@NotNull String map, int row, int col) {
        boolean result = false;
        String value = map.charAt(row*9 + col)+"";
        if (value.equals("")) {
            return result;
        }
        int start_i = row < 3 ? 0 : (row < 6 ? 3 : 6);//3x3 格子的边界
        int start_j = col < 3 ? 0 : (col < 6 ? 3 : 6);
        int end_i = start_i + 3;
        int end_j = start_j + 3;

        for (int i = start_i; i < end_i; i++) {
            for (int j = start_j; j < end_j; j++) {
                if (i == row && j == col) continue;
                if (value.equals(map.charAt(i*9 + j))) {//如果3x3格子的内容有重复的数字则返回错误
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    static private String simpleMaps[] = {
            "500091703"+"420003000"+"010800000"+"640002019"+"700010004"+"180900057"+"000006030"+"000200046"+"906340002",
            "920001003"+"100030007"+"000085906"+"040510090"+"010900704"+"002070000"+"000157060"+"509006070"+"001400800",
            "703000020"+"000005403"+"008630100"+"000903804"+"010027000"+"309080002"+"240050090"+"030006040"+"600400705",
    };
    static private String mediumMaps[] = {
            "200800070"+"003000910"+"700306000"+"004062800"+"620005000"+"050000607"+"030070009"+"042000730"+"006950002",
            "090008000"+"000100062"+"608050000"+"500360400"+"001000030"+"086001250"+"400200005"+"060000903"+"100003000",
            "600000720"+"000062009"+"200030080"+"000009072"+"007185000"+"305000100"+"048000090"+"070000803"+"000250000"
    };
    static private String hardMaps[] = {
            "020006000"+"000800670"+"003000040"+"510300000"+"000092001"+"000700360"+"000020003"+"207040000"+"930001000",
            "000050480"+"010000900"+"080009000"+"001060003"+"700000000"+"290807000"+"008040005"+"003106002"+"004000600"
    };
    static private String defaultMap =
            "005406000"+"000000201"+"007380000"+
                    "062700090"+"050023804"+"704109060"+
                    "823590010"+"490867020"+"576031948";
    static private String teachMap =
            "123456789"+"456789123"+"789123456"+
                    "231564978"+"564807312"+"897231645"+
                    "310645897"+"645978231"+"978312564";
    public static String getDefaultMap() {
        return defaultMap;
    }

    public static String getTeachMap() {
        return teachMap;
    }
}
