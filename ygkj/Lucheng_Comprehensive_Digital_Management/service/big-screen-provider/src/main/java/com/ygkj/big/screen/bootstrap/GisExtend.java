package com.ygkj.big.screen.bootstrap;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * @author xq
 * @Description
 * @Date 2021/7/11
 */
public class GisExtend {
    String gisExtend;

    public GisExtend() {
        this.gisExtend = "";
        build();
    }

    public String getGisExtend() {
        return gisExtend;
    }

    public void build() {
        InputStream stream = null;
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            org.springframework.core.io.Resource[] resources = resolver.getResources("classpath:gis/gis_extent");
            org.springframework.core.io.Resource resource = resources[0];
            //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流

            stream = resource.getInputStream();
            StringBuilder buffer = new StringBuilder();
            byte[] bytes = new byte[1024];
            try {
                for (int n; (n = stream.read(bytes)) != -1; ) {
                    buffer.append(new String(bytes, 0, n));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            gisExtend = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
