package com.web.springmvc.web_tin_tuc.utils;

public class EmailUtils {
    public static String getEmailMessage(String name, String host, String token) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click the link below to verify your account. \n\n" +
                getVerificationUrl(host, token) + "\n\nThe support Team";
    }

    public static String getVerificationUrl(String host, String token) {
        return host + "/auth/register/confirm?token=" + token;
    }

    public static String getResetPasswordUrl(String host, String token, Integer id) {
        return host + "/auth/reset-password/"+id.toString()+"?token="+token;
    }
}
