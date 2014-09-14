import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

class PackTestData {
    final String name;
    final List<String> configs;

    final String doc;
    final String style;
    final String expected;

    PackTestData(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        try {
            StringBuilder configBuilder = new StringBuilder();
            StringBuilder docBuilder = new StringBuilder();
            StringBuilder styleBuilder = new StringBuilder();
            StringBuilder expectedBuilder = new StringBuilder();

            StringBuilder builder = new StringBuilder();
            String ln;
            while ((ln = r.readLine()) != null) {
                if (ln.equals("[[CONFIG]]"))
                    builder = configBuilder;
                else if (ln.equals("[[DOC]]"))
                    builder = docBuilder;
                else if (ln.equals("[[STYLE]]"))
                    builder = styleBuilder;
                else if (ln.equals("[[EXPECTED]]"))
                    builder = expectedBuilder;
                else if (ln.matches("\\[\\[\\w+\\]\\]"))
                    builder = new StringBuilder();
                else
                    builder.append(ln).append("\r\n");
            }
            builder.delete(builder.length() - "\r\n".length(), builder.length());

            this.name = file.getName().substring(0, file.getName().length() - ".testcase".length());
            this.configs = Arrays.asList(StringUtils.split(configBuilder.toString(), SystemUtils.LINE_SEPARATOR));
            this.doc = docBuilder.toString();
            this.style = styleBuilder.toString();
            this.expected = expectedBuilder.toString();
        } finally {
            is.close();
            r.close();
        }
    }
}
