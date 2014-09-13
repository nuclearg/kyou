package net.nuclearg.kyou.pack.matcher;

import net.nuclearg.kyou.pack.matcher.MatchString.Lex;
import net.nuclearg.kyou.pack.matcher.MatchString.Syntax;
import net.nuclearg.kyou.util.parser.SyntaxString;
import net.nuclearg.kyou.util.parser.SyntaxTreeNode;

import org.junit.Assert;
import org.junit.Test;

public class MatcherStrTest {
    @Test
    public void 绝对路径() {
        test("#.a.b.c.d");
    }

    @Test
    public void 节点类型() {
        test("document");
        test("field");
        test("array");
        test("struct");

        try {
            test("xxx");
            Assert.fail();
        } catch (Exception ex) {
        }
    }

    @Test
    public void 节点名称() {
        test("#asdf");

        test("#_a3B2");

        try {
            test("#汉字");
            Assert.fail();
        } catch (Exception ex) {
        }
    }

    @Test
    public void 属性() {
        test("[a='3']");
        test("['a'='3']");
        try {
            test("[a=3]");
            Assert.fail();
        } catch (Exception ex) {
        }
    }

    @Test
    public void 过滤器() {
        test(":asdf");
        test(":asdf(1)");
        test(":asdf('asdf')");
        try {
            test(":asdf(汉字)");
        } catch (Exception ex) {
        }
    }

    @Test
    public void 混成一坨() {
        test("struct#kkk[a='3']:asdf(12)");
        test("#a , #b");
        test("struct > #aaa[asdf^='ddd']");
    }

    private void test(String str) {
        SyntaxString<Lex, Syntax> syntaxStr = new SyntaxString<>(str);
        SyntaxTreeNode<Lex, Syntax> root = syntaxStr.parse(Syntax.Root);
        System.out.println(root);
    }
}
