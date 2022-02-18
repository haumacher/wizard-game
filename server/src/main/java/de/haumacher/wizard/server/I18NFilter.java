/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet filter that dispatches to localized resources and sets the default character encoding <code>utf-8</code>.
 */
@WebFilter(urlPatterns = "/*")
public class I18NFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		String servletPath = req.getServletPath();
		if (servletPath.endsWith(".html")) {
			response.setCharacterEncoding("UTF-8");
			
			Locale locale = request.getLocale();
			String language = locale.getLanguage();
			if (!language.equals("en")) {
				System.out.println("Localizing (" + language + "): " + servletPath);
				
				int nameSep = servletPath.lastIndexOf('/');
				if (nameSep == servletPath.length() - 1) {
					// Ends with '/'. Use index file to start localization.
					servletPath = servletPath + "index.html";
				}
				int localizationSep = servletPath.indexOf('_', nameSep + 1);
				if (localizationSep < 0) {
					// Not yet localized.
					int extIndex = servletPath.lastIndexOf('.');
					if (extIndex >= 0) {
						String localizedPath = servletPath.substring(0, extIndex) + '_' + language + servletPath.substring(extIndex);
						if (request.getServletContext().getResource(localizedPath) != null) {
							req.getServletContext().getRequestDispatcher(localizedPath).forward(request, response);
							return;
						}
					}
				}
			}
		}

		chain.doFilter(request, response);
	}

}
