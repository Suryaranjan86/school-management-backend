package com.srs.school.utils;

public class NumberToWords {

    static String[] units = {"", "One", "Two", "Three", "Four", "Five",
            "Six", "Seven", "Eight", "Nine", "Ten", "Eleven",
            "Twelve", "Thirteen", "Fourteen", "Fifteen"};

    static String[] tens = {"", "", "Twenty", "Thirty", "Forty", "Fifty"};

    public static String convert(long n) {
        if (n == 0) return "Zero";

        if (n < 16) return units[(int) n];

        if (n < 100)
            return tens[(int) n / 10] + " " + units[(int) n % 10];

        if (n < 1000)
            return units[(int) n / 100] + " Hundred " + convert(n % 100);

        if (n < 100000)
            return convert(n / 1000) + " Thousand " + convert(n % 1000);

        return n + "";
    }
}