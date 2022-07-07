package com.bezkoder.spring.security.postgresql.util.propertyfile.context;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import com.bezkoder.spring.security.postgresql.util.context.handler.ContextRefreshedEventHandler;
import com.bezkoder.spring.security.postgresql.util.propertyfile.AppConfigConstants;
import com.bezkoder.spring.security.postgresql.util.propertyfile.ContextConstants;

public class AppContext extends ContextRefreshedEventHandler {
	/**
	 *
	 * @param event
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Environment env = event.getApplicationContext().getEnvironment();

		ContextConstants.contextRoot = event.getApplicationContext().getApplicationName();

		ContextConstants.fileSaveLocation = env.getRequiredProperty(AppConfigConstants.APP_FILE_SAVE_PATH);
		ContextConstants.smtpHostname = env.getRequiredProperty(AppConfigConstants.SMTP_HOSTNAME);
		ContextConstants.smtpPort = env.getRequiredProperty(AppConfigConstants.SMTP_PORT);
		ContextConstants.smtpContentType = env.getRequiredProperty(AppConfigConstants.SMTP_CONTENT_TYPE);
		ContextConstants.smtpFromAddress = env.getRequiredProperty(AppConfigConstants.SMTP_FROM_ADDRESS);
		ContextConstants.smtpPassword = env.getRequiredProperty(AppConfigConstants.SMTP_PASSWORD);
		//ContextConstants.appBaseUrl = env.getRequiredProperty(AppConfigConstants.APP_BASE_URL);

	}
}
