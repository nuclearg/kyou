package net.nuclearg.kyou.pack.matcher.attribute;

/**
 * 属性值不相等
 * 
 * @author ng
 * 
 */
@AttributeOperatorDescription("!=")
class NEQ extends AttributeOperator {

    @Override
    boolean matches(String value, String attr) {
        return !value.equals(attr);
    }

}
