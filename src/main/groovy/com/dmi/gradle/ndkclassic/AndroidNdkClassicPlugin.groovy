package com.dmi.gradle.ndkclassic

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.tasks.NdkCompile

import static com.android.builder.model.AndroidProject.FD_INTERMEDIATES

class AndroidNdkClassicPlugin implements Plugin<Project> {
    void apply(Project project) {
        if (project.android == null) {
            throw new IllegalStateException("android-ndk-classic: plugin needs android plugin")
        }

        project.afterEvaluate {
            def jni = project.android.sourceSets.main.jni
            if (jni.srcDirs.size() > 1) {
                throw new IllegalStateException("android-ndk-classic: only support one dir in jniDirs")
            } else if (jni.srcDirs.size() == 1) {
                def jniDir = jni.srcDirs.toList().first()

                def variants = project.android instanceof LibraryExtension ?
                    project.android.libraryVariants :
                    project.android.applicationVariants;

                variants.all { variant ->
                    def variantData = variant.variantData
                    def ndkCompile = variantData.ndkCompileTask
                    def variantConfig = variantData.variantConfiguration
                    
                    NdkClassicCompile ndkExtCompile = project.tasks.create(
                            "compile${variantData.variantConfiguration.fullName.capitalize()}NdkClassic",
                            NdkClassicCompile)
                    ndkExtCompile.dependsOn variantData.preBuildTask
                    ndkExtCompile.plugin = ndkCompile.plugin
                    ndkExtCompile.conventionMapping.sourceFolders = {
                        return variantConfig.jniSourceList
                    }
                    ndkExtCompile.conventionMapping.applicationMk = {
                        new File(jniDir, "Application.mk")
                    }
                    ndkExtCompile.conventionMapping.androidMk = {
                        new File(jniDir, "Android.mk")
                    }
                    ndkExtCompile.conventionMapping.debuggable = {
                        variantConfig.buildType.jniDebuggable
                    }
                    ndkExtCompile.conventionMapping.objFolder = {
                        project.file("$project.buildDir/${FD_INTERMEDIATES}/ndk/${variantConfig.dirName}/obj")
                    }
                    ndkExtCompile.conventionMapping.soFolder = {
                        project.file("$project.buildDir/${FD_INTERMEDIATES}/ndk/${variantConfig.dirName}/lib")
                    }
                    variantData.javaCompileTask.dependsOn ndkExtCompile
                }

                project.tasks.withType(NdkCompile) {
                    compileTask -> compileTask.enabled = false
                }
            }
        }
    }
}