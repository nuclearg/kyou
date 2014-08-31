package net.nuclearg.kyou;

import java.io.InputStream;

import net.nuclearg.kyou.dom.KyouDocument;
import net.nuclearg.kyou.pack.StyleSpecification;

import org.apache.log4j.lf5.util.StreamUtils;
import org.junit.Assert;
import org.junit.Test;

public class Test1 {
    @Test
    public void test() throws Exception {
        InputStream docIs = this.getClass().getResourceAsStream("/test1_doc.xml");
        InputStream specIs = this.getClass().getResourceAsStream("/test1_style.xml");

        Kyou kyou = new Kyou();
        KyouDocument doc = kyou.loadDocument(docIs);
        StyleSpecification spec = kyou.loadStyleSpecification(specIs);

        byte[] bytes = kyou.pack(doc, spec);
        String result = new String(bytes, "utf8");

        InputStream expectedIs = this.getClass().getResourceAsStream("/test1_expect.txt");
        String expected = new String(StreamUtils.getBytes(expectedIs), "utf8");

        Assert.assertEquals(expected, result);
    }
}
