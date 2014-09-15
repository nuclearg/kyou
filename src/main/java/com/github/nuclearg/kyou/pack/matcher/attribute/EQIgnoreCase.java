package com.github.nuclearg.kyou.pack.matcher.attribute;

/**
 * 判断报文节点的属性值是否与指定字符串相等
 * <p>
 * 不区分大小写
 * </p>
 * 
 * @example [attr?='vAl']
 *          只匹配存在attr属性，且值为val的报文节点，不匹配大小写
 * 
 * @author ng
 * 
 */
@OperatorDescription("?=")
class EQIgnoreCase extends Operator {

    @Override
    boolean matches(String value, String attr) {
        return value.equalsIgnoreCase(attr);
    }

}
