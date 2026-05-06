package com.example.gradle.project;

import com.example.gradle.library.*;
import com.example.gradle.tools.*;

public class GradleProject {

    public static void main(String[] args) {
        Message message = GradleLibrary.createMessage();
        System.out.println(message.text() + " und aus dem Hauptprogramm");
    }

}
