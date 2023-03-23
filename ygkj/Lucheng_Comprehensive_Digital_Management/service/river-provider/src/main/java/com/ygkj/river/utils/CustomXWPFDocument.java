package com.ygkj.river.utils;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;

public class CustomXWPFDocument extends XWPFDocument {

    public CustomXWPFDocument(OPCPackage in) throws IOException {
        super(in);
    }

    public CustomXWPFDocument() {
        super();
    }
}
