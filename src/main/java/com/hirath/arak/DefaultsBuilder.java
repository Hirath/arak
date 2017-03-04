package com.hirath.arak;

import java.util.ArrayList;
import java.util.List;

public class DefaultsBuilder<T> {
	
	private HolderBuilder<T> parent;
	private List<Object> defaults;

	public DefaultsBuilder(HolderBuilder<T> parent) {
		this.parent = parent;
		this.defaults = new ArrayList<>();
	}

	public DefaultsBuilder<T> then(Object object){
		defaults.add(object);
		return this;
	}
	
	public List<Object> getDefaults() {
		return defaults;
	}

	public T instantiate(){
		return parent.instantiate();
	}
	
}
