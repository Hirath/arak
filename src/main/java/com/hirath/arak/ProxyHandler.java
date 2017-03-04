package com.hirath.arak;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hirath.arak.functional.CheckedSupplier;

public class ProxyHandler<T> implements InvocationHandler {
    
    private Map<String, CheckedSupplier<T>> values;
    private Map<String, CheckedSupplier<T>> defaults;

    
	public ProxyHandler(Map<String, CheckedSupplier<T>> values, List<Object> defaults) {
		this.values = values;
		this.defaults = defaults.stream()
				                .map(this::mapObjectMethods)
				                .reduce((map1,map2) -> {map2.putAll(map1); return map2;})
				                .orElse(new HashMap<>());
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		return values.getOrDefault(methodName, defaults.getOrDefault(methodName, () -> {throw new NoResponseForMethodException(methodName);})).get();
    }
	
	private Map<String, CheckedSupplier<T>> mapObjectMethods(Object object){
		return Arrays.stream(object.getClass().getMethods())
				     .filter(method -> method.getParameterCount() == 0)
				     .collect(Collectors.toMap(Method::getName, method -> this.methodToSupplier(object, method)));
		
	}
	private CheckedSupplier<T> methodToSupplier(Object object, Method method){
		return () -> (T) method.invoke(object, null);
	}
	
}