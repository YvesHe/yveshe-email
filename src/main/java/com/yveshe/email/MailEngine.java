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
import org.apache.commons.mail.EmailConstants;

import com.yveshe.EmailException;
import com.yveshe.core.log.InfraLogger;

public class MailEngine {
    private static InfraLogger logger = new InfraLogger(MailEngine.class);

    public static final String[] NONSUPPORT_PROPERTIES = new String[] { EmailConstants.MAIL_HOST, EmailConstants.MAIL_PORT, EmailConstants.MAIL_SMTP_TIMEOUT };
    public static final int AUTHENTICATE_TYPE_DEFAULT = 0;
    public static final int AUTHENTICATE_TYPE_TSL = 1;
    public static final int AUTHENTICATE_TYPE_SSL = 2;

    private BESMultiPartMail email = new BESMultiPartMail();
    private final MailConf conf;
    private final MailMessageInfo info;

    public MailEngine(MailConf conf, MailMessageInfo info) throws EmailException {
        if (conf == null) {
            throw new EmailException("email conf can't be null.");
        }
        if (info == null) {
            throw new EmailException("email message info can't be null.");
        }
        this.conf = conf;
        this.info = info;
        init();
    }

    private EmailAttachment createAttachment(String filePath) {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(filePath);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setName(filePath.substring(filePath.lastIndexOf("/") + 1)); // 附件名称
        attachment.setDescription(filePath.substring(filePath.lastIndexOf("/") + 1));// 无效果
        return attachment;
    }

    private void init() throws EmailException {
        email.advanceConf(conf.getExtraProp());

        try {
            switch (conf.getSecureConnectType()) {
            case AUTHENTICATE_TYPE_DEFAULT:
                break;
            case AUTHENTICATE_TYPE_TSL:
                email.setStartTLSEnabled(true);
                break;
            case AUTHENTICATE_TYPE_SSL:
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
        } catch (Exception e) {
            throw new EmailException(e.getMessage());
        }
    }

    /**
     * 发送邮件(单独模式)<br>
     * 邮件列表存在错误的邮件地址时,正确的邮件地址照常发送
     *
     * @throws EmailException
     */
    public void sendOneByOne() throws EmailException {
        if (info.getMailTos() != null) {
            for (String mailTo : info.getMailTos()) {
                email = new BESMultiPartMail();
                this.init();

                try {
                    email.addTo(mailTo);
                    email.send();
                } catch (Exception e) {
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
     * @throws EmailException
     */
    public void send() throws EmailException {
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
            throw new EmailException(e.getMessage());
        }
    }

    /**
     * 测试邮件(仅根据邮件配置信息测试测试邮件)
     *
     * @param conf
     * @throws EmailException
     */
    public static void test(MailConf conf) throws EmailException {
        // 构造MessageInfo
        MailMessageInfo msgInfo = new MailMessageInfo();
        msgInfo.setMailTos(Arrays.asList(conf.getMailUserName()));
        msgInfo.setMsg("This is a test mail ... :-)");
        msgInfo.setSubject("Test Mail!");

        new MailEngine(conf, msgInfo).send();
    }

}
