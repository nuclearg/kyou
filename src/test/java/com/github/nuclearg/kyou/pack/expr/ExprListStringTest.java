package com.github.nuclearg.kyou.pack.expr;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.nuclearg.kyou.pack.expr.Expr;

public class ExprListStringTest {
    @Test
    public void test() {
        test("s2b.'gbk' a.'我人有的和\\‘主产不为这'");
        test("s2b.gbk aligns[align='r',padding='\\'',len='5'] v");
        test("s2b aligns[align='r',padding=' ',len=%2] v");
    }

    private void test(String str) {
        try {
            System.out.print(str + " ===> ");
            List<Expr> list = Expr.parseExprList(str);
            System.out.print(list);
            Assert.assertNotNull(list);
        } finally {
            System.out.println();
        }
    }
}
