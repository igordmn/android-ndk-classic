package com.dmi.gradle.ndkclassic

import com.android.annotations.Nullable
import com.android.build.gradle.internal.tasks.BaseTask
import com.android.ide.common.internal.CommandLineRunner
import com.google.common.collect.Lists
import org.gradle.api.GradleException
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.*
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

import static com.android.SdkConstants.CURRENT_PLATFORM
import static com.android.SdkConstants.PLATFORM_WINDOWS

class NdkClassicCompile extends BaseTask {
    List<File> sourceFolders

    @Input
    File applicationMk

    @Input
    File androidMk

    @Input
    boolean debuggable

    @OutputDirectory
    File soFolder

    @OutputDirectory
    File objFolder

    @Input
    boolean ndkCygwinMode

    @InputFiles
    FileTree getSource() {
        return getProject().files(new ArrayList<Object>(getSourceFolders())).getAsFileTree()
    }

    @TaskAction
    void taskAction(IncrementalTaskInputs inputs) {
        File ndkFolder = getNdkFolder()

        List<String> commands = Lists.newArrayList()
        String exe = ndkFolder.absolutePath + File.separator + "ndk-build"
        if (CURRENT_PLATFORM == PLATFORM_WINDOWS && !ndkCygwinMode) {
            exe += ".cmd"
        }
        commands.add(exe)
        commands.add("NDK_PROJECT_PATH=null")
        if (getApplicationMk().exists()) {
            commands.add("NDK_APPLICATION_MK=" + getApplicationMk().absolutePath)
        }
        commands.add("APP_BUILD_SCRIPT=" + getAndroidMk().absolutePath)
        commands.add("NDK_OUT=" + getObjFolder().absolutePath)
        commands.add("NDK_LIBS_OUT=" + getSoFolder().absolutePath)
        if (getDebuggable()) {
            commands.add("NDK_DEBUG=1")
        }
        CommandLineRunner.CommandLineOutput commandLineOutput = new CommandLineRunner.CommandLineOutput() {
            @Override
            void out(@Nullable String line) {
                println line
            }
        }
        getBuilder().commandLineRunner.runCmdLine(commands, commandLineOutput, null)
    }

    private File getNdkFolder() {
        File ndkFolder = getPlugin().ndkFolder
        if (ndkFolder == null || !ndkFolder.isDirectory()) {
            throw new GradleException(
                    "NDK not configured.\n" +
                            "Download the NDK from http://developer.android.com/tools/sdk/ndk/." +
                            "Then add ndk.dir=path/to/ndk in local.properties.\n" +
                            "(On Windows, make sure you escape backslashes, e.g. C:\\\\ndk rather than C:\\ndk)");
        }
        return ndkFolder
    }
}