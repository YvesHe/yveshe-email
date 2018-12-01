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

import org.apache.commons.mail.EmailConstants;

public final class EmailConstants_ {
    private EmailConstants_() {
    }

    public static final String[] NONSUPPORT_PROPERTIES = new String[] { EmailConstants.MAIL_HOST, EmailConstants.MAIL_PORT, EmailConstants.MAIL_SMTP_TIMEOUT };
    public static final int AUTHENTICATE_TYPE_DEFAULT = 0;
    public static final int AUTHENTICATE_TYPE_TSL = 1;
    public static final int AUTHENTICATE_TYPE_SSL = 2;
}
