
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <x264.h>
#include "common.h"
#include "avcodec.h"

#define DATA_MAX 3000000
#define H264_MTU 1024
typedef struct
{
struct AVCodec *codec;			  // Codec
struct AVCodecContext *c;		  // Codec Context
struct AVFrame *picture;		  // Frame
} Decoder;


typedef struct
{
	x264_param_t * param;
    x264_t *handle;
    x264_picture_t * picture;
    x264_nal_t  *nal;
} Encoder;
	
int *colortab=NULL;
int *u_b_tab=NULL;
int *u_g_tab=NULL;
int *v_g_tab=NULL;
int *v_r_tab=NULL;


unsigned int *rgb_2_pix=NULL;
unsigned int *r_2_pix=NULL;
unsigned int *g_2_pix=NULL;
unsigned int *b_2_pix=NULL;
int g_bInitTab=0;
void DeleteYUVTab()
{

	if (g_bInitTab)
	{
		av_free(colortab);
		av_free(rgb_2_pix);
		g_bInitTab=0;
	}
}

void CreateYUVTab_16()
{
	int i;
	int u, v;

	if (g_bInitTab)
		return;
	colortab = (int *)av_malloc(4*256*sizeof(int));
	u_b_tab = &colortab[0*256];
	u_g_tab = &colortab[1*256];
	v_g_tab = &colortab[2*256];
	v_r_tab = &colortab[3*256];

	for (i=0; i<256; i++)
	{
		u = v = (i-128);

		u_b_tab[i] = (int) ( 1.772 * u);
		u_g_tab[i] = (int) ( 0.34414 * u);
		v_g_tab[i] = (int) ( 0.71414 * v); 
		v_r_tab[i] = (int) ( 1.402 * v);
	}

	rgb_2_pix = (unsigned int *)av_malloc(3*768*sizeof(unsigned int));

	r_2_pix = &rgb_2_pix[0*768];
	g_2_pix = &rgb_2_pix[1*768];
	b_2_pix = &rgb_2_pix[2*768];

	for(i=0; i<256; i++)
	{
		r_2_pix[i] = 0;
		g_2_pix[i] = 0;
		b_2_pix[i] = 0;
	}

	for(i=0; i<256; i++)
	{
		r_2_pix[i+256] = (i & 0xF8) << 8;
		g_2_pix[i+256] = (i & 0xFC) << 3;
		b_2_pix[i+256] = (i ) >> 3;
	}

	for(i=0; i<256; i++)
	{
		r_2_pix[i+512] = 0xF8 << 8;
		g_2_pix[i+512] = 0xFC << 3;
		b_2_pix[i+512] = 0x1F;
	}

	r_2_pix += 256;
	g_2_pix += 256;
	b_2_pix += 256;
	g_bInitTab=1;
}

void DisplayYUV_16(unsigned int *pdst1, unsigned char *y, unsigned char *u, unsigned char *v, int width, int height, int src_ystride, int src_uvstride, int dst_ystride)
{
	int i, j;
	int r, g, b, rgb;

	int yy, ub, ug, vg, vr;

	unsigned char* yoff;
	unsigned char* uoff;
	unsigned char* voff;
	
	unsigned int* pdst=pdst1;

	int width2 = width/2;
	int height2 = height/2;



	for(j=0; j<height2; j++) // 一次2x2共四个像素
	{
		yoff = y + j * 2 * src_ystride;
		uoff = u + j * src_uvstride;
		voff = v + j * src_uvstride;

		for(i=0; i<width2; i++)
		{
			yy  = *(yoff+(i<<1));
			ub = u_b_tab[*(uoff+i)];
			ug = u_g_tab[*(uoff+i)];
			vg = v_g_tab[*(voff+i)];
			vr = v_r_tab[*(voff+i)];

			b = yy + ub;
			g = yy - ug - vg;
			r = yy + vr;

			rgb = r_2_pix[r] + g_2_pix[g] + b_2_pix[b];

			yy = *(yoff+(i<<1)+1);
			b = yy + ub;
			g = yy - ug - vg;
			r = yy + vr;

			pdst[(j*dst_ystride+i)] = (rgb)+((r_2_pix[r] + g_2_pix[g] + b_2_pix[b])<<16);

			yy = *(yoff+(i<<1)+src_ystride);
			b = yy + ub;
			g = yy - ug - vg;
			r = yy + vr;

			rgb = r_2_pix[r] + g_2_pix[g] + b_2_pix[b];

			yy = *(yoff+(i<<1)+src_ystride+1);
			b = yy + ub;
			g = yy - ug - vg;
			r = yy + vr;

			pdst [((2*j+1)*dst_ystride+i*2)>>1] = (rgb)+((r_2_pix[r] + g_2_pix[g] + b_2_pix[b])<<16);
		}
	}
}

//====================================================

jlong Java_h264_com_H264Encoder_CompressBegin(JNIEnv* env, jobject thiz,
		jint width, jint height) {
	Encoder * en = (Encoder *) malloc(sizeof(Encoder));
	en->param = (x264_param_t *) malloc(sizeof(x264_param_t));
	en->picture = (x264_param_t *) malloc(sizeof(x264_picture_t));
	x264_param_default(en->param); //set default param
	x264_param_apply_profile(en->param,"baseline");
	//en->param->rc.i_rc_method = X264_RC_CQP;
	en->param->i_log_level = X264_LOG_NONE;
	en->param->i_width = width; //set frame width
	en->param->i_height = height; //set frame height
	en->param->rc.i_lookahead =0;

	en->param->i_fps_num =5;
	en->param->i_fps_den = 1;
	if ((en->handle = x264_encoder_open(en->param)) == 0) {
		return 0;
	}
	/* Create a new pic */
	x264_picture_alloc(en->picture, X264_CSP_I420, en->param->i_width,
			en->param->i_height);
	return (jlong) en;
}

jint Java_h264_com_H264Encoder_CompressEnd(JNIEnv* env, jobject thiz,jlong handle)
{
	Encoder * en = (Encoder *) handle;
	if(en->picture)
	{
		x264_picture_clean(en->picture);
		free(en->picture);
		en->picture	= 0;
	}
	if(en->param)
	{
		free(en->param);
		en->param=0;
	}
	if(en->handle)
	{
		x264_encoder_close(en->handle);
	}
	free(en);
	return 0;
}
jint Java_h264_com_H264Encoder_CompressBuffer(JNIEnv* env, jobject thiz,jlong handle,jint type,jbyteArray in, jint insize,jbyteArray out)
{
	Encoder * en = (Encoder *) handle;
	x264_picture_t pic_out;


    int i_data=0;
	int nNal=-1;
	int result=0;
	int i=0,j=0;
	int nPix=0;

	jbyte * Buf = (jbyte*)(*env)->GetByteArrayElements(env, in, 0);
	jbyte * h264Buf = (jbyte*)(*env)->GetByteArrayElements(env, out, 0);
	unsigned char * pTmpOut = h264Buf;


	int nPicSize=en->param->i_width*en->param->i_height;
	/*
	Y数据全部从在一块，UV数据使用interleave方式存储
	YYYY
	YYYY
	UVUV
	 */
	jbyte * y=en->picture->img.plane[0];
	jbyte * v=en->picture->img.plane[1];
	jbyte * u=en->picture->img.plane[2];
	memcpy(en->picture->img.plane[0],Buf,nPicSize);
	for (i=0;i<nPicSize/4;i++)
	{
		*(u+i)=*(Buf+nPicSize+i*2);
		*(v+i)=*(Buf+nPicSize+i*2+1);
	}

	switch (type)
	{
	case 0:
		en->picture->i_type = X264_TYPE_P;
		break;
	case 1:
		en->picture->i_type = X264_TYPE_IDR;
		break;
	case 2:
		en->picture->i_type = X264_TYPE_I;
		break;
	default:
		en->picture->i_type = X264_TYPE_AUTO;
		break;
	}


    if( x264_encoder_encode( en->handle, &(en->nal), &nNal, en->picture ,&pic_out) < 0 )
    {
        return -1;
    }
    for (i = 0; i < nNal; i++){
          memcpy(pTmpOut, en->nal[i].p_payload, en->nal[i].i_payload);
          pTmpOut += en->nal[i].i_payload;
          result+=en->nal[i].i_payload;
    }
	return result;
}



/*
 * Class:     h264_com_VView
 * Method:    InitDecoder
 * Signature: ()I
 */
jlong Java_h264_com_VView_InitDecoder(JNIEnv* env, jobject thiz)
{
	CreateYUVTab_16();
	Decoder * de  = (Decoder *)av_malloc(sizeof(Decoder));
	de->c = avcodec_alloc_context();

	avcodec_open(de->c);

	de->picture  = avcodec_alloc_frame();//picture= malloc(sizeof(AVFrame));
		
	return (jlong)de;
}

/*
 * Class:     h264_com_VView
 * Method:    UninitDecoder
 * Signature: ()I
 */
jint Java_h264_com_VView_UninitDecoder(JNIEnv* env, jobject thiz,jlong pDecoder)
{
	DeleteYUVTab();
	if (pDecoder)
	{
		Decoder * de=(Decoder * )pDecoder;
		if(de->c)
		{
			decode_end(de->c);
			free(de->c->priv_data);

			free(de->c);
			de->c = NULL;
		}

		if(de->picture)
		{
			free(de->picture);
			de->picture = NULL;
		}
		free(de);
		return 1;
	}
	else
		return 0;


	

}

/*
 * Class:     h264_com_VView
 * Method:    DecoderNal
 * Signature: ([B[I)I
 */
jint Java_h264_com_VView_DecoderNal(JNIEnv* env, jobject thiz,jlong pDecoder,jbyteArray in, jint nalLen, jbyteArray out)
{
	int i;
	int imod;
	int got_picture;
	if (pDecoder)
	{
		Decoder * de=(Decoder * )pDecoder;

		jbyte * Buf = (jbyte*)(*env)->GetByteArrayElements(env, in, 0);
		jbyte * Pixel= (jbyte*)(*env)->GetByteArrayElements(env, out, 0);

		int consumed_bytes = decode_frame(de->c, de->picture, &got_picture, Buf, nalLen);

		if(consumed_bytes > 0)
		{
			DisplayYUV_16((int*)Pixel, de->picture->data[0], de->picture->data[1], de->picture->data[2], de->c->width, de->c->height, de->picture->linesize[0], de->picture->linesize[1],  de->c->width);
		}

		(*env)->ReleaseByteArrayElements(env, in, Buf, 0);
		(*env)->ReleaseByteArrayElements(env, out, Pixel, 0);
	
		return consumed_bytes;
	}
	else
		return 0;
}

jint Java_h264_com_VView_GetH264Width(JNIEnv* env, jobject thiz,jlong pDecoder)
{
	if (pDecoder)
	{
		Decoder * de=(Decoder * )pDecoder;
		return de->c->width;
	}
	else
		return 0;
}


jint Java_h264_com_VView_GetH264Height(JNIEnv* env, jobject thiz,jlong pDecoder)
{
	if (pDecoder)
	{
		Decoder * de=(Decoder * )pDecoder;
		return de->c->height;
	}
	else
		return 0;
}

jint Java_h264_com_VView_ntohl(JNIEnv* env, jobject class,jint i)
{
	return ntohl(i);
}
jint Java_h264_com_VView_htonl(JNIEnv* env, jobject class,jint i)
{
	return htonl(i);
}
jshort Java_h264_com_VView_ntohs(JNIEnv* env, jobject class,jshort i)
{
	return ntohs(i);
}
jshort Java_h264_com_VView_htons(JNIEnv* env, jobject class,jshort i)
{
	return htons(i);
}
