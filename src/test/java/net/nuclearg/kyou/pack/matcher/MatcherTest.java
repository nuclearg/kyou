package net.nuclearg.kyou.pack.matcher;

import org.junit.Assert;
import org.junit.Test;

public class MatcherTest {
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
        test("[attr]");
        test("['attr']");
        test("[a='3']");
        test("['a'='3']");
        test("[e!='x\\'xx']");
        try {
            test("[a=3]");
            Assert.fail();
        } catch (Exception ex) {
        }
    }

    @Test
    public void 过滤器() {
        test(":empty");
        test(":nth-child(1)");
        test(":nth-child('3')");
        try {
            test(":asdf(汉字)");
        } catch (Exception ex) {
        }
    }

    @Test
    public void 混成一坨() {
        test("field['b']");
        test("struct#name field");
        test("struct#kkk[a='3']:nth-child(12)");
        test("s2b.gbk aligns[align='r', padding='\\'', len='5'] v");
        test("#a , #b");
        test("struct > #aaa[asdf^='ddd']");
        test("struct > field , array > field, #head field");
    }

    private void test(String str) {
        try {
            System.out.print(str + " ===> ");
            Matcher matcher = Matcher.parse(str);
            System.out.print(matcher);
            Assert.assertNotNull(matcher);
        } finally {
            System.out.println();
        }
    }
}
