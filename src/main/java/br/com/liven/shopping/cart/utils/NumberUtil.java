package br.com.liven.shopping.cart.utils;

public class NumberUtil {



    public static boolean hasElevenDigits(long number) {
        return String.valueOf(Math.abs(number)).length() == 11;
    }

}
