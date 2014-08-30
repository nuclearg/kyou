package net.nuclearg.kyou.util;

/**
 * Kyou在执行组包过程时支持的数据类型
 * 
 * @author ng
 * 
 */
public enum KyouValueType {
    /**
     * 报文文档树的一部分
     */
    Dom,

    /**
     * 整数
     */
    Integer,
    /**
     * 字符串
     */
    String,
    /**
     * 字节数组
     */
    Bytes,
    /**
     * 回退
     */
    Backspace,
}
