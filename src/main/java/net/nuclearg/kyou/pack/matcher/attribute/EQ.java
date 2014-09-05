package net.nuclearg.kyou.pack.matcher.attribute;

/**
 * 属性值相等
 * 
 * @author ng
 * 
 */
@AttributeOperatorDescription("=")
class EQ extends AttributeOperator {

    @Override
    boolean matches(String value, String attr) {
        return value.equals(attr);
    }

}
