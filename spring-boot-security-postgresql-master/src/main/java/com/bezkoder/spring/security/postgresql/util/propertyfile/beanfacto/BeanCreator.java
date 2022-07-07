package com.bezkoder.spring.security.postgresql.util.propertyfile.beanfacto;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import com.bezkoder.spring.security.postgresql.util.context.handler.ContextRefreshedEventHandler;

public class BeanCreator extends ContextRefreshedEventHandler {

	private static ApplicationContext context;

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T getBeanInstance(Class<T> clazz) {
		return context.getBean(clazz);
	}

	public void onApplicationEvent(ContextRefreshedEvent event) {
		context = event.getApplicationContext();
	}
}
