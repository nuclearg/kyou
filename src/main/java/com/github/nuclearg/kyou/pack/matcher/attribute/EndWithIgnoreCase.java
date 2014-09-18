package com.github.nuclearg.kyou.pack.matcher.attribute;

import org.apache.commons.lang.StringUtils;

/**
 * 判断报文节点的属性值是否以指定字符串结尾
 * <p>
 * 不区分大小写
 * </p>
 * 
 * @example [attr$?='eNd']
 *          只匹配存在attr属性，且以end几个字符结尾的报文节点，不区分大小写
 * 
 * @author ng
 * 
 */
@OperatorDescription("$?=")
class EndWithIgnoreCase extends Operator {

    @Override
    boolean matches(String exprValue, String attrValue) {
        if (StringUtils.isEmpty(attrValue))
            return false;

        return attrValue.toLowerCase().endsWith(exprValue.toLowerCase());
    }

}
