package com.example.weatherclock.util;

public class UIUpdateNotifier {

    private static UIUpdateNotifier instance;


    public UIUpdateNotifier() {
    }

    public static UIUpdateNotifier getInstance() {
        if (instance == null) {
            instance = new UIUpdateNotifier();
        }
        return instance;
    }
}
