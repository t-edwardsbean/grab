package com.baidu.mongo;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by edwardsbean on 14-11-11.
 */
public class TimeFormatTest {
    //tpTime格式有问题，+08:00应该为+0800
    //T代表后面跟着时间，Z代表UTC统一时间
    String tpTme = "2014-11-11T14:00:00+08:00";
    String pmTime = "2014-11-07T14:00:00Z";

    @Test
    public void testTPTime() throws Exception {
        //2014-11-11T14:00:00+08:00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String time = format.format(new Date());
        System.out.println(time);

    }

    @Test
    public void testPMTime() throws Exception {
        //2014-11-07T14:00:00Z
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = format.format(new Date());
        System.out.println(time);
    }

    //转换回来
    @Test
    public void testParsePMTime() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date time = df.parse(pmTime);
        System.out.println(time);
    }

    //转换回来
    @Test
    public void testParseTPTime() throws Exception {
        //tpTime格式有问题，+08:00应该为+0800
        //所以去掉Z
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date time = df.parse(tpTme);
        System.out.println(time);
    }
}
