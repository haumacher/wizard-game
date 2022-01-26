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
	private interface V {
		// Pure base interface.
	}
	
	/**
	 * A message resource with a single parameter.
	 */
	public interface R1 extends V {
		/**
		 * Formats the message with the given arguments.
		 */
		String format(Object arg1);
	}
	
	/**
	 * A message resource with two parameters.
	 */
	public interface R2 extends V {
		/**
		 * Formats the message with the given arguments.
		 */
		String format(Object arg1, Object arg2);
	}
	
	/**
	 * A message resource with three parameters.
	 */
	public interface R3 extends V {
		/**
		 * Formats the message with the given arguments.
		 */
		String format(Object arg1, Object arg2, Object arg3);
	}
	
	/**
	 * A message resource with four parameters.
	 */
	public interface R4 extends V {
		/**
		 * Formats the message with the given arguments.
		 */
		String format(Object arg1, Object arg2, Object arg3, Object arg4);
	}
	
	/**
	 * A message resource with five parameters.
	 */
	public interface R5 extends V {
		/**
		 * Formats the message with the given arguments.
		 */
		String format(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5);
	}
	
	/**
	 * A message resource with more than five parameters.
	 */
	public interface RX extends V {
		/**
		 * Formats the message with the given arguments.
		 */
		String format(Object... args);
	}
	
	protected static void load(Class<? extends StaticResources> binding) {
		load(binding, bundle(binding));
	}
	
	protected static ResourceBundle bundle(Class<? extends StaticResources> binding) {
		return bundle(binding.getPackage().getName().replace('.', '/') + '/' + "Messages");
	}

	protected static void load(Class<? extends StaticResources> binding, String baseName) {
		load(binding, bundle(baseName));
	}

	protected static ResourceBundle bundle(String baseName) {
		return ResourceBundle.getBundle(baseName);
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
				
				InvocationHandler handler = type == RX.class ? new VArgsHandler(text) : new MessageHandler(text);
				
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

	private static final class MessageHandler implements InvocationHandler {
		private final String _format;

		/** 
		 * Creates a {@link MessageHandler}.
		 */
		private MessageHandler(String format) {
			_format = format;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return MessageFormat.format(_format, args);
		}
	}

	private static final class VArgsHandler implements InvocationHandler {
		private final String _format;
		
		/** 
		 * Creates a {@link MessageHandler}.
		 */
		private VArgsHandler(String format) {
			_format = format;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return MessageFormat.format(_format, (Object[])args[0]);
		}
	}
	
}
