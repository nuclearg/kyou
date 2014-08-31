package net.nuclearg.kyou;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.nuclearg.kyou.dom.KyouDocument;
import net.nuclearg.kyou.pack.KyouPackStyle;
import net.nuclearg.kyou.util.ByteOutputStream;
import net.nuclearg.kyou.util.FormatString;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

public class PackTestCase {
    final String name;

    final KyouDocument doc;
    final KyouPackStyle style;
    final byte[] expected;
    final String expectedString;

    PackTestCase(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        try {
            StringBuilder docBuilder = new StringBuilder();
            StringBuilder styleBuilder = new StringBuilder();
            StringBuilder expectedBuilder = new StringBuilder();

            StringBuilder builder = new StringBuilder();
            String ln;
            while ((ln = r.readLine()) != null) {
                if (ln.equals("[[DOC]]"))
                    builder = docBuilder;
                else if (ln.equals("[[STYLE]]"))
                    builder = styleBuilder;
                else if (ln.equals("[[EXPECTED]]"))
                    builder = expectedBuilder;
                else if (ln.equals("[[REMARK]]"))
                    builder = new StringBuilder();
                else
                    builder.append(ln).append(SystemUtils.LINE_SEPARATOR);
            }
            builder.delete(builder.length() - SystemUtils.LINE_SEPARATOR.length(), builder.length());

            this.name = file.getName().substring(0, file.getName().length() - ".testcase".length());
            this.doc = Kyou.loadDocument(docBuilder.toString());
            this.style = Kyou.loadPackStyle(styleBuilder.toString());

            @SuppressWarnings("resource")
            ByteOutputStream os = new ByteOutputStream();
            String expectedStr = expectedBuilder.toString();
            FormatString expected = new FormatString(expectedStr, this.style.config.encoding);
            for (byte[] segment : expected)
                if (segment == null)
                    throw new UnsupportedOperationException("result must simple");
                else
                    os.write(segment);

            this.expected = os.export();

            expectedStr = StringUtils.replace(expectedStr, "\\\\", "");
            expectedStr = StringUtils.replace(expectedStr, "\\%", "");
            if (expectedBuilder.toString().contains("\\"))
                this.expectedString = null;
            else
                this.expectedString = new String(this.expected, this.style.config.encoding);

        } finally {
            is.close();
            r.close();
        }
    }
}
