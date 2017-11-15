//
// Created by mahmoodms on 4/3/2017.
//

#include "rt_nonfinite.h"
#include "ssvep_filter_f32.h"

/*Additional Includes*/
#include <jni.h>
#include <android/log.h>
#include "ecg_var_filter.h"
#include "ecg_var_filter_emxAPI.h"

#define  LOG_TAG "jniExecutor-cpp"
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// Function Definitions
extern "C" {
JNIEXPORT jfloatArray JNICALL
Java_com_yeolabgt_mahmoodms_ecg2chdemo_DeviceControlActivity_jecgVarFilter(
        JNIEnv *env, jobject jobject1, jdoubleArray data, jdouble sample_rate,
        jdouble window_length) {
    jdouble *X1 = env->GetDoubleArrayElements(data, NULL);
    int iv0[1] = {(int) window_length};
    //TODO; also try:
    if (X1 == NULL) {
        LOGE("ERROR - C_ARRAY IS NULL");
        return nullptr;
    }
    emxArray_real32_T *Y;
    emxInitArray_real32_T(&Y, 2);
    emxArray_real_T *X = emxCreateND_real_T(1, *(int (*)[1]) &iv0[0]);
    for (int i = 0; i < X->size[1]; ++i) {
        X->data[i] = X1[i];
    }
    jfloatArray m_result = env->NewFloatArray(iv0[0]);
    ecg_var_filter(X, sample_rate, window_length, Y);
    env->SetFloatArrayRegion(m_result, 0, iv0[0], Y->data);
    emxDestroyArray_real32_T(Y);
    emxDestroyArray_real_T(X);
    return m_result;
}
}

extern "C" {
JNIEXPORT jfloatArray JNICALL
Java_com_yeolabgt_mahmoodms_ecg2chdemo_DeviceControlActivity_jSSVEPCfilter(
        JNIEnv *env, jobject jobject1, jdoubleArray data) {
    jdouble *X1 = env->GetDoubleArrayElements(data, NULL);
    float Y[1000]; // First two values = Y; last 499 = cPSD
    if (X1 == NULL) LOGE("ERROR - C_ARRAY IS NULL");
    jfloatArray m_result = env->NewFloatArray(1000);
    ssvep_filter_f32(X1, Y);
    env->SetFloatArrayRegion(m_result, 0, 1000, Y);
    return m_result;
}
}

extern "C" {
JNIEXPORT jdoubleArray JNICALL
/**
 *
 * @param env
 * @param jobject1
 * @return array of frequencies (Hz) corresponding to a raw input signal.
 */
Java_com_yeolabgt_mahmoodms_ecg2chdemo_DeviceControlActivity_jLoadfPSD(
        JNIEnv *env, jobject jobject1, jint sampleRate) {
    jdoubleArray m_result = env->NewDoubleArray(sampleRate);
    double fPSD[sampleRate];
    for (int i = 0; i < sampleRate; i++) {
        fPSD[i] = (double) i * (double) sampleRate / (double) (sampleRate * 2);
    }
    env->SetDoubleArrayRegion(m_result, 0, sampleRate, fPSD);
    return m_result;
}
}

extern "C" {
JNIEXPORT jint JNICALL
Java_com_yeolabgt_mahmoodms_ecg2chdemo_DeviceControlActivity_jmainInitialization(
        JNIEnv *env, jobject obj, jboolean initialize) {
    if (!(bool) initialize) {
        ecg_var_filter_initialize();
        return 0;
    } else {
        ecg_var_filter_terminate();
        return -1;
    }
}
}
