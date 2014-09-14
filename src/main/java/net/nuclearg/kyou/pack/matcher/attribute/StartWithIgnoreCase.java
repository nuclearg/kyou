package net.nuclearg.kyou.pack.matcher.attribute;

import org.apache.commons.lang.StringUtils;

/**
 * 判断报文节点的属性值是否以指定字符串开头
 * <p>
 * 不区分大小写
 * </p>
 * 
 * @example [attr^?='sTaRt']
 *          匹配存在attr属性，且以start几个字符开头的报文节点，不区分大小写
 * 
 * @author ng
 * 
 */
@OperatorDescription("^?=")
class StartWithIgnoreCase extends Operator {

    @Override
    boolean matches(String value, String attr) {
        if (StringUtils.isEmpty(attr))
            return false;

        return attr.startsWith(value);
    }

}
