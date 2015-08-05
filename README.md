android-ndk-classic gradle plugin
===================

This plugin disables automatic generation of Android.mk.
Instead, use src\main\jni\Android.mk and src\main\jni\Application.mk

Add the following to your build script to use the plugin:

    buildscript {
        repositories {
            maven {
                jcenter()
            }
        }
        dependencies {
            classpath 'com.android.tools.build:gradle:1.3.0'
            classpath 'com.dmi.gradle:android-ndk-classic:0.3.0'
        }
    }

    apply plugin: 'com.android.application'
    apply plugin: 'android-ndk-classic'
