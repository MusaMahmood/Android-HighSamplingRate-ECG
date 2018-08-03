//
// Academic License - for use in teaching, academic research, and meeting
// course requirements at degree granting institutions only.  Not for
// government, commercial, or other organizational use.
// File: resample_ecg.cpp
//
// MATLAB Coder version            : 3.3
// C/C++ source code generated on  : 02-Aug-2018 14:10:00
//

// Include Files
#include "rt_nonfinite.h"
#include "resample_ecg.h"

// Type Definitions
#ifndef struct_dsp_FIRRateConverter_0
#define struct_dsp_FIRRateConverter_0

struct dsp_FIRRateConverter_0
{
  int S0_isInitialized;
  double W0_InBuf[85];
  int W1_InBufIdx;
  double P0_FILTER[85];
  int P1_PolyphaseSelector;
  int P2_StartIdx[4];
  int P3_StopIdx[4];
};

#endif                                 //struct_dsp_FIRRateConverter_0

typedef struct {
  int isInitialized;
  dsp_FIRRateConverter_0 cSFunObject;
} dspcodegen_FIRRateConverter;

// Function Declarations
static void SystemCore_reset(dspcodegen_FIRRateConverter *obj);
static void SystemCore_step(dspcodegen_FIRRateConverter *obj, const double
  varargin_1[8084], double varargout_1[2021]);
static dspcodegen_FIRRateConverter *c_FIRRateConverter_FIRRateConve
  (dspcodegen_FIRRateConverter *obj);
static void d_FIRRateConverter_FIRRateConve(dspcodegen_FIRRateConverter **obj);
static void resample(const double x[8000], double yout[2000]);

// Function Definitions

//
// Arguments    : dspcodegen_FIRRateConverter *obj
// Return Type  : void
//
static void SystemCore_reset(dspcodegen_FIRRateConverter *obj)
{
  dspcodegen_FIRRateConverter *b_obj;
  int i;
  if (obj->isInitialized == 1) {
    b_obj = obj;

    // System object Initialization function: dsp.FIRRateConverter
    b_obj->cSFunObject.W1_InBufIdx = 0;
    for (i = 0; i < 85; i++) {
      b_obj->cSFunObject.W0_InBuf[i] = 0.0;
    }
  }
}

//
// Arguments    : dspcodegen_FIRRateConverter *obj
//                const double varargin_1[8084]
//                double varargout_1[2021]
// Return Type  : void
//
static void SystemCore_step(dspcodegen_FIRRateConverter *obj, const double
  varargin_1[8084], double varargout_1[2021])
{
  dspcodegen_FIRRateConverter *b_obj;
  dsp_FIRRateConverter_0 *c_obj;
  int outIdx;
  int i;
  int inIdx;
  int inBufIdx;
  int n;
  int outputStartIdx;
  int outputStopIdx;
  double acc;
  int coefPolyphaseOffset;
  double prod;
  if (obj->isInitialized != 1) {
    b_obj = obj;
    b_obj->isInitialized = 1;

    // System object Initialization function: dsp.FIRRateConverter
    b_obj->cSFunObject.W1_InBufIdx = 0;
    for (i = 0; i < 85; i++) {
      b_obj->cSFunObject.W0_InBuf[i] = 0.0;
    }
  }

  b_obj = obj;
  c_obj = &b_obj->cSFunObject;

  // System object Outputs function: dsp.FIRRateConverter
  outIdx = 0;
  inIdx = 0;

  // Update inBufIdx and inputChannelOffset for current channel
  inBufIdx = b_obj->cSFunObject.W1_InBufIdx;
  for (n = 0; n < 8084; n++) {
    i = n % 4;
    outputStartIdx = c_obj->P2_StartIdx[i];
    outputStopIdx = c_obj->P3_StopIdx[i];

    // Read input into inBufArray
    c_obj->W0_InBuf[inBufIdx] = varargin_1[inIdx];
    inIdx++;

    // Generate outputs (if any) for current input n
    while (outputStartIdx < outputStopIdx) {
      acc = 0.0;
      coefPolyphaseOffset = c_obj->P1_PolyphaseSelector * 85;
      for (i = inBufIdx; i < 85; i++) {
        prod = c_obj->P0_FILTER[(coefPolyphaseOffset + i) - inBufIdx] *
          c_obj->W0_InBuf[i];
        acc += prod;
      }

      for (i = 0; i < inBufIdx; i++) {
        prod = c_obj->P0_FILTER[((coefPolyphaseOffset - inBufIdx) + i) + 85] *
          c_obj->W0_InBuf[i];
        acc += prod;
      }

      varargout_1[outIdx] = acc;
      outIdx++;
      outputStartIdx++;
    }

    // Decrement inBufIdx, wrap if necessary
    if (inBufIdx == 0) {
      inBufIdx = 84;
    } else {
      inBufIdx--;
    }
  }

  // Update inBufIdx
  b_obj->cSFunObject.W1_InBufIdx = inBufIdx;
}

//
// Arguments    : dspcodegen_FIRRateConverter *obj
// Return Type  : dspcodegen_FIRRateConverter *
//
static dspcodegen_FIRRateConverter *c_FIRRateConverter_FIRRateConve
  (dspcodegen_FIRRateConverter *obj)
{
  dspcodegen_FIRRateConverter *b_obj;
  b_obj = obj;
  d_FIRRateConverter_FIRRateConve(&b_obj);
  return b_obj;
}

//
// Arguments    : dspcodegen_FIRRateConverter **obj
// Return Type  : void
//
static void d_FIRRateConverter_FIRRateConve(dspcodegen_FIRRateConverter **obj)
{
  dspcodegen_FIRRateConverter *b_obj;
  dsp_FIRRateConverter_0 *c_obj;
  int i;
  static const double dv1[85] = { 0.0, 0.0, 0.0, 0.0, -3.5799115519692397E-19,
    -0.00028264939479611523, -0.00052579196542946907, -0.00047541698372319323,
    9.2726027350597383E-19, 0.00073162852724047189, 0.0012546327308679548,
    0.0010630797667137758, -1.7472805760904012E-18, -0.0014830129609459293,
    -0.0024477084927997223, -0.0020065313653163728, 2.8035592572493556E-18,
    0.002651272519201363, 0.0042787887572440283, 0.0034384897676579375,
    -4.0459880955907575E-18, -0.00439479131278936, -0.0069958192527395284,
    -0.005554971391924941, 5.3912076618757543E-18, 0.006966729089880221,
    0.011012871024959065, 0.0086988136875902778, -6.7306397836470481E-18,
    -0.010855771610528706, -0.017172133746420341, -0.013606372767195627,
    7.9432457364957935E-18, 0.017243084699543748, 0.027647432518231357,
    0.022320837133011546, -8.9111641394119518E-18, -0.030033587538636723,
    -0.050471105705763832, -0.043494435742885119, 9.5357833560552989E-18,
    0.0741357049010938, 0.15836902171996831, 0.22490814275255214,
    0.250159141272278, 0.22490814275255214, 0.15836902171996831,
    0.0741357049010938, 9.5357833560552989E-18, -0.043494435742885119,
    -0.050471105705763832, -0.030033587538636723, -8.9111641394119518E-18,
    0.022320837133011546, 0.027647432518231357, 0.017243084699543748,
    7.9432457364957935E-18, -0.013606372767195627, -0.017172133746420341,
    -0.010855771610528706, -6.7306397836470481E-18, 0.0086988136875902778,
    0.011012871024959065, 0.006966729089880221, 5.3912076618757543E-18,
    -0.005554971391924941, -0.0069958192527395284, -0.00439479131278936,
    -4.0459880955907575E-18, 0.0034384897676579375, 0.0042787887572440283,
    0.002651272519201363, 2.8035592572493556E-18, -0.0020065313653163728,
    -0.0024477084927997223, -0.0014830129609459293, -1.7472805760904012E-18,
    0.0010630797667137758, 0.0012546327308679548, 0.00073162852724047189,
    9.2726027350597383E-19, -0.00047541698372319323, -0.00052579196542946907,
    -0.00028264939479611523, -3.5799115519692397E-19 };

  static const signed char iv0[4] = { 0, 1, 1, 1 };

  b_obj = *obj;
  *obj = b_obj;
  b_obj = *obj;
  *obj = b_obj;
  b_obj = *obj;
  *obj = b_obj;
  b_obj = *obj;
  *obj = b_obj;
  (*obj)->isInitialized = 0;
  b_obj = *obj;
  *obj = b_obj;
  b_obj = *obj;
  *obj = b_obj;
  b_obj = *obj;
  *obj = b_obj;
  c_obj = &(*obj)->cSFunObject;

  // System object Constructor function: dsp.FIRRateConverter
  for (i = 0; i < 85; i++) {
    c_obj->P0_FILTER[i] = dv1[i];
  }

  c_obj->P1_PolyphaseSelector = 0;
  for (i = 0; i < 4; i++) {
    c_obj->P2_StartIdx[i] = iv0[i];
  }

  for (i = 0; i < 4; i++) {
    c_obj->P3_StopIdx[i] = 1;
  }
}

//
// Arguments    : const double x[8000]
//                double yout[2000]
// Return Type  : void
//
static void resample(const double x[8000], double yout[2000])
{
  dspcodegen_FIRRateConverter s;
  double b_x[8084];
  double y[2021];
  c_FIRRateConverter_FIRRateConve(&s);
  memcpy(&b_x[0], &x[0], 8000U * sizeof(double));
  memset(&b_x[8000], 0, 84U * sizeof(double));
  SystemCore_step(&s, b_x, y);
  SystemCore_reset(&s);

  // System object Destructor function: dsp.FIRRateConverter
  memcpy(&yout[0], &y[11], 2000U * sizeof(double));
}

//
// RESAMPLE_ECG Takes input X, Fs, and resamples down to 250 Hz.
//  ______
//  INPUTS
//  X: Input array of size (8*Fs, 1)
//  Fs: Currently only supports 1kHz
//  ______
//  OUTPUT:
//  Y: Resampled output array of size (8*250, 1)
// Arguments    : const double X[8000]
//                double Fs
//                double Y[2000]
// Return Type  : void
//
void resample_ecg(const double X[8000], double Fs, double Y[2000])
{
  if (Fs == 1000.0) {
    resample(X, Y);
  } else {
    memset(&Y[0], 0, 2000U * sizeof(double));
  }
}

//
// Arguments    : void
// Return Type  : void
//
void resample_ecg_initialize()
{
  rt_InitInfAndNaN(8U);
}

//
// Arguments    : void
// Return Type  : void
//
void resample_ecg_terminate()
{
  // (no terminate code required)
}

//
// File trailer for resample_ecg.cpp
//
// [EOF]
//
