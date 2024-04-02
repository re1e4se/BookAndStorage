package com.sunrisenw.bookandstorage.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexValidator {
    public static boolean isValidHex(String str) {
        final Pattern HEXADECIMAL_PATTERN = Pattern.compile("\\p{XDigit}+");

        final Matcher matcher = HEXADECIMAL_PATTERN.matcher(str);
        return matcher.matches();
    }
}