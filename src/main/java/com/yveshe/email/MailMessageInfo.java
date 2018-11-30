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
import java.util.List;

public class MailMessageInfo implements Serializable {
    private static final long serialVersionUID = 3192856067259983601L;

    private List<String> mailTos;
    private List<String> attachFilePaths;
    private String subject;
    private String msg;

    public List<String> getMailTos() {
        return mailTos;
    }

    public void setMailTos(List<String> mailTos) {
        this.mailTos = mailTos;
    }

    public List<String> getAttachFilePaths() {
        return attachFilePaths;
    }

    public void setAttachFilePaths(List<String> attachFilePaths) {
        this.attachFilePaths = attachFilePaths;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
