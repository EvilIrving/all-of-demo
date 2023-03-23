package com.ygkj.common.notify.functions;

/**
 * @author Huang.zh
 * @date 2021/4/10 12:32
 * @Description: 通用建造元模板
 */
public interface BuildFunction<T, R> {
    //单一参数构造方法，多参数不建议使用，建议包装成单一元数据作为构造函数的形参
    R apply(T t);
}
