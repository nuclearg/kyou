
import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import net.nuclearg.kyou.Kyou;
import net.nuclearg.kyou.dom.KyouDocument;
import net.nuclearg.kyou.pack.KyouPackStyle;
import net.nuclearg.kyou.util.ByteOutputStream;
import net.nuclearg.kyou.util.FormatString;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

@RunWith(PackTests.class)
public class PackTests extends ParentRunner<PackTestData> {
    private static final String POSTFIX = ".testcase";

    private List<PackTestData> tests;

    public PackTests(Class<?> testClass) throws InitializationError {
        super(testClass);

        try {
            this.tests = findTests();
        } catch (Exception ex) {
            throw new InitializationError(ex);
        }
    }

    @Override
    protected List<PackTestData> getChildren() {
        return this.tests;
    }

    @Override
    protected Description describeChild(PackTestData child) {
        return Description.createSuiteDescription(child.name);
    }

    @Override
    protected void runChild(final PackTestData child, RunNotifier notifier) {
        Statement statement = new Statement() {

            @Override
            public void evaluate() throws Throwable {
                // doc
                KyouDocument doc = Kyou.loadDocument(child.doc);
                // style
                KyouPackStyle style = Kyou.loadPackStyle(child.style);

                // expected bytes
                @SuppressWarnings("resource")
                ByteOutputStream os = new ByteOutputStream();
                FormatString expected = new FormatString(child.expected, style.config.encoding);
                for (byte[] segment : expected)
                    if (segment == null)
                        throw new UnsupportedOperationException("result must simple");
                    else
                        os.write(segment);
                byte[] expectedBytes = os.export();

                // expected string
                String expectedStr = StringUtils.replace(child.expected, "\\\\", "");
                expectedStr = StringUtils.replace(expectedStr, "\\%", "");
                if (child.expected.contains("\\"))
                    expectedStr = null;
                else
                    expectedStr = new String(expectedBytes, style.config.encoding);

                /*
                 * pack
                 */
                byte[] output = Kyou.pack(doc, style);

                /*
                 * compare
                 */
                if (expectedStr != null)
                    Assert.assertEquals(expectedStr, new String(output, style.config.encoding));
                else
                    Assert.assertArrayEquals(expectedBytes, output);
            }
        };

        runLeaf(statement, describeChild(child), notifier);
    }

    private List<PackTestData> findTests() throws Exception {
        File[] files = new File(URI.create(this.getClass().getResource(".").toString())).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(POSTFIX);
            }
        });

        List<PackTestData> tests = new ArrayList<PackTestData>();
        for (File file : files)
            tests.add(new PackTestData(file));

        return tests;
    }
}
