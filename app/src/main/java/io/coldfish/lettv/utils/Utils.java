package io.coldfish.lettv.utils;

public final class Utils {
    private static final String NumberPattern = "[0-9]+";

    public static String getYearFromAirDate(String firstAirDate) {
        if (firstAirDate == null || firstAirDate.length() == 0)
            return "";
        String substring = firstAirDate.substring(0, 4);
        return substring.matches(NumberPattern) ? substring : "";
    }
}
