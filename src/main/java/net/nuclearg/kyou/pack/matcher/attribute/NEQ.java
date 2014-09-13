package net.nuclearg.kyou.pack.matcher.attribute;

/**
 * 判断报文节点的属性值是否与指定字符串不同
 * <p>
 * 例如，指定[attr!='val']，表示只匹配存在attr属性，且值不为val的报文节点
 * </p>
 * 
 * @author ng
 * 
 */
@OperatorDescription("!=")
class NEQ extends Operator {

    @Override
    boolean matches(String value, String attr) {
        if (attr == null)
            return false;

        return !value.equals(attr);
    }

}
