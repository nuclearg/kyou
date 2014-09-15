import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.github.nuclearg.kyou.Kyou;
import com.github.nuclearg.kyou.dom.KyouDocument;
import com.github.nuclearg.kyou.pack.KyouPackStyle;
import com.github.nuclearg.kyou.util.ByteOutputStream;

@RunWith(PackTests.class)
public class PackTests extends ParentRunner<PackTestData> {
    private static final String POSTFIX = ".testcase";

    private List<PackTestData> tests;

    public PackTests(Class<?> testClass) throws InitializationError {
        super(testClass);

        try {
            this.tests = this.findTests();
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

                // 组包
                byte[] output = Kyou.pack(doc, style);

                // 判断是走字符串还是走字节
                if (child.configs.contains("BYTES_TARGET")) {
                    // 走字节
                    @SuppressWarnings("resource")
                    ByteOutputStream os = new ByteOutputStream();
                    StyleFormatString expected = new StyleFormatString(child.expected, style.config.encoding);
                    for (byte[] segment : expected)
                        if (segment == null)
                            throw new UnsupportedOperationException("result must simple");
                        else
                            os.write(segment);
                    byte[] expectedBytes = os.export();

                    Assert.assertArrayEquals(expectedBytes, output);
                } else {
                    // 走字符串
                    String outputStr = new String(output, style.config.encoding);
                    Assert.assertEquals(child.expected, outputStr);
                }
            }
        };

        this.runLeaf(statement, this.describeChild(child), notifier);
    }

    private List<PackTestData> findTests() throws Exception {
        File[] files = new File(URI.create(this.getClass().getResource(".").toString())).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(POSTFIX);
            }
        });

        List<PackTestData> tests = new ArrayList<>();
        for (File file : files)
            tests.add(new PackTestData(file));

        return tests;
    }
}
