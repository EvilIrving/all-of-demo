package com.ygkj.big.screen.unit;

import org.springframework.context.ApplicationContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xq
 * @Description
 * @Date 2021/8/6
 */
public abstract class MemStackCachedUnitManager extends AbstractUnitManager {
    protected ConcurrentHashMap<String, Object> cache;

    public MemStackCachedUnitManager(ApplicationContext context) {
        super(context);
        cache = new ConcurrentHashMap<>();
    }

    public MemStackCachedUnitManager() {
        cache = new ConcurrentHashMap<>();
    }
}
