package com.github.nuclearg.kyou.util;

import java.nio.charset.Charset;

/**
 * 字节处理工具类
 * 
 * @author ng
 * 
 */
public class ByteUtils {
    public static final Charset UTF8 = Charset.forName("utf8");
    public static final Charset GB2312 = Charset.forName("gb2312");
    public static final Charset GBK = Charset.forName("gbk");

    /**
     * 将8位整数写成字节
     * 
     * @param value
     *            整数
     * @return 表示该整数的字节
     */
    public static byte writeInteger8(long value) {
        return (byte) value;
    }

    /**
     * 将16位整数写成字节
     * 
     * @param value
     *            整数
     * @param endian
     *            字节序 <li>true - 大尾</li><li>false - 小尾</li>
     * @return 表示该整数的字节
     */
    public static byte[] writeInteger16(long value, boolean endian) {
        byte b0 = (byte) (value >> 8);
        byte b1 = (byte) (value & 0xFF);

        byte[] buff = new byte[2];
        if (endian) {
            buff[0] = b0;
            buff[1] = b1;
        } else {
            buff[0] = b1;
            buff[1] = b0;
        }

        return buff;
    }

    /**
     * 将32位整数写成字节
     * 
     * @param value
     *            整数
     * @param endian
     *            字节序 <li>true - 大尾</li><li>false - 小尾</li>
     * @return 表示该整数的字节
     */
    public static byte[] writeInteger32(long value, boolean endian) {
        byte b0 = (byte) (value >> 24);
        byte b1 = (byte) ((value & 0xFF0000) >> 16);
        byte b2 = (byte) ((value & 0xFF00) >> 8);
        byte b3 = (byte) (value & 0xFF);

        byte[] buff = new byte[4];

        if (endian) {
            buff[0] = b0;
            buff[1] = b1;
            buff[2] = b2;
            buff[3] = b3;
        } else {
            buff[0] = b3;
            buff[1] = b2;
            buff[2] = b1;
            buff[3] = b0;
        }

        return buff;
    }

    /**
     * 将64位的整数写成字节
     * 
     * @param value
     *            64位整数
     * @param endian
     *            字节序 <li>true - 大尾</li><li>false - 小尾</li>
     * @return 保存该整数的字节数组
     */
    public static byte[] writeInteger64(long value, boolean endian) {
        int i1 = (int) (value >> 32);
        int i2 = (int) (value & 0xFFFFFFFF);

        byte[] buff = new byte[8];

        if (endian) {
            System.arraycopy(writeInteger32(i1, endian), 0, buff, 0, 4);
            System.arraycopy(writeInteger32(i2, endian), 0, buff, 4, 4);
        } else {
            System.arraycopy(writeInteger32(i2, endian), 0, buff, 0, 4);
            System.arraycopy(writeInteger32(i1, endian), 0, buff, 4, 4);
        }

        return buff;
    }

    /**
     * 将32位浮点数写成字节
     * 
     * @param value
     *            32位浮点数
     * @param endian
     *            字节序 <li>true - 大尾</li><li>false - 小尾</li>
     * @return 保存该浮点数的字节数组
     */
    public static byte[] writeFloat(float value, boolean endian) {
        int val = Float.floatToIntBits(value);
        return writeInteger32(val, endian);
    }

    /**
     * 将64位浮点数写成字节
     * 
     * @param value
     *            64位浮点数
     * @param endian
     *            字节序 {true}为big endian {false}为small endian
     * @return 保存该浮点数的字节数组
     */
    public static byte[] writeDouble(double value, boolean endian) {
        long val = Double.doubleToLongBits(value);
        return writeInteger64(val, endian);
    }
}
