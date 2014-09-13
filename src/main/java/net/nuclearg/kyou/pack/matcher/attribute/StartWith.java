package net.nuclearg.kyou.pack.matcher.attribute;

import org.apache.commons.lang.StringUtils;

/**
 * 判断报文节点的属性值是否以指定字符串开头
 * <p>
 * 例如，指定[attr^='start']，表示只匹配存在attr属性，且以start几个字符开头的报文节点
 * </p>
 * 
 * @author ng
 * 
 */
@OperatorDescription("^=")
class StartWith extends Operator {

    @Override
    boolean matches(String value, String attr) {
        if (StringUtils.isEmpty(attr))
            return false;

        return attr.startsWith(value);
    }

}
