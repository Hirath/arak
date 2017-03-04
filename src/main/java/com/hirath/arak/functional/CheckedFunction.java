package com.hirath.arak.functional;

@FunctionalInterface
public interface CheckedFunction<T,E> {

	E apply(T param) throws Exception;
}
