package com.se14;

import com.formdev.flatlaf.FlatLightLaf;
import com.se14.controller.ViewController;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        ViewController controller = new ViewController();
        controller.run();
    }
}
