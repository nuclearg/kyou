package net.nuclearg.kyou.pack.matcher.attribute;

import java.util.Map;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.pack.matcher.Matcher;
import net.nuclearg.kyou.util.ClassUtils;
import net.nuclearg.kyou.util.ClassUtils.AnnotationNameParser;

/**
 * 对属性进行判断的匹配器
 * 
 * @author ng
 * 
 */
public class AttributeMatcher extends Matcher {
    private static final Map<String, Class<? extends Operator>> OPERATOR_CLASSES = ClassUtils.buildAnnotatedClassMap(OperatorDescription.class, Operator.class, new AnnotationNameParser<OperatorDescription>() {

        @Override
        public String parseName(OperatorDescription annotation) {
            return annotation.value();
        }
    });

    /**
     * 属性名
     */
    private final String name;
    /**
     * 运算符
     */
    private final Operator op;
    /**
     * 属性值
     */
    private final String value;

    private AttributeMatcher(String name, Operator op, String value) {
        this.name = name;
        this.op = op;
        this.value = value;
    }

    @Override
    public boolean matches(KyouItem item) {
        return this.op.matches(this.value, item.attr(this.name));
    }

    @Override
    public String toString() {
        return "[" + this.name + " " + this.op.getClass().getSimpleName() + (this.value != null ? " " + this.value : "") + "]";
    }

    /**
     * 构建一个属性匹配器
     * 
     * @param attrName
     *            属性名称
     * @param attrOperator
     *            运算符
     * @param attrValue
     *            属性的值
     * @return 属性匹配器
     */
    public static Matcher buildAttributeMatcher(String attrName, String attrOperator, String attrValue) {
        if (attrOperator == null)
            attrOperator = "";

        Class<? extends Operator> opClass = OPERATOR_CLASSES.get(attrOperator);
        if (opClass == null)
            throw new KyouException("unsupported attribute operator. op: " + attrOperator);

        Operator op = ClassUtils.newInstance(opClass);

        return new AttributeMatcher(attrName, op, attrValue);
    }
}
