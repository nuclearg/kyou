package com.github.nuclearg.kyou.pack.matcher.attribute;

import org.apache.commons.lang.StringUtils;

/**
 * 判断报文节点的属性值是否以指定字符串开头
 * <p>
 * 区分大小写
 * </p>
 * 
 * @example [attr^='start']
 *          匹配存在attr属性，且以start几个字符开头的报文节点
 * 
 * @author ng
 * 
 */
@OperatorDescription("^=")
class StartWith extends Operator {

    @Override
    boolean matches(String exprValue, String attrValue) {
        if (StringUtils.isEmpty(attrValue))
            return false;

        return attrValue.startsWith(exprValue);
    }

}
