package com.ygkj.utils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author xq
 * @Description
 * @Date 2021/8/5
 */
public class OptionalString {
    private final String value;

    private static final OptionalString EMPTY = new OptionalString();

    private OptionalString() {
        this.value = null;
    }

    private OptionalString(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static OptionalString ofNullable(String value) {
        return value == null ? empty() : of(value);
    }

    public static OptionalString ofBlank(String value) {
        return StringUtils.isBlank(value) ? empty() : of(value);
    }

    public static OptionalString empty() {
        return EMPTY;
    }

    public static OptionalString of(String value) {
        return new OptionalString(value);
    }

    public String get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isNullPresent() {
        return value != null;
    }

    public boolean isBlankPresent() {
        return StringUtils.isNotBlank(value);
    }

    public void ifNullPresent(Consumer<String> consumer) {
        if (value != null)
            consumer.accept(value);
    }

    public void ifBlankPresent(Consumer<String> consumer) {
        if (StringUtils.isNotBlank(value))
            consumer.accept(value);
    }

    public String orNullElse(String other) {
        return value != null ? value : other;
    }

    public String orBlankElse(String other) {
        return StringUtils.isNotBlank(value) ? value : other;
    }

    public String orNullElseGet(Supplier<String> other) {
        return value != null ? value : other.get();
    }

    public String orBlankElseGet(Supplier<String> other) {
        return StringUtils.isNotBlank(value) ? value : other.get();
    }
}
