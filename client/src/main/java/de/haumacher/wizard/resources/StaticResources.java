/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.resources;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Utility base class for loading a resource bundle and binding it to Java constants.
 */
public abstract class StaticResources {

	/**
	 * Marker interface for resource values.
	 */
	private interface V {}
	
	public interface R1 extends V {
		String format(Object arg1);
	}
	
	public interface R2 extends V {
		String format(Object arg1, Object arg2);
	}
	
	public interface R3 extends V {
		String format(Object arg1, Object arg2, Object arg3);
	}
	
	public interface R4 extends V {
		String format(Object arg1, Object arg2, Object arg3, Object arg4);
	}
	
	public interface R5 extends V {
		String format(Object arg1, Object arg2, Object arg3, Object arg5);
	}
	
	protected static void load(Class<? extends StaticResources> binding, String baseName) {
		ResourceBundle bundle = ResourceBundle.getBundle(baseName);
		load(binding, bundle);
	}
	
	protected static void load(Class<? extends StaticResources> binding, ResourceBundle bundle) {
		for (Field f : binding.getFields()) {
			if (f.isSynthetic()) {
				continue;
			}
			if ((f.getModifiers() & Modifier.STATIC) == 0) {
				continue;
			}
			if ((f.getModifiers() & Modifier.FINAL) != 0) {
				continue;
			}
			
			String name = f.getName();
			String text = bundle.getString(name);

			Class<?> type = f.getType();
			Object value;
			if (type == String.class) {
				value = text;
			} else {
				assert V.class.isAssignableFrom(type) : "Resource field must be either string or one of the R types.";
				
				InvocationHandler handler = new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						return MessageFormat.format(text, args);
					}
				};
				
				Class<?>[] interfaces = {type};
				value = Proxy.newProxyInstance(binding.getClassLoader(), interfaces, handler);
			}
			
			try {
				f.set(null, value);
			} catch (IllegalArgumentException | IllegalAccessException ex) {
				System.err.println("Invalid resource field: " + f + ": " + ex.getMessage());
			}
		}
	}

}
