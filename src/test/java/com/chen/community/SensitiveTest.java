package com.chen.community;

import com.chen.community.util.SensitiveFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description 敏感词过滤测试
 * @date 2023-08-07 18:14
 **/
@Slf4j
public class SensitiveTest extends CommunityApplicationTests{
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter(){
        String text = "这里可以赌博，可以吸毒，可以嫖娼，可以开票,hhhfabc";
        text = sensitiveFilter.filter(text);
        log.info("过滤后结果={}",text);

        text = "这里可以性☆赌☆博☆，可以☆吸☆毒☆，可以☆嫖☆娼☆，可以开☆票,哈哈哈哈！";
        text = sensitiveFilter.filter(text);
        log.info("过滤后结果={}",text);
    }
}
