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

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class BESMultiPartMail extends MultiPartEmail {

    private final Properties props = new Properties();

    public void advanceConf(Map<String, String> conf) {
        // 防止附件名称过长,导致解析错误
        System.setProperty("mail.mime.splitlongparameters", "false");
        props.putAll(System.getProperties());
        for (Entry<String, String> entry : conf.entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Session getMailSession() throws EmailException {
        Session result = null;
        try {
            Field declaredField = Email.class.getDeclaredField("session");
            declaredField.setAccessible(true);
            Object object = declaredField.get(this);

            if (declaredField.get(this) != null) {
                declaredField.setAccessible(false);
                result = (Session) object;
            } else {
                declaredField.set(this, result);
            }
            declaredField.setAccessible(false);
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException("EamilEngineException: relect the  ession field failed!");
        }

        // copy from Email.java
        if (StringUtils.isEmpty(this.hostName)) {
            this.hostName = props.getProperty(EmailConstants.MAIL_HOST);
        }
        if (StringUtils.isEmpty(this.hostName)) {
            throw new EmailException("Cannot find valid hostname for mail session");
        }

        props.setProperty(EmailConstants.MAIL_PORT, this.smtpPort);
        props.setProperty(EmailConstants.MAIL_HOST, this.hostName);
        // props.setProperty(EmailConstants.MAIL_DEBUG,
        // String.valueOf(this.debug));

        props.setProperty(EmailConstants.MAIL_TRANSPORT_STARTTLS_ENABLE,
            isStartTLSEnabled() ? "true" : "false");
        props.setProperty(EmailConstants.MAIL_TRANSPORT_STARTTLS_REQUIRED,
            isStartTLSRequired() ? "true" : "false");

        props.setProperty(EmailConstants.MAIL_SMTP_SEND_PARTIAL,
            isSendPartial() ? "true" : "false");
        props.setProperty(EmailConstants.MAIL_SMTPS_SEND_PARTIAL,
            isSendPartial() ? "true" : "false");

        if (this.authenticator != null) {
            props.setProperty(EmailConstants.MAIL_SMTP_AUTH, "true");
        }

        if (isSSLOnConnect()) {
            props.setProperty(EmailConstants.MAIL_PORT, this.sslSmtpPort);
            props.setProperty(EmailConstants.MAIL_SMTP_SOCKET_FACTORY_PORT, this.sslSmtpPort);
            props.setProperty(EmailConstants.MAIL_SMTP_SOCKET_FACTORY_CLASS, "javax.net.ssl.SSLSocketFactory");
            props.setProperty(EmailConstants.MAIL_SMTP_SOCKET_FACTORY_FALLBACK, "false");
        }

        if ((isSSLOnConnect() || isStartTLSEnabled()) && isSSLCheckServerIdentity()) {
            props.setProperty(EmailConstants.MAIL_SMTP_SSL_CHECKSERVERIDENTITY, "true");
        }

        if (this.bounceAddress != null) {
            props.setProperty(EmailConstants.MAIL_SMTP_FROM, this.bounceAddress);
        }

        if (this.socketTimeout > 0) {
            props.setProperty(EmailConstants.MAIL_SMTP_TIMEOUT, Integer.toString(this.socketTimeout));
        }

        if (this.socketConnectionTimeout > 0) {
            props.setProperty(EmailConstants.MAIL_SMTP_CONNECTIONTIMEOUT, Integer.toString(this.socketConnectionTimeout));
        }

        result = Session.getInstance(props, this.authenticator);
        return result;
    }
}