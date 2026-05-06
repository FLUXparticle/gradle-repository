package com.example.gradle.library;

import com.example.gradle.tools.*;

public class GradleLibrary {

    public static void run() {
        String msg = MessageTools.emphasize("Hallo Gradle!");
        System.out.println(msg);
    }

}
