package com.chen.community;

import com.chen.community.util.MailClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @Description 发送邮件测试
 * @date 2023-07-30 21:33
 **/
public class MailTest extends CommunityApplicationTests{
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void testTextMail(){
        mailClient.sendMail("1220305066@qq.com","TEST","hahahahahaha.");
    }
    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username","Sunday");

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMail("1220305066@qq.com","html",content);
    }
}
