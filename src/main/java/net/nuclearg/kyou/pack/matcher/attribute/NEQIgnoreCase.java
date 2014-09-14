package net.nuclearg.kyou.pack.matcher.attribute;

/**
 * 判断报文节点的属性值是否与指定字符串不同
 * <p>
 * 如果报文节点不存在该属性，则匹配失败
 * <p>
 * 不区分大小写
 * </p>
 * 
 * @example [attr!?='vAl']
 *          只匹配存在attr属性，且值不为val的报文节点，不区分大小写
 * 
 * @author ng
 * 
 */
@OperatorDescription("!?=")
class NEQIgnoreCase extends Operator {

    @Override
    boolean matches(String value, String attr) {
        if (attr == null)
            return false;

        return !value.equalsIgnoreCase(attr);
    }

}
