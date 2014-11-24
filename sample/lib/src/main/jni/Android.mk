LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := libhellouniverse
LOCAL_SRC_FILES := com_dmi_gradle_ndkclassic_sample_HelloUniverse.cpp
include $(BUILD_SHARED_LIBRARY)
