
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.SystemUtils;

class PackTestData {
    final String name;

    final String doc;
    final String style;
    final String expected;

    PackTestData(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        try {
            StringBuilder docBuilder = new StringBuilder();
            StringBuilder styleBuilder = new StringBuilder();
            StringBuilder expectedBuilder = new StringBuilder();

            StringBuilder builder = new StringBuilder();
            String ln;
            while ((ln = r.readLine()) != null)
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
            builder.delete(builder.length() - SystemUtils.LINE_SEPARATOR.length(), builder.length());

            this.name = file.getName().substring(0, file.getName().length() - ".testcase".length());
            this.doc = docBuilder.toString();
            this.style = styleBuilder.toString();
            this.expected = expectedBuilder.toString();
        } finally {
            is.close();
            r.close();
        }
    }
}
