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

            return new MatchExpressionToken(tokenType, str.substring(0, length));
        }

        throw new KyouException("unrecognizable token. str: " + str);
    }
}
