package net.nuclearg.kyou;

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

@RunWith(PackTests.class)
public class PackTests extends ParentRunner<PackTestCase> {
    private static final String POSTFIX = ".testcase";

    private List<PackTestCase> tests;

    public PackTests(Class<?> testClass) throws InitializationError {
        super(testClass);

        try {
            this.tests = findTests();
        } catch (Exception ex) {
            throw new InitializationError(ex);
        }
    }

    @Override
    protected List<PackTestCase> getChildren() {
        return this.tests;
    }

    @Override
    protected Description describeChild(PackTestCase child) {
        return Description.createTestDescription(this.getClass(), child.name);
    }

    @Override
    protected void runChild(final PackTestCase child, RunNotifier notifier) {
        Statement statement = new Statement() {

            @Override
            public void evaluate() throws Throwable {
                byte[] output = Kyou.pack(child.doc, child.style);

                if (child.expectedString != null)
                    Assert.assertEquals(child.expectedString, new String(output, child.style.config.encoding));
                else
                    Assert.assertArrayEquals(child.expected, output);
            }
        };

        runLeaf(statement, describeChild(child), notifier);
    }

    private List<PackTestCase> findTests() throws Exception {
        File[] files = new File(URI.create(this.getClass().getResource(".").toString())).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(POSTFIX);
            }
        });

        List<PackTestCase> tests = new ArrayList<PackTestCase>();
        for (File file : files)
            tests.add(new PackTestCase(file));

        return tests;
    }
}
