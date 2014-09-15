package com.github.nuclearg.kyou;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.github.nuclearg.kyou.Kyou;
import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.dom.KyouDocument;

public class KyouTest {

    @Test
    public void testLoadDocumentInputStream() {
        try {
            Kyou.loadDocument((InputStream) null);
            Assert.fail();
        } catch (KyouException ex) {

        }
    }

    @Test
    public void testLoadDocumentString() {
        try {
            Kyou.loadDocument("  ");
            Assert.fail();
        } catch (KyouException ex) {

        }
    }

    //
    // @Test
    // public void testSaveDocument() {
    // fail("Not yet implemented");
    // }

    @Test
    public void testLoadPackStyleInputStream() {
        try {
            Kyou.loadPackStyle((InputStream) null);
            Assert.fail();
        } catch (KyouException ex) {

        }
    }

    @Test
    public void testLoadPackStyleString() {
        try {
            Kyou.loadPackStyle("  ");
            Assert.fail();
        } catch (KyouException ex) {

        }
    }

    @Test
    public void testPack() {
        try {
            Kyou.pack(null, null);
            Assert.fail();
        } catch (KyouException ex) {
        }

        try {
            Kyou.pack(new KyouDocument(), null);
            Assert.fail();
        } catch (KyouException ex) {
        }
    }
}
