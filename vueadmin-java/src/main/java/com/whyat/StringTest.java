package com.whyat;

import java.nio.charset.StandardCharsets;

/**
 * TODO
 *
 * @Author Whyat
 * @Date 2021/8/12 17:43
 */
public class StringTest {
    public static void main(String[] args) {
        char a = '莫';
        System.out.println("🀎".length());
        System.out.println("🀎".getBytes(StandardCharsets.UTF_8));
        System.out.println("🀎".charAt(0));
        System.out.println("🀎".charAt(1));
    }
}
