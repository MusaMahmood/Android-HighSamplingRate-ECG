//
// Academic License - for use in teaching, academic research, and meeting
// course requirements at degree granting institutions only.  Not for
// government, commercial, or other organizational use.
// File: ecg_var_filter.cpp
//
// MATLAB Coder version            : 3.3
// C/C++ source code generated on  : 14-Nov-2017 19:21:24
//

// Include Files
#include "rt_nonfinite.h"
#include "ecg_var_filter.h"
#include "ecg_var_filter_emxutil.h"

// Function Declarations
static void b_filter(const emxArray_real_T *x, const double zi[6],
                     emxArray_real_T *y);

static void c_filter(const emxArray_real_T *x, const double zi[6],
                     emxArray_real_T *y);

static void d_filter(const emxArray_real_T *x, const double zi[6],
                     emxArray_real_T *y);

static void e_filter(const emxArray_real_T *x, const double zi[6],
                     emxArray_real_T *y);

static void filter(const emxArray_real_T *x, const double zi[6],
                   emxArray_real_T *y);

static void flipud(emxArray_real_T *x);

// Function Definitions

//
// Arguments    : const emxArray_real_T *x
//                const double zi[6]
//                emxArray_real_T *y
// Return Type  : void
//
static void b_filter(const emxArray_real_T *x, const double zi[6],
                     emxArray_real_T *y) {
    short x_idx_0;
    int niccp;
    int nx;
    int k;
    int j;
    static const double dv2[7] = {0.007167667426659, 0.0, -0.021503002279976, 0.0,
                                  0.021503002279976, 0.0, -0.007167667426659};

    double as;
    static const double dv3[7] = {1.0, -5.0445676794459251, 10.694677001386529,
                                  -12.217373564180143, 7.9414563333917991, -2.7860101600364189,
                                  0.411839132950083};

    x_idx_0 = (short) x->size[0];
    niccp = y->size[0];
    y->size[0] = x_idx_0;
    emxEnsureCapacity((emxArray__common *) y, niccp, sizeof(double));
    nx = x->size[0];
    niccp = x->size[0];
    if (!(niccp < 6)) {
        niccp = 6;
    }

    for (k = 0; k + 1 <= niccp; k++) {
        y->data[k] = zi[k];
    }

    while (niccp + 1 <= nx) {
        y->data[niccp] = 0.0;
        niccp++;
    }

    for (k = 1; k <= nx; k++) {
        niccp = (nx - k) + 1;
        if (!(niccp < 7)) {
            niccp = 7;
        }

        for (j = -1; j + 2 <= niccp; j++) {
            y->data[k + j] += x->data[k - 1] * dv2[j + 1];
        }

        niccp = nx - k;
        if (!(niccp < 6)) {
            niccp = 6;
        }

        as = -y->data[k - 1];
        for (j = 1; j <= niccp; j++) {
            y->data[(k + j) - 1] += as * dv3[j];
        }
    }
}

//
// Arguments    : const emxArray_real_T *x
//                const double zi[6]
//                emxArray_real_T *y
// Return Type  : void
//
static void c_filter(const emxArray_real_T *x, const double zi[6],
                     emxArray_real_T *y) {
    short x_idx_0;
    int niccp;
    int nx;
    int k;
    int j;
    static const double dv4[7] = {0.00107948982226, 0.0, -0.003238469466779, 0.0,
                                  0.003238469466779, 0.0, -0.00107948982226};

    double as;
    static const double dv5[7] = {1.0, -5.5390031075784583, 12.814616286414871,
                                  -15.852598393614553, 11.060956098498181, -4.1275431915197149,
                                  0.64357270671065};

    x_idx_0 = (short) x->size[0];
    niccp = y->size[0];
    y->size[0] = x_idx_0;
    emxEnsureCapacity((emxArray__common *) y, niccp, sizeof(double));
    nx = x->size[0];
    niccp = x->size[0];
    if (!(niccp < 6)) {
        niccp = 6;
    }

    for (k = 0; k + 1 <= niccp; k++) {
        y->data[k] = zi[k];
    }

    while (niccp + 1 <= nx) {
        y->data[niccp] = 0.0;
        niccp++;
    }

    for (k = 1; k <= nx; k++) {
        niccp = (nx - k) + 1;
        if (!(niccp < 7)) {
            niccp = 7;
        }

        for (j = -1; j + 2 <= niccp; j++) {
            y->data[k + j] += x->data[k - 1] * dv4[j + 1];
        }

        niccp = nx - k;
        if (!(niccp < 6)) {
            niccp = 6;
        }

        as = -y->data[k - 1];
        for (j = 1; j <= niccp; j++) {
            y->data[(k + j) - 1] += as * dv5[j];
        }
    }
}

//
// Arguments    : const emxArray_real_T *x
//                const double zi[6]
//                emxArray_real_T *y
// Return Type  : void
//
static void d_filter(const emxArray_real_T *x, const double zi[6],
                     emxArray_real_T *y) {
    short x_idx_0;
    int niccp;
    int nx;
    int k;
    int j;
    static const double dv6[7] = {0.00014931384513637959, 0.0,
                                  -0.00044794153540913872, 0.0, 0.00044794153540913872, 0.0,
                                  -0.00014931384513637959};

    double as;
    static const double dv7[7] = {1.0, -5.7744885348453057, 13.9024822354029,
                                  -17.862961622606818, 12.918932118347655, -4.9864648458745462,
                                  0.802500656483461};

    x_idx_0 = (short) x->size[0];
    niccp = y->size[0];
    y->size[0] = x_idx_0;
    emxEnsureCapacity((emxArray__common *) y, niccp, sizeof(double));
    nx = x->size[0];
    niccp = x->size[0];
    if (!(niccp < 6)) {
        niccp = 6;
    }

    for (k = 0; k + 1 <= niccp; k++) {
        y->data[k] = zi[k];
    }

    while (niccp + 1 <= nx) {
        y->data[niccp] = 0.0;
        niccp++;
    }

    for (k = 1; k <= nx; k++) {
        niccp = (nx - k) + 1;
        if (!(niccp < 7)) {
            niccp = 7;
        }

        for (j = -1; j + 2 <= niccp; j++) {
            y->data[k + j] += x->data[k - 1] * dv6[j + 1];
        }

        niccp = nx - k;
        if (!(niccp < 6)) {
            niccp = 6;
        }

        as = -y->data[k - 1];
        for (j = 1; j <= niccp; j++) {
            y->data[(k + j) - 1] += as * dv7[j];
        }
    }
}

//
// Arguments    : const emxArray_real_T *x
//                const double zi[6]
//                emxArray_real_T *y
// Return Type  : void
//
static void e_filter(const emxArray_real_T *x, const double zi[6],
                     emxArray_real_T *y) {
    short x_idx_0;
    int niccp;
    int nx;
    int k;
    int j;
    static const double dv8[7] = {1.9675346213925849E-5, 0.0,
                                  -5.9026038641777543E-5, 0.0, 5.9026038641777543E-5, 0.0,
                                  -1.9675346213925849E-5};

    double as;
    static const double dv9[7] = {1.0, -5.8886044477318116, 14.450542855246466,
                                  -18.915859691827226, 13.930370002411454, -5.4723100859653524,
                                  0.895861367980286};

    x_idx_0 = (short) x->size[0];
    niccp = y->size[0];
    y->size[0] = x_idx_0;
    emxEnsureCapacity((emxArray__common *) y, niccp, sizeof(double));
    nx = x->size[0];
    niccp = x->size[0];
    if (!(niccp < 6)) {
        niccp = 6;
    }

    for (k = 0; k + 1 <= niccp; k++) {
        y->data[k] = zi[k];
    }

    while (niccp + 1 <= nx) {
        y->data[niccp] = 0.0;
        niccp++;
    }

    for (k = 1; k <= nx; k++) {
        niccp = (nx - k) + 1;
        if (!(niccp < 7)) {
            niccp = 7;
        }

        for (j = -1; j + 2 <= niccp; j++) {
            y->data[k + j] += x->data[k - 1] * dv8[j + 1];
        }

        niccp = nx - k;
        if (!(niccp < 6)) {
            niccp = 6;
        }

        as = -y->data[k - 1];
        for (j = 1; j <= niccp; j++) {
            y->data[(k + j) - 1] += as * dv9[j];
        }
    }
}

//
// Arguments    : const emxArray_real_T *x
//                const double zi[6]
//                emxArray_real_T *y
// Return Type  : void
//
static void filter(const emxArray_real_T *x, const double zi[6], emxArray_real_T
*y) {
    short x_idx_0;
    int nx;
    int k;
    int naxpy;
    int j;
    static const double dv0[7] = {0.0418768282347742, 0.0, -0.125630484704323,
                                  0.0, 0.125630484704323, 0.0, -0.0418768282347742};

    double as;
    static const double dv1[7] = {1.0, -3.99412602172993, 6.79713743558926,
                                  -6.44840721730666, 3.65712515526032, -1.17053739881085,
                                  0.159769122451512};

    x_idx_0 = (short) x->size[0];
    nx = y->size[0];
    y->size[0] = x_idx_0;
    emxEnsureCapacity((emxArray__common *) y, nx, sizeof(double));
    nx = x->size[0];
    for (k = 0; k < 6; k++) {
        y->data[k] = zi[k];
    }

    for (k = 7; k <= nx; k++) {
        y->data[k - 1] = 0.0;
    }

    for (k = 1; k <= nx; k++) {
        naxpy = (nx - k) + 1;
        if (!(naxpy < 7)) {
            naxpy = 7;
        }

        for (j = -1; j + 2 <= naxpy; j++) {
            y->data[k + j] += x->data[k - 1] * dv0[j + 1];
        }

        naxpy = nx - k;
        if (!(naxpy < 6)) {
            naxpy = 6;
        }

        as = -y->data[k - 1];
        for (j = 1; j <= naxpy; j++) {
            y->data[(k + j) - 1] += as * dv1[j];
        }
    }
}

//
// Arguments    : emxArray_real_T *x
// Return Type  : void
//
static void flipud(emxArray_real_T *x) {
    int m;
    int md2;
    int i;
    double xtmp;
    m = x->size[0];
    md2 = x->size[0] >> 1;
    for (i = 1; i <= md2; i++) {
        xtmp = x->data[i - 1];
        x->data[i - 1] = x->data[m - i];
        x->data[m - i] = xtmp;
    }
}

//
// ECG_VAR_FILTER Filters ECG Signals
//  X must be singular dimension!
// Arguments    : const emxArray_real_T *X
//                double Fs
//                double L
//                emxArray_real32_T *Y_return
// Return Type  : void
//
void ecg_var_filter(const emxArray_real_T *X, double Fs, double L,
                    emxArray_real32_T *Y_return) {
    emxArray_real_T *Y;
    int i0;
    int loop_ub;
    emxArray_real_T *y_out;
    emxArray_real_T *x;
    emxArray_real_T *y;
    double b_y;
    double d0;
    int b_x;
    emxArray_real_T *c_y;
    double a[6];
    static const double b_a[6] = {-0.041876828234757295, -0.041876828234824783,
                                  0.083753656469613066, 0.0837536564695041, -0.041876828234757114,
                                  -0.04187682823477689};

    emxArray_real_T *d_y;
    static const double c_a[6] = {-0.0071676674264306165, -0.00716766742758271,
                                  0.014335334854835756, 0.014335334852045524,
                                  -0.0071676674261167816,
                                  -0.0071676674267530565};

    emxArray_real_T *e_y;
    static const double d_a[6] = {-0.0010794898272580762, -0.0010794897995737159,
                                  0.0021589796031568544, 0.0021589796823893513,
                                  -0.0010794898396731503,
                                  -0.0010794898190433745};

    emxArray_real_T *f_y;
    static const double e_a[6] = {-0.00014931381404319587,
                                  -0.00014931399359042888, 0.00029862797409114432,
                                  0.00029862741867479659,
                                  -0.00014931371504361222, -0.00014931387008867994};

    emxArray_real_T *g_y;
    static const double f_a[6] = {-1.9675619502730023E-5, -1.967401021306225E-5,
                                  3.9348079257138779E-5, 3.9353248749813825E-5,
                                  -1.9676596906123338E-5,
                                  -1.9675101385043892E-5};

    emxArray_real_T *h_y;
    emxArray_real_T *i_y;
    emxArray_real_T *j_y;
    emxArray_real_T *k_y;
    emxArray_real_T *l_y;
    emxArray_int32_T *r0;
    emxArray_int32_T *r1;
    emxArray_int32_T *r2;
    emxArray_int32_T *r3;
    emxArray_int32_T *r4;
    emxInit_real_T(&Y, 2);
    i0 = Y->size[0] * Y->size[1];
    Y->size[0] = (int) L;
    Y->size[1] = 1;
    emxEnsureCapacity((emxArray__common *) Y, i0, sizeof(double));
    loop_ub = (int) L;
    for (i0 = 0; i0 < loop_ub; i0++) {
        Y->data[i0] = 0.0;
    }

    emxInit_real_T(&y_out, 2);
    emxInit_real_T1(&x, 1);
    emxInit_real_T1(&y, 1);
    if (Fs == 250.0) {
        if (X->size[0] == 1) {
            i0 = x->size[0];
            x->size[0] = 1;
            emxEnsureCapacity((emxArray__common *) x, i0, sizeof(double));
            x->data[0] = X->data[0];
        } else {
            i0 = x->size[0];
            x->size[0] = X->size[0];
            emxEnsureCapacity((emxArray__common *) x, i0, sizeof(double));
            loop_ub = X->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                x->data[i0] = X->data[i0];
            }
        }

        if (x->size[0] == 0) {
            i0 = y_out->size[0] * y_out->size[1];
            y_out->size[0] = 0;
            y_out->size[1] = 0;
            emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
        } else {
            b_y = 2.0 * x->data[0];
            d0 = 2.0 * x->data[x->size[0] - 1];
            b_x = x->size[0] - 2;
            i0 = y->size[0];
            y->size[0] = 36 + x->size[0];
            emxEnsureCapacity((emxArray__common *) y, i0, sizeof(double));
            for (i0 = 0; i0 < 18; i0++) {
                y->data[i0] = b_y - x->data[18 - i0];
            }

            loop_ub = x->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                y->data[i0 + 18] = x->data[i0];
            }

            for (i0 = 0; i0 < 18; i0++) {
                y->data[(i0 + x->size[0]) + 18] = d0 - x->data[b_x - i0];
            }

            b_y = y->data[0];
            for (i0 = 0; i0 < 6; i0++) {
                a[i0] = b_a[i0] * b_y;
            }

            emxInit_real_T1(&c_y, 1);
            i0 = c_y->size[0];
            c_y->size[0] = y->size[0];
            emxEnsureCapacity((emxArray__common *) c_y, i0, sizeof(double));
            loop_ub = y->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                c_y->data[i0] = y->data[i0];
            }

            filter(c_y, a, y);
            flipud(y);
            b_y = y->data[0];
            emxFree_real_T(&c_y);
            for (i0 = 0; i0 < 6; i0++) {
                a[i0] = b_a[i0] * b_y;
            }

            emxInit_real_T1(&h_y, 1);
            i0 = h_y->size[0];
            h_y->size[0] = y->size[0];
            emxEnsureCapacity((emxArray__common *) h_y, i0, sizeof(double));
            loop_ub = y->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                h_y->data[i0] = y->data[i0];
            }

            filter(h_y, a, y);
            flipud(y);
            emxFree_real_T(&h_y);
            if (X->size[0] == 1) {
                loop_ub = x->size[0];
                i0 = y_out->size[0] * y_out->size[1];
                y_out->size[0] = 1;
                y_out->size[1] = loop_ub;
                emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
                for (i0 = 0; i0 < loop_ub; i0++) {
                    y_out->data[y_out->size[0] * i0] = y->data[18 + i0];
                }
            } else {
                emxInit_int32_T(&r0, 1);
                loop_ub = x->size[0];
                i0 = y_out->size[0] * y_out->size[1];
                y_out->size[0] = loop_ub;
                y_out->size[1] = 1;
                emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
                b_x = x->size[0];
                i0 = r0->size[0];
                r0->size[0] = b_x;
                emxEnsureCapacity((emxArray__common *) r0, i0, sizeof(int));
                for (i0 = 0; i0 < b_x; i0++) {
                    r0->data[i0] = 19 + i0;
                }

                for (i0 = 0; i0 < loop_ub; i0++) {
                    y_out->data[i0] = y->data[r0->data[i0] - 1];
                }

                emxFree_int32_T(&r0);
            }
        }

        i0 = Y->size[0] * Y->size[1];
        Y->size[0] = y_out->size[0];
        Y->size[1] = y_out->size[1];
        emxEnsureCapacity((emxArray__common *) Y, i0, sizeof(double));
        loop_ub = y_out->size[0] * y_out->size[1];
        for (i0 = 0; i0 < loop_ub; i0++) {
            Y->data[i0] = y_out->data[i0];
        }
    } else if (Fs == 500.0) {
        if (X->size[0] == 1) {
            i0 = x->size[0];
            x->size[0] = 1;
            emxEnsureCapacity((emxArray__common *) x, i0, sizeof(double));
            x->data[0] = X->data[0];
        } else {
            i0 = x->size[0];
            x->size[0] = X->size[0];
            emxEnsureCapacity((emxArray__common *) x, i0, sizeof(double));
            loop_ub = X->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                x->data[i0] = X->data[i0];
            }
        }

        if (x->size[0] == 0) {
            i0 = y_out->size[0] * y_out->size[1];
            y_out->size[0] = 0;
            y_out->size[1] = 0;
            emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
        } else {
            b_y = 2.0 * x->data[0];
            d0 = 2.0 * x->data[x->size[0] - 1];
            b_x = x->size[0] - 2;
            i0 = y->size[0];
            y->size[0] = 36 + x->size[0];
            emxEnsureCapacity((emxArray__common *) y, i0, sizeof(double));
            for (i0 = 0; i0 < 18; i0++) {
                y->data[i0] = b_y - x->data[18 - i0];
            }

            loop_ub = x->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                y->data[i0 + 18] = x->data[i0];
            }

            for (i0 = 0; i0 < 18; i0++) {
                y->data[(i0 + x->size[0]) + 18] = d0 - x->data[b_x - i0];
            }

            b_y = y->data[0];
            for (i0 = 0; i0 < 6; i0++) {
                a[i0] = c_a[i0] * b_y;
            }

            emxInit_real_T1(&d_y, 1);
            i0 = d_y->size[0];
            d_y->size[0] = y->size[0];
            emxEnsureCapacity((emxArray__common *) d_y, i0, sizeof(double));
            loop_ub = y->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                d_y->data[i0] = y->data[i0];
            }

            b_filter(d_y, a, y);
            flipud(y);
            b_y = y->data[0];
            emxFree_real_T(&d_y);
            for (i0 = 0; i0 < 6; i0++) {
                a[i0] = c_a[i0] * b_y;
            }

            emxInit_real_T1(&i_y, 1);
            i0 = i_y->size[0];
            i_y->size[0] = y->size[0];
            emxEnsureCapacity((emxArray__common *) i_y, i0, sizeof(double));
            loop_ub = y->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                i_y->data[i0] = y->data[i0];
            }

            b_filter(i_y, a, y);
            flipud(y);
            emxFree_real_T(&i_y);
            if (X->size[0] == 1) {
                loop_ub = x->size[0];
                i0 = y_out->size[0] * y_out->size[1];
                y_out->size[0] = 1;
                y_out->size[1] = loop_ub;
                emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
                for (i0 = 0; i0 < loop_ub; i0++) {
                    y_out->data[y_out->size[0] * i0] = y->data[18 + i0];
                }
            } else {
                emxInit_int32_T(&r1, 1);
                loop_ub = x->size[0];
                i0 = y_out->size[0] * y_out->size[1];
                y_out->size[0] = loop_ub;
                y_out->size[1] = 1;
                emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
                b_x = x->size[0];
                i0 = r1->size[0];
                r1->size[0] = b_x;
                emxEnsureCapacity((emxArray__common *) r1, i0, sizeof(int));
                for (i0 = 0; i0 < b_x; i0++) {
                    r1->data[i0] = 19 + i0;
                }

                for (i0 = 0; i0 < loop_ub; i0++) {
                    y_out->data[i0] = y->data[r1->data[i0] - 1];
                }

                emxFree_int32_T(&r1);
            }
        }

        i0 = Y->size[0] * Y->size[1];
        Y->size[0] = y_out->size[0];
        Y->size[1] = y_out->size[1];
        emxEnsureCapacity((emxArray__common *) Y, i0, sizeof(double));
        loop_ub = y_out->size[0] * y_out->size[1];
        for (i0 = 0; i0 < loop_ub; i0++) {
            Y->data[i0] = y_out->data[i0];
        }
    } else if (Fs == 1000.0) {
        if (X->size[0] == 1) {
            i0 = x->size[0];
            x->size[0] = 1;
            emxEnsureCapacity((emxArray__common *) x, i0, sizeof(double));
            x->data[0] = X->data[0];
        } else {
            i0 = x->size[0];
            x->size[0] = X->size[0];
            emxEnsureCapacity((emxArray__common *) x, i0, sizeof(double));
            loop_ub = X->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                x->data[i0] = X->data[i0];
            }
        }

        if (x->size[0] == 0) {
            i0 = y_out->size[0] * y_out->size[1];
            y_out->size[0] = 0;
            y_out->size[1] = 0;
            emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
        } else {
            b_y = 2.0 * x->data[0];
            d0 = 2.0 * x->data[x->size[0] - 1];
            b_x = x->size[0] - 2;
            i0 = y->size[0];
            y->size[0] = 36 + x->size[0];
            emxEnsureCapacity((emxArray__common *) y, i0, sizeof(double));
            for (i0 = 0; i0 < 18; i0++) {
                y->data[i0] = b_y - x->data[18 - i0];
            }

            loop_ub = x->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                y->data[i0 + 18] = x->data[i0];
            }

            for (i0 = 0; i0 < 18; i0++) {
                y->data[(i0 + x->size[0]) + 18] = d0 - x->data[b_x - i0];
            }

            b_y = y->data[0];
            for (i0 = 0; i0 < 6; i0++) {
                a[i0] = d_a[i0] * b_y;
            }

            emxInit_real_T1(&e_y, 1);
            i0 = e_y->size[0];
            e_y->size[0] = y->size[0];
            emxEnsureCapacity((emxArray__common *) e_y, i0, sizeof(double));
            loop_ub = y->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                e_y->data[i0] = y->data[i0];
            }

            c_filter(e_y, a, y);
            flipud(y);
            b_y = y->data[0];
            emxFree_real_T(&e_y);
            for (i0 = 0; i0 < 6; i0++) {
                a[i0] = d_a[i0] * b_y;
            }

            emxInit_real_T1(&j_y, 1);
            i0 = j_y->size[0];
            j_y->size[0] = y->size[0];
            emxEnsureCapacity((emxArray__common *) j_y, i0, sizeof(double));
            loop_ub = y->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                j_y->data[i0] = y->data[i0];
            }

            c_filter(j_y, a, y);
            flipud(y);
            emxFree_real_T(&j_y);
            if (X->size[0] == 1) {
                loop_ub = x->size[0];
                i0 = y_out->size[0] * y_out->size[1];
                y_out->size[0] = 1;
                y_out->size[1] = loop_ub;
                emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
                for (i0 = 0; i0 < loop_ub; i0++) {
                    y_out->data[y_out->size[0] * i0] = y->data[18 + i0];
                }
            } else {
                emxInit_int32_T(&r2, 1);
                loop_ub = x->size[0];
                i0 = y_out->size[0] * y_out->size[1];
                y_out->size[0] = loop_ub;
                y_out->size[1] = 1;
                emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
                b_x = x->size[0];
                i0 = r2->size[0];
                r2->size[0] = b_x;
                emxEnsureCapacity((emxArray__common *) r2, i0, sizeof(int));
                for (i0 = 0; i0 < b_x; i0++) {
                    r2->data[i0] = 19 + i0;
                }

                for (i0 = 0; i0 < loop_ub; i0++) {
                    y_out->data[i0] = y->data[r2->data[i0] - 1];
                }

                emxFree_int32_T(&r2);
            }
        }

        i0 = Y->size[0] * Y->size[1];
        Y->size[0] = y_out->size[0];
        Y->size[1] = y_out->size[1];
        emxEnsureCapacity((emxArray__common *) Y, i0, sizeof(double));
        loop_ub = y_out->size[0] * y_out->size[1];
        for (i0 = 0; i0 < loop_ub; i0++) {
            Y->data[i0] = y_out->data[i0];
        }
    } else if (Fs == 2000.0) {
        if (X->size[0] == 1) {
            i0 = x->size[0];
            x->size[0] = 1;
            emxEnsureCapacity((emxArray__common *) x, i0, sizeof(double));
            x->data[0] = X->data[0];
        } else {
            i0 = x->size[0];
            x->size[0] = X->size[0];
            emxEnsureCapacity((emxArray__common *) x, i0, sizeof(double));
            loop_ub = X->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                x->data[i0] = X->data[i0];
            }
        }

        if (x->size[0] == 0) {
            i0 = y_out->size[0] * y_out->size[1];
            y_out->size[0] = 0;
            y_out->size[1] = 0;
            emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
        } else {
            b_y = 2.0 * x->data[0];
            d0 = 2.0 * x->data[x->size[0] - 1];
            b_x = x->size[0] - 2;
            i0 = y->size[0];
            y->size[0] = 36 + x->size[0];
            emxEnsureCapacity((emxArray__common *) y, i0, sizeof(double));
            for (i0 = 0; i0 < 18; i0++) {
                y->data[i0] = b_y - x->data[18 - i0];
            }

            loop_ub = x->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                y->data[i0 + 18] = x->data[i0];
            }

            for (i0 = 0; i0 < 18; i0++) {
                y->data[(i0 + x->size[0]) + 18] = d0 - x->data[b_x - i0];
            }

            b_y = y->data[0];
            for (i0 = 0; i0 < 6; i0++) {
                a[i0] = e_a[i0] * b_y;
            }

            emxInit_real_T1(&f_y, 1);
            i0 = f_y->size[0];
            f_y->size[0] = y->size[0];
            emxEnsureCapacity((emxArray__common *) f_y, i0, sizeof(double));
            loop_ub = y->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                f_y->data[i0] = y->data[i0];
            }

            d_filter(f_y, a, y);
            flipud(y);
            b_y = y->data[0];
            emxFree_real_T(&f_y);
            for (i0 = 0; i0 < 6; i0++) {
                a[i0] = e_a[i0] * b_y;
            }

            emxInit_real_T1(&k_y, 1);
            i0 = k_y->size[0];
            k_y->size[0] = y->size[0];
            emxEnsureCapacity((emxArray__common *) k_y, i0, sizeof(double));
            loop_ub = y->size[0];
            for (i0 = 0; i0 < loop_ub; i0++) {
                k_y->data[i0] = y->data[i0];
            }

            d_filter(k_y, a, y);
            flipud(y);
            emxFree_real_T(&k_y);
            if (X->size[0] == 1) {
                loop_ub = x->size[0];
                i0 = y_out->size[0] * y_out->size[1];
                y_out->size[0] = 1;
                y_out->size[1] = loop_ub;
                emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
                for (i0 = 0; i0 < loop_ub; i0++) {
                    y_out->data[y_out->size[0] * i0] = y->data[18 + i0];
                }
            } else {
                emxInit_int32_T(&r3, 1);
                loop_ub = x->size[0];
                i0 = y_out->size[0] * y_out->size[1];
                y_out->size[0] = loop_ub;
                y_out->size[1] = 1;
                emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
                b_x = x->size[0];
                i0 = r3->size[0];
                r3->size[0] = b_x;
                emxEnsureCapacity((emxArray__common *) r3, i0, sizeof(int));
                for (i0 = 0; i0 < b_x; i0++) {
                    r3->data[i0] = 19 + i0;
                }

                for (i0 = 0; i0 < loop_ub; i0++) {
                    y_out->data[i0] = y->data[r3->data[i0] - 1];
                }

                emxFree_int32_T(&r3);
            }
        }

        i0 = Y->size[0] * Y->size[1];
        Y->size[0] = y_out->size[0];
        Y->size[1] = y_out->size[1];
        emxEnsureCapacity((emxArray__common *) Y, i0, sizeof(double));
        loop_ub = y_out->size[0] * y_out->size[1];
        for (i0 = 0; i0 < loop_ub; i0++) {
            Y->data[i0] = y_out->data[i0];
        }
    } else {
        if (Fs == 4000.0) {
            if (X->size[0] == 1) {
                i0 = x->size[0];
                x->size[0] = 1;
                emxEnsureCapacity((emxArray__common *) x, i0, sizeof(double));
                x->data[0] = X->data[0];
            } else {
                i0 = x->size[0];
                x->size[0] = X->size[0];
                emxEnsureCapacity((emxArray__common *) x, i0, sizeof(double));
                loop_ub = X->size[0];
                for (i0 = 0; i0 < loop_ub; i0++) {
                    x->data[i0] = X->data[i0];
                }
            }

            if (x->size[0] == 0) {
                i0 = y_out->size[0] * y_out->size[1];
                y_out->size[0] = 0;
                y_out->size[1] = 0;
                emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
            } else {
                b_y = 2.0 * x->data[0];
                d0 = 2.0 * x->data[x->size[0] - 1];
                b_x = x->size[0] - 2;
                i0 = y->size[0];
                y->size[0] = 36 + x->size[0];
                emxEnsureCapacity((emxArray__common *) y, i0, sizeof(double));
                for (i0 = 0; i0 < 18; i0++) {
                    y->data[i0] = b_y - x->data[18 - i0];
                }

                loop_ub = x->size[0];
                for (i0 = 0; i0 < loop_ub; i0++) {
                    y->data[i0 + 18] = x->data[i0];
                }

                for (i0 = 0; i0 < 18; i0++) {
                    y->data[(i0 + x->size[0]) + 18] = d0 - x->data[b_x - i0];
                }

                b_y = y->data[0];
                for (i0 = 0; i0 < 6; i0++) {
                    a[i0] = f_a[i0] * b_y;
                }

                emxInit_real_T1(&g_y, 1);
                i0 = g_y->size[0];
                g_y->size[0] = y->size[0];
                emxEnsureCapacity((emxArray__common *) g_y, i0, sizeof(double));
                loop_ub = y->size[0];
                for (i0 = 0; i0 < loop_ub; i0++) {
                    g_y->data[i0] = y->data[i0];
                }

                e_filter(g_y, a, y);
                flipud(y);
                b_y = y->data[0];
                emxFree_real_T(&g_y);
                for (i0 = 0; i0 < 6; i0++) {
                    a[i0] = f_a[i0] * b_y;
                }

                emxInit_real_T1(&l_y, 1);
                i0 = l_y->size[0];
                l_y->size[0] = y->size[0];
                emxEnsureCapacity((emxArray__common *) l_y, i0, sizeof(double));
                loop_ub = y->size[0];
                for (i0 = 0; i0 < loop_ub; i0++) {
                    l_y->data[i0] = y->data[i0];
                }

                e_filter(l_y, a, y);
                flipud(y);
                emxFree_real_T(&l_y);
                if (X->size[0] == 1) {
                    loop_ub = x->size[0];
                    i0 = y_out->size[0] * y_out->size[1];
                    y_out->size[0] = 1;
                    y_out->size[1] = loop_ub;
                    emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
                    for (i0 = 0; i0 < loop_ub; i0++) {
                        y_out->data[y_out->size[0] * i0] = y->data[18 + i0];
                    }
                } else {
                    emxInit_int32_T(&r4, 1);
                    loop_ub = x->size[0];
                    i0 = y_out->size[0] * y_out->size[1];
                    y_out->size[0] = loop_ub;
                    y_out->size[1] = 1;
                    emxEnsureCapacity((emxArray__common *) y_out, i0, sizeof(double));
                    b_x = x->size[0];
                    i0 = r4->size[0];
                    r4->size[0] = b_x;
                    emxEnsureCapacity((emxArray__common *) r4, i0, sizeof(int));
                    for (i0 = 0; i0 < b_x; i0++) {
                        r4->data[i0] = 19 + i0;
                    }

                    for (i0 = 0; i0 < loop_ub; i0++) {
                        y_out->data[i0] = y->data[r4->data[i0] - 1];
                    }

                    emxFree_int32_T(&r4);
                }
            }

            i0 = Y->size[0] * Y->size[1];
            Y->size[0] = y_out->size[0];
            Y->size[1] = y_out->size[1];
            emxEnsureCapacity((emxArray__common *) Y, i0, sizeof(double));
            loop_ub = y_out->size[0] * y_out->size[1];
            for (i0 = 0; i0 < loop_ub; i0++) {
                Y->data[i0] = y_out->data[i0];
            }
        }
    }

    emxFree_real_T(&y);
    emxFree_real_T(&x);
    emxFree_real_T(&y_out);
    i0 = Y_return->size[0] * Y_return->size[1];
    Y_return->size[0] = Y->size[0];
    Y_return->size[1] = Y->size[1];
    emxEnsureCapacity((emxArray__common *) Y_return, i0, sizeof(float));
    loop_ub = Y->size[0] * Y->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
        Y_return->data[i0] = (float) Y->data[i0];
    }

    emxFree_real_T(&Y);
}

//
// Arguments    : void
// Return Type  : void
//
void ecg_var_filter_initialize() {
    rt_InitInfAndNaN(8U);
}

//
// Arguments    : void
// Return Type  : void
//
void ecg_var_filter_terminate() {
    // (no terminate code required)
}

//
// File trailer for ecg_var_filter.cpp
//
// [EOF]
//
