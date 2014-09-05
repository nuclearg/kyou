package net.nuclearg.kyou.pack.matcher.attribute;

import org.apache.commons.lang.StringUtils;

/**
 * 属性值以期望的字符串结尾
 * 
 * @author ng
 * 
 */
@AttributeOperatorDescription("$=")
class EndWith extends AttributeOperator {

    @Override
    boolean matches(String value, String attr) {
        if (StringUtils.isEmpty(attr))
            return false;

        return attr.endsWith(value);
    }

}
