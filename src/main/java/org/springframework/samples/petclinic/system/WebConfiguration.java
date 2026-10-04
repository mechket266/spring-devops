package org.springframework.samples.petclinic.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Configures internationalization (i18n) support for the application.
 *
 * <p>
 * Handles loading language-specific messages, tracking the user's language, and allowing
 * language changes via the URL parameter (e.g., <code>?lang=de</code>).
 * </p>
 *
 * @author Anuj Ashok Potdar
 */
@Configuration
@SuppressWarnings("unused")
public class WebConfiguration implements WebMvcConfigurer {

	/**
	 * Uses session storage to remember the user’s language setting across requests.
	 * Defaults to English if nothing is specified.
	 * @return session-based {@link LocaleResolver}
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(Locale.ENGLISH);
		return resolver;
	}

	/**
	 * Allows the app to switch languages using a URL parameter like
	 * <code>?lang=es</code>.
	 * @return a {@link LocaleChangeInterceptor} that handles the change
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");
		return interceptor;
	}

	/**
	 * Registers the locale change interceptor so it can run on each request.
	 * @param registry where interceptors are added
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	/**
	 * Security headers filter to address OWASP ZAP warnings
	 * @return security headers filter
	 */
	@Bean
	public Filter securityHeadersFilter() {
		return new Filter() {
			@Override
			public void init(FilterConfig filterConfig) throws ServletException {
			}

			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				// Anti-clickjacking
				httpResponse.setHeader("X-Frame-Options", "DENY");
				// X-Content-Type-Options
				httpResponse.setHeader("X-Content-Type-Options", "nosniff");
				// Content Security Policy
				httpResponse.setHeader("Content-Security-Policy",
						"default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:;");
				// Permissions Policy
				httpResponse.setHeader("Permissions-Policy", "geolocation=(), microphone=(), camera=()");
				// Cross-Origin-Embedder-Policy
				httpResponse.setHeader("Cross-Origin-Embedder-Policy", "require-corp");
				// Cache control for non-storable content
				httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				httpResponse.setHeader("Pragma", "no-cache");
				httpResponse.setHeader("Expires", "0");
				chain.doFilter(request, response);
			}

			@Override
			public void destroy() {
			}
		};
	}

}
