package com.dmi.gradle.ndkclassic

import com.android.annotations.Nullable
import com.android.ide.common.internal.CommandLineRunner
import com.android.build.gradle.internal.LoggerWrapper;
import com.android.utils.ILogger;
import com.google.common.collect.Lists
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.*
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

import static com.android.SdkConstants.CURRENT_PLATFORM
import static com.android.SdkConstants.PLATFORM_WINDOWS

class NdkClassicCompile extends DefaultTask {
    private ILogger logger = new LoggerWrapper(getLogger())

    File applicationMk

    File androidMk

    boolean debuggable

    File soFolder

    File objFolder

    File ndkDirectory

    @TaskAction
    void taskAction() {
        checkNdkDirectory()

        List<String> commands = Lists.newArrayList()
        String exe = ndkDirectory.absolutePath + File.separator + "ndk-build"
        if (CURRENT_PLATFORM == PLATFORM_WINDOWS) {
            exe += ".cmd"
        }
        commands.add(exe)
        commands.add("NDK_PROJECT_PATH=null")
        if (getApplicationMk().exists()) {
            commands.add("NDK_APPLICATION_MK=" + applicationMk.absolutePath)
        }
        commands.add("APP_BUILD_SCRIPT=" + androidMk.absolutePath)
        commands.add("NDK_OUT=" + objFolder.absolutePath)
        commands.add("NDK_LIBS_OUT=" + soFolder.absolutePath)
        if (debuggable) {
            commands.add("NDK_DEBUG=1")
        }
        if (project.ndkClassic.additionalArguments) {
            commands.add(project.ndkClassic.additionalArguments)
        }

        CommandLineRunner.CommandLineOutput commandLineOutput = new CommandLineRunner.CommandLineOutput() {
            @Override
            void out(@Nullable String line) {
                println line
            }
        }
        new CommandLineRunner(logger).runCmdLine(commands, commandLineOutput, null)
    }

    private void checkNdkDirectory() {
        if (ndkDirectory == null || !ndkDirectory.isDirectory()) {
            throw new GradleException(
                    "NDK not configured.\n" +
                            "Download the NDK from http://developer.android.com/tools/sdk/ndk/." +
                            "Then add ndk.dir=path/to/ndk in local.properties.\n" +
                            "(On Windows, make sure you escape backslashes, e.g. C:\\\\ndk rather than C:\\ndk)");
        }
    }
}