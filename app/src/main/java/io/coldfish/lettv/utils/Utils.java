package io.coldfish.lettv.utils;

public final class Utils {
    public static String getYearFromAirDate(String firstAirDate) {
        return firstAirDate.substring(0, 4);
    }
}
