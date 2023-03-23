package com.ygkj.entity;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author xq
 * @Description
 * @Date 2021/6/26
 */
public class ByteArrayMultipartFile implements MultipartFile, Serializable {
    private static final long serialVersionUID = -5989056797282595228L;

    private final byte[] part;

    private final String filename;

    private final InputStream inputStream;

    private final String contentType;

    public ByteArrayMultipartFile(byte[] part, String filename, String contentType) {
        this.part = part;
        this.filename = filename;
        this.inputStream = new ByteArrayInputStream(part);
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return System.currentTimeMillis() + Math.random() + filename.substring(filename.lastIndexOf("."));
    }

    @Override
    public String getOriginalFilename() {
        return filename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return (this.part.length == 0);
    }

    @Override
    public long getSize() {
        return this.part.length;
    }

    @Override
    public byte[] getBytes() {
        return part;
    }

    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(part);
    }
}
