package net.nuclearg.kyou.dom.query;

import org.apache.commons.lang.ArrayUtils;

/**
 * 组成查询字符串的词
 * 
 * @author ng
 * 
 */
class QueryToken {
    final QueryTokenType type;
    final String value;
    final int length;

    QueryToken(QueryTokenType type, String value, int length) {
        this.type = type;
        this.value = value;
        this.length = length;
    }

    /**
     * 尝试从字符串中解析出第一个词
     * 
     * @param str
     *            待解析的字符串
     * @param expected
     *            期望的词的类型
     * @return 解析出的词，或为null
     */
    static QueryToken tryParse(String str, QueryTokenType... expected) {
        for (QueryTokenType tokenType : QueryTokenType.values()) {
            int length = tokenType.matches(str);

            if (length < 0)
                continue;

            if (ArrayUtils.contains(expected, tokenType))
                return new QueryToken(tokenType, str.substring(0, length), length);
            else
                continue;
        }

        return null;
    }
}
