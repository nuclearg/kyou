package net.nuclearg.kyou.pack.matcher.attribute;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.pack.matcher.Matcher;
import net.nuclearg.kyou.util.ClassUtils;

import org.apache.commons.lang.StringUtils;

/**
 * 对属性进行判断的匹配器
 * 
 * @author ng
 * 
 */
public class AttributeMatcher extends Matcher {
    private static final Map<String, Class<? extends AttributeOperator>> OPERATOR_CLASSES;

    /**
     * 属性名
     */
    private final String name;
    /**
     * 运算符
     */
    private final AttributeOperator op;
    /**
     * 属性值
     */
    private final String value;

    static {
        Map<String, Class<? extends AttributeOperator>> classes = new HashMap<String, Class<? extends AttributeOperator>>();

        for (Class<?> cls : ClassUtils.searchClassesWithAnnotation(AttributeOperatorDescription.class)) {
            AttributeOperatorDescription annotation = cls.getAnnotation(AttributeOperatorDescription.class);
            String name = annotation.value();

            if (classes.containsKey(name))
                throw new KyouException("attribute operator name duplicated. old: " + classes.get(name) + ", new: " + cls);

            classes.put(annotation.value(), cls.asSubclass(AttributeOperator.class));
        }

        OPERATOR_CLASSES = Collections.unmodifiableMap(classes);
    }

    public AttributeMatcher(String text) {
        // 去掉头尾的中括号
        text = text.substring(1, text.length() - 1);

        for (String op : OPERATOR_CLASSES.keySet())
            if (text.contains(op))
                try {
                    this.name = text.substring(0, text.indexOf(op));
                    this.op = ClassUtils.newInstance(OPERATOR_CLASSES.get(op));
                    this.value = text.substring(text.indexOf(op) + op.length());

                    if (StringUtils.isEmpty(this.name))
                        throw new KyouException("attribute name is empty. expr: [" + text + "]");
                    if (StringUtils.isEmpty(this.value))
                        throw new KyouException("attribute value is empty. expr: [" + text + "]");

                    return;
                } catch (Exception ex) {
                    throw new KyouException("init attribute operator fail. text: " + text, ex);
                }

        this.name = text;
        this.op = new NotNull();
        this.value = null;
    }

    @Override
    public boolean matches(KyouItem item) {
        String attr = item.attr(this.name);
        return this.op.matches(this.value, attr);
    }

    @Override
    public String toString() {
        return "[" + this.name + this.op + (this.value != null ? this.value : "") + "]";
    }

    public static Matcher buildAttributeMatcher(String attrName, String op, String attrValue) {
        return null;
    }
}
