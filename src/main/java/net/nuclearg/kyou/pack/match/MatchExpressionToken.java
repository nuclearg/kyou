package net.nuclearg.kyou.pack.match;

/**
 * 组成查询字符串的词
 * 
 * @author ng
 * 
 */
class MatchExpressionToken {
    final MatchExpressionTokenType type;
    final String value;
    final int length;

    MatchExpressionToken(MatchExpressionTokenType type, String value, int length) {
        this.type = type;
        this.value = value;
        this.length = length;
    }

    /**
     * 尝试从字符串中解析出第一个词
     * 
     * @param str
     *            待解析的字符串
     * @return 解析出的词，或为null
     */
    static MatchExpressionToken tryParse(String str) {
        for (MatchExpressionTokenType tokenType : MatchExpressionTokenType.values()) {
            int length = tokenType.matches(str);

            if (length < 0)
                continue;

            return new MatchExpressionToken(tokenType, str.substring(0, length), length);
        }

        return null;
    }
}
