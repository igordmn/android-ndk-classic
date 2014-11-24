#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL Java_com_dmi_gradle_ndkclassic_sample_HelloActivity_getMessage
        (JNIEnv *env, jobject) {
    return env->NewStringUTF("Hello, World!");
}
