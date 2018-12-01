/**
*
* Copyright:   Copyright (c)2016
* Company:     YvesHe
* @version:    1.0
* Create at:   2018年12月1日
* Description:
*
* Author       YvesHe
*/
package com.yveshe.email;

import java.util.Arrays;

import org.junit.Test;

import com.yveshe.EmailException_;

public class MailEngineTest {

    @Test
    public void testSendOneByOne() throws EmailException_ {
        EmailConf conf = new EmailConf();
        conf.setDefFromName("YvesHe");
        conf.setHostName("smtp.aliyun.com");
        conf.setMailFrom("yveshe@aliyun.com");
        conf.setMailPassword("***************");
        conf.setMailUserName("yveshe@aliyun.com");
        conf.setSecureConnectType(EmailConstants_.AUTHENTICATE_TYPE_SSL);
        conf.setSmtpPort(465);

        EmailMsgInfo info = new EmailMsgInfo();
        info.setAttachFilePaths(Arrays.asList("D://eclipse-workspace//worksapce-book//yves-redis//pom.xml"));// 设置附件地址
        info.setMailTos(Arrays.asList("yveshe@aliyun.com"));
        info.setMsg("This is message!");
        info.setSubject("This is Subject!");

        EmailEngine engine = new EmailEngine(conf, info);
        engine.sendOneByOne();
    }

    @Test
    public void testSend() throws EmailException_ {
        EmailConf conf = new EmailConf();
        conf.setDefFromName("YvesHe");
        conf.setHostName("smtp.aliyun.com");
        conf.setMailFrom("yveshe@aliyun.com");
        conf.setMailPassword("***************");
        conf.setMailUserName("yveshe@aliyun.com");
        conf.setSecureConnectType(EmailConstants_.AUTHENTICATE_TYPE_SSL);
        conf.setSmtpPort(465);

        EmailMsgInfo info = new EmailMsgInfo();
        info.setAttachFilePaths(Arrays.asList("D://eclipse-workspace//worksapce-book//yveshe-mail//pom.xml"));// 设置附件地址
        info.setMailTos(Arrays.asList("yveshe@aliyun.com"));
        info.setMsg("This is message!");
        info.setSubject("This is Subject!");

        EmailEngine engine = new EmailEngine(conf, info);
        engine.send();
    }

    @Test
    public void testTest() throws EmailException_ {
        EmailConf conf = new EmailConf();
        conf.setDefFromName("YvesHe");
        conf.setHostName("smtp.aliyun.com");
        conf.setMailFrom("yveshe@aliyun.com");
        conf.setMailPassword("***************");
        conf.setMailUserName("yveshe@aliyun.com");
        conf.setSecureConnectType(EmailConstants_.AUTHENTICATE_TYPE_SSL);
        conf.setSmtpPort(465);

        EmailEngine.test(conf);
    }

}
