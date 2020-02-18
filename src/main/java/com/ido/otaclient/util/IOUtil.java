package com.ido.otaclient.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * IO 工具类
 */

public class IOUtil {

    /**
     * 关闭IO.可用于任何IO类的close操作
     * @param closeable
     */
    public static void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
                closeable = null;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
