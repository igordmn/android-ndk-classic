#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL Java_com_dmi_gradle_ndkclassic_sample_HelloUniverse_getMessage
        (JNIEnv *env, jclass) {
    return env->NewStringUTF("Hello, Universe!");
}
