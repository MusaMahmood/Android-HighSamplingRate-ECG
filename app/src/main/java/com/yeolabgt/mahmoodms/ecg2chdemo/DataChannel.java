package com.yeolabgt.mahmoodms.ecg2chdemo;

import com.google.common.primitives.Bytes;

/**
 * Created by mmahmood31 on 9/19/2017.
 * For Handling BLE incoming data packets.
 */

class DataChannel {
    boolean chEnabled;
    private static boolean MSBFirst;
    byte[] characteristicDataPacketBytes;
    short packetCounter;
    int mTotalDataPointsPlotted;
    byte[] dataBuffer;

    DataChannel(boolean chEnabled, boolean MSBFirst) {
        this.packetCounter = 0;
        this.mTotalDataPointsPlotted = 0;
        this.chEnabled = chEnabled;
        setMSBFirst(MSBFirst);
    }

    private static void setMSBFirst(boolean MSBFirst) {
        DataChannel.MSBFirst = MSBFirst;
    }

    void resetBuffer() {
        this.dataBuffer = null;
        this.packetCounter = 0;
    }

    /**
     * If 'dataBuffer' is not null, concatenate new data using Guava lib
     * else: initialize dataBuffer with new data.
     *
     * @param newDataPacket new data packet received via BLE>
     */
    void handleNewData(byte[] newDataPacket) {
        this.characteristicDataPacketBytes = newDataPacket;
        if (this.dataBuffer != null) {
            this.dataBuffer = Bytes.concat(this.dataBuffer, newDataPacket);
        } else {
            this.dataBuffer = newDataPacket;
        }
        this.packetCounter++;
    }

    static double bytesToDoubleMPUAccel(byte a1, byte a2) {
        int unsigned;
        if (MSBFirst) {
            unsigned = unsignedBytesToInt(a2, a1);
        } else {
            unsigned = unsignedBytesToInt(a1, a2);
        }
        return ((double) unsignedToSigned16bit(unsigned) / 32767.0) * 16.0;
    }

    static double bytesToDoubleMPUGyro(byte a1, byte a2) {
        int unsigned;
        if (MSBFirst) {
            unsigned = unsignedBytesToInt(a2, a1);
        } else {
            unsigned = unsignedBytesToInt(a1, a2);
        }
        return ((double) unsignedToSigned16bit(unsigned) / 32767.0) * 4000.0;
    }

    static double bytesToDouble(byte a1, byte a2) {
        int unsigned;
        if (MSBFirst) {
            unsigned = unsignedBytesToInt(a2, a1);
        } else {
            unsigned = unsignedBytesToInt(a1, a2);
        }
        return ((double) unsignedToSigned16bit(unsigned) / 32767.0) * 2.25;
    }

    static int bytesToInt(byte a1, byte a2) {
        if (MSBFirst) {
            return unsignedToSigned16bit(unsignedBytesToInt(a2, a1));
        } else {
            return unsignedToSigned16bit(unsignedBytesToInt(a1, a2));
        }
    }

    static int bytesToInt(byte a1, byte a2, byte a3) {
        if (MSBFirst) {
            return unsignedToSigned24bit(unsignedBytesToInt(a3, a2, a1));
        } else {
            return unsignedToSigned24bit(unsignedBytesToInt(a1, a2, a3));
        }
    }


    static double bytesToDouble(byte a1, byte a2, byte a3) {
        int unsigned;
        if (MSBFirst) {
            unsigned = unsignedBytesToInt(a3, a2, a1);
        } else {
            unsigned = unsignedBytesToInt(a1, a2, a3);
        }
        return ((double) unsignedToSigned24bit(unsigned) / 8388607.0) * 2.25;
    }

    private static int unsignedToSigned16bit(int unsigned) {
        if ((unsigned & 0x8000) != 0) return -1 * (0x8000 - (unsigned & (0x8000 - 1)));
        else return unsigned;
    }

    private static int unsignedToSigned24bit(int unsigned) {
        if ((unsigned & 0x800000) != 0) return -1 * (0x800000 - (unsigned & (0x800000 - 1)));
        else return unsigned;
    }

    private static int unsignedBytesToInt(byte b0, byte b1) {
        return (unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8));
    }

    private static int unsignedBytesToInt(byte b0, byte b1, byte b2) {
        return (unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8) + (unsignedByteToInt(b2) << 16));
    }

    /*
        Non-size-dependent functions
     */

    private static int unsignedByteToInt(byte b) {
        return b & 0xFF;
    }

//    static int unsignedToSigned(int unsigned, int size) {
//        if ((unsigned & (1 << size - 1)) != 0) unsigned = -1 * ((1 << size - 1) - (unsigned & ((1 << size - 1) - 1)));
//        return unsigned;
//    }
//    static String bytesToHexString(byte[] a) {
//        StringBuilder s = new StringBuilder("");
//        for (byte b: a) {
//            s.append(String.format("%02X", b));
//        }
//        return s.toString();
//    }

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int i = 0; i < bytes.length; i++ ) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
