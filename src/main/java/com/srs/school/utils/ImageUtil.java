package com.srs.school.utils;

import org.springframework.core.io.ClassPathResource;

import java.util.Base64;

public class ImageUtil {

    public static String getBase64FromStatic(String path) {
        try {
            ClassPathResource resource = new ClassPathResource("static" + path);
            byte[] bytes = resource.getInputStream().readAllBytes();

            return "data:image/png;base64," +
                    Base64.getEncoder().encodeToString(bytes);

        } catch (Exception e) {
            throw new RuntimeException("Error loading image", e);
        }
    }
}