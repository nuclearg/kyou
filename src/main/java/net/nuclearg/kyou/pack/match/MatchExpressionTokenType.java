package net.nuclearg.kyou.pack.match;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询字符串的词法元素的类型
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

    /**
     * 空白
     */
    Space("^\\s+"),

    /**
     * 过滤器
     */
    Filter("^\\:[a-z]+"),

    /**
     * 属性
     */
    Attribute("oh fuck 这玩意的正则该怎么写"),

    ;

    private final Pattern regex;

    private MatchExpressionTokenType(String regex) {
        this.regex = Pattern.compile(regex);
    }

    /**
     * 判断给定字符串是否与当前词法元素匹配，并给出匹配的长度
     * 
     * @param str
     *            要进行匹配的字符串
     * @return 匹配的长度，如果无法匹配则返回-1
     */
    int matches(String str) {
        Matcher match = this.regex.matcher(str);
        if (match.find())
            return match.end() - match.start();
        else
            return -1;
    }
}
