package com.jnu.sudokudos.dataprocess;

import junit.framework.TestCase;

import org.junit.Assert;
import com.jnu.sudokudos.BuildConfig;

import java.util.Random;

import static com.jnu.sudokudos.dataprocess.MapTool.isMap;

public class MapToolTest extends TestCase {

    private String testMap =
            "213456789"+"526789123"+"789123456";
    private String diffs[] = {"s", "m", "h"};

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    public void testIsMap() {
        String str=new String();
        java.util.Random random =new Random();
        int a=100;
        while(a>0)
        {
            for (int i = 0; i < 81; i++) {
                str += String.valueOf(random.nextInt(9));
            }

            Assert.assertEquals(true, MapTool.isMap(str));
            a--;
            str=new String();

        }
    }

    public void testGetDiff() {
        String str=new String();
        int i=0;
        java.util.Random random =new Random();
        int num=random.nextInt(81);
        for(i=0;i<num;i++)
            str+="0";
        for(i=num;i<81;i++)
            str+="1";

        if(num > 65)
            Assert.assertEquals("s", MapTool.getDiff(str));
        else if(num > 40)
            Assert.assertEquals("m", MapTool.getDiff(str));
        else
            Assert.assertEquals("h", MapTool.getDiff(str));
    }

    public void testGenMap() {
        String map;
        for(String i : diffs){
            map = MapTool.genMap(i);
            if (BuildConfig.DEBUG && !isMap(map)) {
                throw new AssertionError("生成了非法题目");
            }
        }
    }

    public void testCheckSection() {
        MapTool.checkSection(testMap,1,1);
    }
}