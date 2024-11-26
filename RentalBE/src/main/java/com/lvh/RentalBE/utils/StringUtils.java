package com.lvh.RentalBE.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {

    public static String removeAccents(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replaceAll("đ", "d").replaceAll("Đ", "D");
    }
}
