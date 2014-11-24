LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := libhello
LOCAL_SRC_FILES := com_dmi_gradle_ndkclassic_sample_HelloActivity.cpp
include $(BUILD_SHARED_LIBRARY)
