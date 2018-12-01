/**
*
* Copyright:   Copyright (c)2016
* Company:     YvesHe
* @version:    1.0
* Create at:   2018年11月30日
* Description:
*
* Author       YvesHe
*/
package com.yveshe.email;

import java.util.Arrays;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;

import com.yveshe.EmailException_;
import com.yveshe.core.log.InfraLogger;

public class EmailEngine {
    private static InfraLogger logger = new InfraLogger(EmailEngine.class);

    private MultiPartEmail_ email = new MultiPartEmail_();
    private final EmailConf conf;
    private final EmailMsgInfo info;

    public EmailEngine(EmailConf conf, EmailMsgInfo info) throws EmailException_ {
        if (conf == null) {
            throw new EmailException_("email conf can't be null.");
        }
        if (info == null) {
            throw new EmailException_("email message info can't be null.");
        }
        this.conf = conf;
        this.info = info;

        try {
            init();
        } catch (org.apache.commons.mail.EmailException e) {
            logger.error("init mail-engine failed", e);
            throw new EmailException_(e.getMessage());
        }
    }

    private EmailAttachment createAttachment(String filePath) {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(filePath);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setName(filePath.substring(filePath.lastIndexOf("/") + 1)); // 附件名称
        attachment.setDescription(filePath.substring(filePath.lastIndexOf("/") + 1));// 无效果
        return attachment;
    }

    private void init() throws org.apache.commons.mail.EmailException {
        email.advanceConf(conf.getExtraProp());

        switch (conf.getSecureConnectType()) {
        case EmailConstants_.AUTHENTICATE_TYPE_DEFAULT:
            break;
        case EmailConstants_.AUTHENTICATE_TYPE_TSL:
            email.setStartTLSEnabled(true);
            break;
        case EmailConstants_.AUTHENTICATE_TYPE_SSL:
            email.setSSLOnConnect(true);
            break;
        default:
            break;
        }

        email.setHostName(conf.getHostName());
        email.setFrom(conf.getMailFrom(), conf.getDefFromName());// 默认发送者昵称
        email.setSubject(info.getSubject());
        email.setMsg(info.getMsg());
        email.setCharset("UTF-8");
        email.setSmtpPort(conf.getSmtpPort());
        email.setAuthenticator(new DefaultAuthenticator(conf.getMailUserName(), conf.getMailPassword()));

        // 增加附件
        if (info.getAttachFilePaths() != null) {
            for (String filePath : info.getAttachFilePaths()) {
                email.attach(createAttachment(filePath));
            }
        }

        // 校验添加联系人
        if (info.getMailTos() != null) {
            for (String mailTo : info.getMailTos()) {
                email.addTo(mailTo);
            }
        }
        email.getToAddresses().clear();
    }

    /**
     * 发送邮件(单独模式)<br>
     * 邮件列表存在错误的邮件地址时,正确的邮件地址照常发送
     *
     * @throws EmailException_
     */
    public void sendOneByOne() throws EmailException_ {
        if (info.getMailTos() != null) {
            for (String mailTo : info.getMailTos()) {
                email = new MultiPartEmail_();
                try {
                    this.init();
                    email.addTo(mailTo);
                    email.send();
                } catch (org.apache.commons.mail.EmailException e) {
                    logger.error("error in send one by one", e);
                    logger.warn("error in send one by one, the mail-to is %s", mailTo);
                }
            }
        }
    }

    /**
     * 发送邮件(批处理模式)<br>
     *
     * 邮件列表存在错误地址,则全部取消发送
     *
     * @throws EmailException_
     */
    public void send() throws EmailException_ {
        try {
            // add mail to
            if (info.getMailTos() != null) {
                for (String mailTo : info.getMailTos()) {
                    email.addTo(mailTo);

                }
            }
            email.send();
        } catch (Exception e) {
            logger.error("error in send mail", e);
            throw new EmailException_(e.getMessage());
        }
    }

    /**
     * 测试邮件(仅根据邮件配置信息测试测试邮件)
     *
     * @param conf
     * @throws EmailException_
     */
    public static void test(EmailConf conf) throws EmailException_ {
        // 构造MessageInfo
        EmailMsgInfo msgInfo = new EmailMsgInfo();
        msgInfo.setMailTos(Arrays.asList(conf.getMailUserName()));
        msgInfo.setMsg("This is a test mail ... :-)");
        msgInfo.setSubject("Test Mail!");

        new EmailEngine(conf, msgInfo).send();
    }

}
