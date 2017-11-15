//
// Academic License - for use in teaching, academic research, and meeting
// course requirements at degree granting institutions only.  Not for
// government, commercial, or other organizational use.
// File: ecg_var_filter.h
//
// MATLAB Coder version            : 3.3
// C/C++ source code generated on  : 14-Nov-2017 19:21:24
//
#ifndef ECG_VAR_FILTER_H
#define ECG_VAR_FILTER_H

// Include Files
#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include "rtwtypes.h"
#include "ecg_var_filter_types.h"

// Function Declarations
extern void ecg_var_filter(const emxArray_real_T *X, double Fs, double L,
  emxArray_real32_T *Y_return);
extern void ecg_var_filter_initialize();
extern void ecg_var_filter_terminate();

#endif

//
// File trailer for ecg_var_filter.h
//
// [EOF]
//
