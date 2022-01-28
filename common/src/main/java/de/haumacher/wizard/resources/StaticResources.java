/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.resources;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Utility base class for loading a resource bundle and binding it to Java constants.
 */
public abstract class StaticResources {
	
	public interface Resource {
		String key();
		String format(Locale locale);
	}

	/**
	 * Marker interface for resource values.
	 */
	private static abstract class M {
		Function<Locale, ResourceBundle> _bundleLoader;
		private final String _key;
		private final String _pattern;
		
		/** 
		 * Creates a {@link StaticResources.M}.
		 */
		M(String key, String pattern) {
			_key = key;
			_pattern = pattern;
		}

		public String key() {
			return _key;
		}
		
		public String pattern() {
			return _pattern;
		}
		
		public Resource fill(Object...args) {
			return new Resource() {
				@Override
				public String key() {
					return _key;
				}
				
				@Override
				public String format(Locale locale) {
					String pattern = _bundleLoader.apply(locale).getString(_key);
					if (args.length == 0) {
						return pattern;
					} else {
						return MessageFormat.format(pattern, args);
					}
				}
			};
		}
		
		String fmt(Object... args) {
			return MessageFormat.format(_pattern, args);
		}
	}
	
	/**
	 * A message resource with a single parameter.
	 */
	public static class M0 extends M {
		/** Only for internal use, called by reflection. */
		public M0(String key, String pattern) {
			super(key, pattern);
		}

		/**
		 * Formats the message.
		 */
		public String format() {
			return pattern();
		}
	}
	
	/**
	 * A message resource with a single parameter.
	 */
	public static class M1 extends M {
		/** Only for internal use, called by reflection. */
		public M1(String key, String pattern) {
			super(key, pattern);
		}
		
		/**
		 * Formats the message with the given arguments.
		 */
		public String format(Object arg1) {
			return fmt(arg1);
		}
	}
	
	/**
	 * A message resource with two parameters.
	 */
	public static class M2 extends M {
		/** Only for internal use, called by reflection. */
		public M2(String key, String pattern) {
			super(key, pattern);
		}

		/**
		 * Formats the message with the given arguments.
		 */
		public String format(Object arg1, Object arg2) {
			return fmt(arg1, arg2);
		}
	}
	
	/**
	 * A message resource with three parameters.
	 */
	public static class M3 extends M {
		/** Only for internal use, called by reflection. */
		public M3(String key, String pattern) {
			super(key, pattern);
		}

		/**
		 * Formats the message with the given arguments.
		 */
		public String format(Object arg1, Object arg2, Object arg3) {
			return fmt(arg1, arg2, arg3);
		}
	}
	
	/**
	 * A message resource with four parameters.
	 */
	public static class M4 extends M {
		/** Only for internal use, called by reflection. */
		public M4(String key, String pattern) {
			super(key, pattern);
		}

		/**
		 * Formats the message with the given arguments.
		 */
		public String format(Object arg1, Object arg2, Object arg3, Object arg4) {
			return fmt(arg1, arg2, arg3, arg4);
		}
	}
	
	/**
	 * A message resource with five parameters.
	 */
	public static class M5 extends M {
		/** Only for internal use, called by reflection. */
		public M5(String key, String pattern) {
			super(key, pattern);
		}

		/**
		 * Formats the message with the given arguments.
		 */
		public String format(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
			return fmt(arg1, arg2, arg3, arg4, arg5);
		}
	}
	
	/**
	 * A message resource with more than five parameters.
	 */
	public static class MX extends M {
		/** Only for internal use, called by reflection. */
		public MX(String key, String pattern) {
			super(key, pattern);
		}

		/**
		 * Formats the message with the given arguments.
		 */
		public String format(Object... args) {
			return fmt(args);
		}
	}

	private static final Class<?>[] SIGNATURE = {String.class, String.class};
	
	protected static void load(Class<? extends StaticResources> binding) {
		load(binding, bundleName(binding));
	}
	
	private static String bundleName(Class<? extends StaticResources> binding) {
		return binding.getPackage().getName().replace('.', '/') + '/' + "Messages";
	}

	protected static void load(Class<? extends StaticResources> binding, String baseName) {
		ResourceBundle bundle = bundle(baseName);
		Function<Locale, ResourceBundle> loader = locale -> ResourceBundle.getBundle(baseName, locale);

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
			
			String key = f.getName();
			String pattern = bundle.getString(key);
			
			try {
				Class<?> type = f.getType();
				Object value;
				if (type == String.class) {
					value = pattern;
				} else {
					Constructor<?> constructor = type.getConstructor(SIGNATURE);
					M message = (M) constructor.newInstance(key, pattern);
					message._bundleLoader = loader;
					value = message;
				}

				f.set(null, value);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InstantiationException | InvocationTargetException ex) {
				System.err.println("Invalid resource field: " + f + ": " + ex.getMessage());
			}
		}
	}

	protected static ResourceBundle bundle(Class<? extends StaticResources> type) {
		return bundle(bundleName(type));
	}
	
	private static ResourceBundle bundle(String baseName) {
		return ResourceBundle.getBundle(baseName);
	}
	
}
