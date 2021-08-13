package com.whyat.test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class EncodingTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "ab";
        byte[] b_unicode = s.getBytes("unicode");
        byte[] b_utf8 = s.getBytes(StandardCharsets.UTF_8);
        byte[] b_iso = s.getBytes(StandardCharsets.ISO_8859_1);

        String unicode = new String(b_unicode, "unicode");
        String utf8 = new String(b_utf8, "utf-8");
        String iso = new String(b_unicode, StandardCharsets.ISO_8859_1);

        System.out.println("...");
    }
}
