package com.juaracoding.TA.handler;

import com.juaracoding.TA.utils.ConstantMessage;

import java.util.regex.Pattern;

public class FormatValidation {

    public static Boolean emailFormatValidation(String email,String writeNull)
    {
        //^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$
        if(!(Pattern.compile(ConstantMessage.REGEX_EMAIL_STANDARD_RFC5322)
                .matcher(email)
                .matches()))
        {
            return false;
        }
        return true;
    }

    public static Boolean phoneNumberFormatValidation(String phoneNumner,String writeNull)
    {
        if(!(Pattern.compile(ConstantMessage.REGEX_PHONE)
                .matcher(phoneNumner)
                .matches()))
        {
            return false;
        }
        return true;
    }

    public static Boolean dateFormatYYYYMMDDValidation(String date, String writeNull)
    {
        if(!(Pattern.compile(ConstantMessage.REGEX_DATE_YYYYMMDD)
                .matcher(date)
                .matches()))
        {
            return false;
        }
        return true;
    }
}
