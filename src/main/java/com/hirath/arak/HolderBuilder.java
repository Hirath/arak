package com.hirath.arak;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hirath.arak.functional.CheckedSupplier;

public class HolderBuilder<T> {

	private List<ResponseBuilder<T,?>> responses = new ArrayList<>();
	private Map<String, CheckedSupplier<?>> suppliers = new HashMap<>();
	private DefaultsBuilder<T> defaultBuilder;
	private Class<T> clazz;
	
	
	public HolderBuilder(Class<T> clazz) {
		this.clazz = clazz;
		defaultBuilder = new DefaultsBuilder<>(this);
	}
	
	public static <T> HolderBuilder<T> as(Class<T> clazz){
		return new HolderBuilder<T>(clazz);
	}
	
	public <E> ResponseBuilder<T,E> call(CheckedSupplier<E> supplier){
		ResponseBuilder<T,E> response = new ResponseBuilder<>(this, clazz, supplier);
		responses.add(response);
		return response;
	}
	
	public <E> ResponseBuilder<T,E> reply(E value){
		return call(()->value);
	}
	
	public DefaultsBuilder<T> defautTo(Object object){
		return defaultBuilder.then(object);
	}
	
	public T instantiate(){
		responses.forEach(response -> response.getMethods().forEach(method -> suppliers.put(method, response.getSupplier())));
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[] { clazz },
                new ProxyHandler(suppliers, defaultBuilder.getDefaults()));
	}
	
}
