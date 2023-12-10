package com.tsoft.jamstrad;

import java.util.Properties;

public class AmstradMain {

	public static final String SETTING_OVERRIDE_PREFIX = "javacpc.";

	public static void main(String[] args) throws Exception {
		AmstradContext context = AmstradFactory.getInstance().getAmstradContext();

		context.initJavaConsole();

		System.out.println("Launching JAmstrad");

		overrideSettingsFromSystemProperties(context);

		context.getMode().launch(args);
	}

	private static void overrideSettingsFromSystemProperties(AmstradContext context) {
		Properties props = System.getProperties();
		for (String prop : props.stringPropertyNames()) {
			if (prop.startsWith(SETTING_OVERRIDE_PREFIX)) {
				String key = prop.substring(SETTING_OVERRIDE_PREFIX.length());
				String value = props.getProperty(prop);
				context.getUserSettings().set(key, value);
			}
		}
	}
}