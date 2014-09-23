package com.dmi.gradle.ndkclassic

import org.gradle.api.Plugin
import org.gradle.api.Project

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

                project.android.applicationVariants.all { variant ->
                    def variantData = variant.variantData
                    def ndkCompile = variantData.ndkCompileTask
                    def variantConfig = variantData.variantConfiguration

                    for (def output : variantData.outputs) {
                        def packageApplication = output.packageApplicationTask
                        packageApplication.dependsOn =
                            packageApplication.taskDependencies.values - ndkCompile
                    }
                   

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
                        variantConfig.buildType.jniDebugBuild
                    }
                    ndkExtCompile.conventionMapping.objFolder = {
                        project.file("$project.buildDir/${FD_INTERMEDIATES}/ndk/${variantConfig.dirName}/obj")
                    }
                    ndkExtCompile.conventionMapping.soFolder = {
                        project.file("$project.buildDir/${FD_INTERMEDIATES}/ndk/${variantConfig.dirName}/lib")
                    }
                    variantData.javaCompileTask.dependsOn ndkExtCompile
                }
            }
        }
    }
}