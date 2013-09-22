LOCAL_PATH:=$(call my-dir)


include $(CLEAR_VARS) 
LOCAL_MODULE := x264
LOCAL_SRC_FILES := libx264.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS) 
LOCAL_MODULE := gpac
LOCAL_SRC_FILES := libgpac.a
include $(PREBUILT_STATIC_LIBRARY)

######################################################################
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional

#this is the target being built
LOCAL_MODULE:= libEncoder

LOCAL_CFLAGS := -std=c99

#All of the source files that we will compile
LOCAL_SRC_FILES:= h264encoder.c output/mp4.c

LOCAL_STATIC_LIBRARIES := libx264 libgpac


#Also need the JNI Headers.
LOCAL_C_INCLUDES += \
$(JNI_H_INCLUDES)\
$(LOCAL_PATH)/common/\
$(LOCAL_PATH)/encoder/\
$(LOCAL_PATH)/filters/\
$(LOCAL_PATH)/filters/video/\
$(LOCAL_PATH)/ \
$(LOCAL_PATH)/gpac/include/ \
$(LOCAL_PATH)/gpac/

#link libs (ex logs)
LOCAL_LDLIBS := -llog -lz

LOCAL_PRELINK_MODULE := false
include $(BUILD_SHARED_LIBRARY)
