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

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * 邮件配置信息
 *
 * @author YvesHe
 *
 */
public class MailConf implements Serializable {
    private static final long serialVersionUID = -4351683275570056467L;

    private String hostName;
    private int smtpPort;
    private String mailFrom;
    private int secureConnectType; // STMP
    private String mailUserName;
    private String mailPassword;
    private String defFromName;// 默认发送人昵称，可有可无
    private final LinkedHashMap<String, String> extraProp = new LinkedHashMap<String, String>();

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public int getSecureConnectType() {
        return secureConnectType;
    }

    public void setSecureConnectType(int secureConnectType) {
        this.secureConnectType = secureConnectType;
    }

    public String getMailUserName() {
        return mailUserName;
    }

    public void setMailUserName(String mailUserName) {
        this.mailUserName = mailUserName;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    public String getDefFromName() {
        return defFromName;
    }

    public void setDefFromName(String defFromName) {
        this.defFromName = defFromName;
    }

    public LinkedHashMap<String, String> getExtraProp() {
        return extraProp;
    }

}
