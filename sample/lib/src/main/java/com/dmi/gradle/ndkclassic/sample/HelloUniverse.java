package com.dmi.gradle.ndkclassic.sample;

public abstract class HelloUniverse {
    static {
        System.loadLibrary("hellouniverse");
    }

    public static native String getMessage();
}
