package net.nuclearg.kyou.pack.expr;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.Expr.ExprDescription;

/**
 * 这个类毫无意义，单纯是用来定位存放各种表达式的包名
 * 
 * @author enji.lj
 * 
 */
public class EXPR_CLASSES {
    public static final Map<String, Class<? extends Expr>> classes;

    static {
        List<Class<? extends Expr>> exprClasses = Arrays.asList(
                ConvertB2SExpr.class,
                ConvertS2BExpr.class,
                MemberExpr.class,
                NameExpr.class,
                ValueExpr.class,
                BackspaceExpr.class,

                IntegerExpr.class
                );

        Map<String, Class<? extends Expr>> exprClassesMap = new HashMap<String, Class<? extends Expr>>();

        for (Class<? extends Expr> exprClass : exprClasses) {
            ExprDescription desc = exprClass.getAnnotation(ExprDescription.class);
            if (desc == null)
                throw new KyouException("expr class without desc. class: " + exprClass);

            String name = desc.name();
            if (exprClassesMap.containsKey(name))
                throw new KyouException("expr name duplicate. name: " + name + ", current: " + exprClass + ", old: " + exprClassesMap.get(name));

            exprClassesMap.put(name, exprClass);
        }

        classes = Collections.unmodifiableMap(exprClassesMap);
    }
}
