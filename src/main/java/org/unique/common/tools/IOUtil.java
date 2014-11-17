package org.unique.common.tools;

import java.io.Closeable;
import java.io.IOException;

/**
 * io util
 * @author biezhi
 * @version 0.1.0
 */
public class IOUtil {

    public static void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
