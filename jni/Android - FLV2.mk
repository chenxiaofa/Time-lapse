LOCAL_PATH:=$(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional

#this is the target being built
LOCAL_MODULE:= libX264

LOCAL_CFLAGS := -std=c99

#All of the source files that we will compile
LOCAL_SRC_FILES:= x264.c common/mc.c common/predict.c common/pixel.c common/macroblock.c \
       common/frame.c common/dct.c common/cpu.c common/cabac.c \
       common/common.c common/osdep.c common/rectangle.c \
       common/set.c common/quant.c common/deblock.c common/vlc.c \
       common/mvpred.c common/bitstream.c common/threadpool.c\
       encoder/analyse.c encoder/me.c encoder/ratecontrol.c \
       encoder/set.c encoder/macroblock.c encoder/cabac.c \
       encoder/cavlc.c encoder/encoder.c encoder/lookahead.c input/input.c input/timecode.c input/raw.c input/y4m.c \
         output/raw.c  output/matroska.c output/matroska_ebml.c \
          output/flv_bytestream.c filters/filters.c \
         filters/video/video.c filters/video/source.c filters/video/internal.c \
         filters/video/resize.c filters/video/cache.c filters/video/fix_vfr_pts.c \
         filters/video/select_every.c filters/video/crop.c filters/video/depth.c \
           input/thread.c

#All of the shared libraries we link against
LOCAL_SHARED_LIBRARIES :=


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
LOCAL_LDLIBS := -llog

LOCAL_PRELINK_MODULE := false
include $(BUILD_STATIC_LIBRARY)
######################################################################
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional

#this is the target being built
LOCAL_MODULE:= libEncoder

LOCAL_CFLAGS := -std=c99

#All of the source files that we will compile
LOCAL_SRC_FILES:= h264encoder.c output/flv.c

LOCAL_STATIC_LIBRARIES := libX264


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
LOCAL_LDLIBS := -llog

LOCAL_PRELINK_MODULE := false
include $(BUILD_SHARED_LIBRARY)
