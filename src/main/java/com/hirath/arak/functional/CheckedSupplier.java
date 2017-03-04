package com.hirath.arak.functional;

@FunctionalInterface
public interface CheckedSupplier<T> {

	T get() throws Exception;
}
