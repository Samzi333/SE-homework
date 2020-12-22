package com.jnu.sudokudos.dataprocess;

import junit.framework.TestCase;

import org.junit.Assert;

import java.util.Random;

public class MapToolTest extends TestCase {

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
}