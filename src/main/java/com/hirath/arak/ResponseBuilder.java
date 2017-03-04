package com.hirath.arak;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hirath.arak.functional.CheckedFunction;
import com.hirath.arak.functional.CheckedSupplier;

public class ResponseBuilder<T,E> {

	private static Map<String,Object> primitives = new HashMap<>();;
	static{
		primitives.put("byte", 0);
		primitives.put("short", 0);
		primitives.put("int", 0);
		primitives.put("long", 0L);
		primitives.put("float", 0.0f);
		primitives.put("double", 0.0d);
		primitives.put("char", '\u0000');
		primitives.put("boolean", false);
	}
	
	private Set<String> methods = new HashSet<>();
	private CheckedSupplier<E> supplier;
	private Class<T> clazz;
	private HolderBuilder<T> parent;


	public ResponseBuilder(HolderBuilder<T> parent, Class<T> clazz, CheckedSupplier<E> supplier) {
		this.clazz = clazz;
		this.parent = parent;
		this.supplier = supplier;
	}

	public HolderBuilder<T> when(CheckedFunction<T,E> consumer){
		methods.clear();
		T value = (T) Proxy.newProxyInstance(clazz.getClassLoader(),
				new Class<?>[] { clazz },this::invoque);
		try {
			consumer.apply(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parent;
	}

	public Set<String> getMethods() {
		return methods;
	}

	public CheckedSupplier<E> getSupplier() {
		return supplier;
	}

	private Object invoque(Object proxy, Method method, Object[] args) throws InstantiationException, IllegalAccessException{
		methods.add(method.getName());
		Class<?> returnType = method.getReturnType();
		if (returnType.isPrimitive()) {
			return primitives.get(returnType.getName());
		}else{
			return null;
		}
	}
}
