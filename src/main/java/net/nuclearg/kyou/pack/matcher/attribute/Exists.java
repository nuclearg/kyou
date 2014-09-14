package net.nuclearg.kyou.pack.matcher.attribute;

import org.apache.commons.lang.StringUtils;

/**
 * 判断报文节点是否存在指定的属性
 * <p>
 * 如果属性存在但其值为null，认为其不存在，匹配失败。<br/>
 * 如果属性存在但其值为空字符串，认为其存在，匹配成功
 * </p>
 * 
 * @example [attr]
 *          只匹配存在attr属性的报文节点，但不关心其值。
 * 
 * 
 * @author ng
 * 
 */
@OperatorDescription("")
class Exists extends Operator {

    @Override
    boolean matches(String value, String attr) {
        return StringUtils.isNotEmpty(attr);
    }

}
