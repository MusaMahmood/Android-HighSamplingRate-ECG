//
// Academic License - for use in teaching, academic research, and meeting
// course requirements at degree granting institutions only.  Not for
// government, commercial, or other organizational use.
// File: resample_ecg.h
//
// MATLAB Coder version            : 3.3
// C/C++ source code generated on  : 02-Aug-2018 14:10:00
//
#ifndef RESAMPLE_ECG_H
#define RESAMPLE_ECG_H

// Include Files
#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include "rtwtypes.h"
#include "resample_ecg_types.h"

// Function Declarations
extern void resample_ecg(const double X[8000], double Fs, double Y[2000]);
extern void resample_ecg_initialize();
extern void resample_ecg_terminate();

#endif

//
// File trailer for resample_ecg.h
//
// [EOF]
//
