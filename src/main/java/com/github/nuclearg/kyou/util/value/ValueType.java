package com.github.nuclearg.kyou.util.value;

/**
 * Kyou在执行组包过程时支持的数据类型
 * 
 * @author ng
 * 
 */
public enum ValueType {
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

    // ---- 从这里开始是kyou内部使用

    /**
     * 表示一个不应该存在的值类型
     */
    Null,

    /**
     * 回退
     */
    Backspace,

    /**
     * 引用其它参数
     */
    RefParam,
}
