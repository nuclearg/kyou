package net.nuclearg.kyou.pack.match;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询字符串的词法元素定义
 * 
 * @author ng
 * 
 */
enum MatchExpressionTokenType {
    /**
     * 绝对路径
     */
    AbsolutePath("^\\#(\\.[0-9a-zA-Z]+)*$"),

    /**
     * 类型
     */
    Type("^(document|field|struct|array)"),
    
    
    /**
     * 节点名称
     */
    NodeName("^\\#[0-9a-zA-Z-_]+"),

    // AttributeStart,
    // AttributeName,
    // AttributeOperator,
    // AttributeValue,
    // AttributeEnd,
    //
    // And,
    //
    // Then,

    ;

    private final Pattern regex;

    private MatchExpressionTokenType(String regex) {
        this.regex = Pattern.compile(regex);
    }

    /**
     * 判断
     * 
     * @param str
     * @return
     */
    int matches(String str) {
        Matcher match = this.regex.matcher(str);
        if (match.find())
            return match.end() - match.start();
        else
            return -1;
    }
}
