package com.yeolabgt.mahmoodms.ecg2chdemo;

import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by mmahmood31 on 10/19/2017.
 *
 */

class SaveFile {
    //Math Stuff
    private int mLinesWritten; //for timestamp
    private double mIncrement;
    private int mByteResolution;

    File file;
    private CSVWriter csvWriter;
    private boolean initialized;
    private final static String TAG = SaveFile.class.getSimpleName();

    SaveFile(String directory, String fileName, int byteResolution, double increment) throws IOException {
        File root = Environment.getExternalStorageDirectory();
        if(root.canWrite()) {
            File dir = new File(root.getAbsolutePath()+directory);
            boolean resultMkdir = dir.mkdirs();
            if (!resultMkdir) {
                Log.e(TAG, "MKDIRS FAILED");
            }
            this.file = new File(dir, fileName+".csv");
            if (this.file.exists() && !this.file.isDirectory()) {
                Log.d(TAG, "File " + this.file.toString() + " already exists - appending data");
                FileWriter fileWriter = new FileWriter(this.file, true);
                this.csvWriter = new CSVWriter(fileWriter);
            } else {
                this.csvWriter = new CSVWriter(new FileWriter(this.file));
            }
            this.mByteResolution = byteResolution;
            this.mIncrement = increment;
            this.initialized = true;
        }
    }

    void exportDataWithTimestamp(byte[] ch1Bytes, byte[] ch2Bytes) throws IOException {
        if(this.initialized) {
            if(mByteResolution==2) {
                for (int i = 0; i < ch1Bytes.length/2; i++) {
                    exportDataDouble(DataChannel.bytesToDouble(ch1Bytes[2 * i], ch1Bytes[2 * i + 1]),
                            DataChannel.bytesToDouble(ch2Bytes[2 * i], ch2Bytes[2 * i + 1]));
                }
            } else {
                for (int i = 0; i < ch1Bytes.length/3; i++) {
                    exportDataDouble(DataChannel.bytesToDouble(ch1Bytes[3 * i], ch1Bytes[3 * i + 1], ch1Bytes[3 * i + 2]),
                            DataChannel.bytesToDouble(ch2Bytes[3 * i], ch2Bytes[3 * i + 1], ch2Bytes[3 * i + 2]));
                }
            }
        }
    }

    /**
     *
     * @param bytes split into 6 colns:
     *
     *
     */
    void exportDataWithTimestampMPU(byte[] bytes) {
        for (int i = 0; i < bytes.length/12; i++) {
            double ax = DataChannel.bytesToDoubleMPUAccel(bytes[12* i   ], bytes[12*i+ 1]);
            double ay = DataChannel.bytesToDoubleMPUAccel(bytes[12* i+ 2], bytes[12*i+ 3]);
            double az = DataChannel.bytesToDoubleMPUAccel(bytes[12* i+ 4], bytes[12*i+ 5]);
            double gx = DataChannel.bytesToDoubleMPUGyro (bytes[12* i+ 6], bytes[12*i+ 7]);
            double gy = DataChannel.bytesToDoubleMPUGyro (bytes[12* i+ 8], bytes[12*i+ 9]);
            double gz = DataChannel.bytesToDoubleMPUGyro (bytes[12* i+10], bytes[12*i+11]);
            exportDataDouble(ax, ay, az, gx, gy, gz);
        }
    }

    private void exportDataDouble(double a, double b) {
        String[] writeCSVValue = new String[3];
        double timestamp = (double)mLinesWritten*mIncrement;
        writeCSVValue[0] = timestamp+"";
        writeCSVValue[1] = a+"";
        writeCSVValue[2] = b+"";
        this.csvWriter.writeNext(writeCSVValue, false);
        this.mLinesWritten++;
    }

    /**
     * Writes 6 data points + timestamp
     * @param a accx
     * @param b accy
     * @param c accz
     * @param d gyrx
     * @param e gyry
     * @param f gyrz
     */
    private void exportDataDouble(double a, double b, double c, double d, double e, double f) {
        String[] writeCSVValue = new String[7];
        double timestamp = (double)mLinesWritten*mIncrement;
        writeCSVValue[0] = timestamp+"";
        writeCSVValue[1] = a+"";
        writeCSVValue[2] = b+"";
        writeCSVValue[3] = c+"";
        writeCSVValue[4] = d+"";
        writeCSVValue[5] = e+"";
        writeCSVValue[6] = f+"";
        this.csvWriter.writeNext(writeCSVValue, false);
        this.mLinesWritten++;
    }


    void terminateDataFileWriter() throws IOException {
        if(this.initialized) {
            this.csvWriter.flush();
            this.csvWriter.close();
            this.initialized = false;
        }
    }


}
