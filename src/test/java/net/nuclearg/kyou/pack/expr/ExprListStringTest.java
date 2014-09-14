package net.nuclearg.kyou.pack.expr;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ExprListStringTest {
    @Test
    public void test() {
        test("s2b.gbk aligns[align='r',padding='\\'',len='5'] v");
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
