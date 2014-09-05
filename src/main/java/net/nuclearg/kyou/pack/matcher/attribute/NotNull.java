package net.nuclearg.kyou.pack.matcher.attribute;

import org.apache.commons.lang.StringUtils;

/**
 * 属性值不为空
 * 
 * @author ng
 * 
 */
class NotNull extends AttributeOperator {

    @Override
    boolean matches(String value, String attr) {
        return StringUtils.isNotEmpty(attr);
    }

}
