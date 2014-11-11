package com.baidu.mongo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试quartz是否正常调度
 * Created by edwardsbean on 14-11-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:dict.xml","classpath*:quartz.xml"})
public class QuartzTest {
    @Test
    public void testName() throws Exception {
         while (true) {
             Thread.sleep(1000);
         }
    }
}
