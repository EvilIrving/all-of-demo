package com.ygkj.big.screen.drill;

import org.springframework.context.ApplicationContext;

/**
 * @author xq
 * @Description
 * @Date 2021/7/9
 */
public abstract class AbstractDrillStrategy implements DrillStrategy {

    ApplicationContext context = null;

    public AbstractDrillStrategy(ApplicationContext context) {
        this.context = context;
    }
}
