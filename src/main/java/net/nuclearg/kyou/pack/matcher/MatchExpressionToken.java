package net.nuclearg.kyou.pack.matcher;

import net.nuclearg.kyou.KyouException;

/**
 * 组成查询字符串的词法元素
 * 
 * @author ng
 * 
 */
class MatchExpressionToken {
    /**
     * 词法元素类型
     */
    final MatchExpressionTokenType type;
    /**
     * 对应的文本
     */
    final String text;

    MatchExpressionToken(MatchExpressionTokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    /**
     * 尝试从字符串中解析出第一个词
     * 
     * @param str
     *            待解析的字符串
     * @return 解析出的词
     */
    static MatchExpressionToken tryParse(String str) {
        for (MatchExpressionTokenType tokenType : MatchExpressionTokenType.values()) {
            int length = tokenType.matches(str);

            if (length < 0)
                continue;

            // 如果是Filter类型，则需要检查后面是否跟了括号
            // 因为可以是嵌套括号，并且这种情况下java的正则是不支持的，所以只能对这种情况单独写代码来判断
            if (tokenType == MatchExpressionTokenType.Filter)
                length += probeParenthesesLength(str);

            return new MatchExpressionToken(tokenType, str.substring(0, length));
        }

        throw new KyouException("unrecognizable token. str: " + str);
    }

    /**
     * 匹配字符串中出现的成对括号
     * 
     * @param str
     *            待匹配的字符串
     * @return 匹配出来的成对括号
     */
    private static int probeParenthesesLength(String str) {
        if (str.startsWith("("))
            return 0;

        int depth = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(')
                depth++;
            if (str.charAt(i) == ')')
                depth--;
            if (depth == 0)
                return i;
        }

        return 0;
    }
}
