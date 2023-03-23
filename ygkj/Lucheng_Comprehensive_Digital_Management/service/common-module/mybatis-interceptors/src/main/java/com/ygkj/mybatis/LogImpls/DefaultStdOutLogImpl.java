package com.ygkj.mybatis.LogImpls;

import org.apache.ibatis.logging.Log;

public class DefaultStdOutLogImpl implements Log {

    public DefaultStdOutLogImpl(String clazz) {

    }

    @Override
    public boolean isDebugEnabled() {
        return Boolean.FALSE;
    }

    @Override
    public boolean isTraceEnabled() {
        return Boolean.FALSE;
    }

    @Override
    public void error(String s, Throwable throwable) {
        System.err.println(s);
        throwable.printStackTrace(System.err);
    }

    @Override
    public void error(String s) {
        System.out.println(s);
    }

    @Override
    public void debug(String s) {
        //ignore
    }

    @Override
    public void trace(String s) {
        //ignore
    }

    @Override
    public void warn(String s) {
        System.out.println(s);
    }
}
