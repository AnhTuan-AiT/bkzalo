package com.hust.bkzalo.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final Pattern phoneNumberRegex = Pattern.compile("(([03+[2-9]|05+[6|8|9]|07+[0|6|7|8|9]|08+[1-9]|09+[1-4|6-9]]){3})+[0-9]{7}");

    public static boolean isValid(String phoneNumber) {
        if (StringUtils.isNumeric(phoneNumber) && phoneNumber.length() == 10) {
            Matcher matcher = phoneNumberRegex.matcher(phoneNumber);
            return matcher.matches();
        }

        return false;
    }

    public static boolean isValidPassword(String password) {
        if (password.length() > 5 && password.length() < 11 && StringUtils.isAlphanumeric(password)) {
//            Pattern letter = Pattern.compile("[a-zA-z]");
//            Pattern digit = Pattern.compile("[0-9]");
//            Matcher hasLetter = letter.matcher(password);
//            Matcher hasDigit = digit.matcher(password);

            Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
            Matcher hasSpecial = special.matcher(password);

            return !hasSpecial.find();
        }

        return false;
    }
}
