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

    static public String genFullMap(){
        StringBuilder map = getZeroMap();
        int i, j, k, value;
        Random random = new Random();

        // 初始化
        int n = 11;
        int[][] field = new int[10][10];
        boolean[][] rows = new boolean[10][10];
        boolean[][] cols = new boolean[10][10];
        boolean[][] blocks = new boolean[10][10];
        for(i = 0; i < 9; i++) {
            for(j = 0; j < 9; j++) {
                field[i][j] = 0;
                rows[i][j+1] = false;
                cols[i][j+1] = false;
                blocks[i][j+1] = false;
            }
        }

        // 随机填入数字
        while(n > 0) {
            i = random.nextInt(9);
            j = random.nextInt(9);
            if(field[i][j] == 0) {
                k = i / 3 * 3 + j / 3;
                value = random.nextInt(9) + 1;
                if(!rows[i][value] && !cols[j][value] && !blocks[k][value]) {
                    field[i][j] = value;
                    rows[i][value] = true;
                    cols[j][value] = true;
                    blocks[k][value] = true;
                    n--;
                }
            }
        }

        for(i = 0; i < 9; i++) {
            for(j = 0; j < 9; j++) {
                map.setCharAt(i*9 + j, (field[i][j]+"").charAt(0));
            }
        }

        if(dfs(field, rows, cols, blocks))
            return genFullMap();

        return map.toString();
    }

    private static boolean dfs(int[][] f, boolean[][] r, boolean[][] c, boolean[][] b) {
        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 9; j++)
                if(f[i][j] == 0) {
                    int k = i / 3 * 3 + j / 3;
                    // 尝试填入1~9
                    for(int n = 1; n < 10; n++) {
                        if(!r[i][n] && !c[j][n] && !b[k][n]) {
                            // 尝试填入一个数
                            r[i][n] = true;
                            c[j][n] = true;
                            b[k][n] = true;
                            f[i][j] = n;
                            // 检查是否满足数独正解
                            if(dfs(f, r, c, b))
                                return true;
                            // 不满足则回溯
                            r[i][n] = false;
                            c[j][n] = false;
                            b[k][n] = false;
                            f[i][j] = 0;
                        }
                    }
                    // 尝试所有数字都不满足则回溯
                    return false;
                }
        return true;
    }

    @NotNull
    static public String genMap(@NotNull String diff) {
        int count = 0;
        Random rand = new Random();

        if (BuildConfig.DEBUG && !"smh".contains(diff)) {
            throw new AssertionError("难度请使用s、m、h");
        }
        // 通过难度控制给出数字的数量
        switch (diff) {
            case "s":
                count = rand.nextInt(60 - 45) + 45;
                break;
            case "m":
                count = rand.nextInt(45 - 30) + 30;
                break;
            case "h":
                count = rand.nextInt(30 - 15) + 15;
                break;
            default:
                break;
        }

        // 空题目
        StringBuilder map = getZeroMap();

        // 随机填数
        int idx, tmp;
        char numbers[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char number;
        // 填count个位置
        for (int i = 0; i < count; ++i) {
            idx = rand.nextInt(81);
            number = numbers[rand.nextInt(numbers.length)];

            // 避免重复位置
            while (map.charAt(idx) != '0') {
                idx = rand.nextInt(81);
            }

            // 避免冲突
            // 或许有可能死循环？
            tmp = 0;
            map.setCharAt(idx, number);
            while (tmp < 10 && checkError(map.toString(), idx)) {
                map.setCharAt(idx, '0');
                number = numbers[rand.nextInt(numbers.length)];
                map.setCharAt(idx, number);
                tmp++;
            }
            // tmp用于防止死循环，此次填数无效化
            if(tmp >= 10) {
                map.setCharAt(idx, '0');
                i--;
            }
        }

        return map.toString();
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
}
