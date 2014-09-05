package net.nuclearg.kyou.pack.matcher.attribute;

import org.apache.commons.lang.StringUtils;

/**
 * 属性值以期望的字符串开头
 * 
 * @author ng
 * 
 */
@AttributeOperatorDescription("^=")
class StartWith extends AttributeOperator {

    @Override
    boolean matches(String value, String attr) {
        if (StringUtils.isEmpty(attr))
            return false;

        return attr.startsWith(value);
    }

}
