package com.alanjz.arimaa.app;

public class AZApp {

    private final AZArimaaBoardFrame boardFrame;

    public AZApp() {
        boardFrame = new AZArimaaBoardFrame("new game");
    }

    public static void main(String[] args) {
        new AZApp();
    }
}