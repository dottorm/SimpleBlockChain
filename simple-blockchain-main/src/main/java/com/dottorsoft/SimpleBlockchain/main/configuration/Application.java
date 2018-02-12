package com.dottorsoft.SimpleBlockchain.main.configuration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = {"com.dottorsoft.SimpleBlockChain"})
public class Application extends SpringBootServletInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	private static final String SPRING_CONFIG_LOCATION_PROPERTY = "spring.config.location";

	@Inject
	private Environment environment;

	static {

		LOGGER.info("starting main module");

		String configLocation = System.getProperty(SPRING_CONFIG_LOCATION_PROPERTY);
		if (configLocation != null) {
			LOGGER.info("Using external spring configuration: {} ", configLocation);
		} else {
			LOGGER.info("Using enclosed spring configuration");
			System.setProperty("spring.config.name", "simple-blockchain-application");
		}
	}

	//Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile.

	@PostConstruct
	void initApplication() {
		if (this.environment.getActiveProfiles().length == 0) {
			LOGGER.warn("No Spring profile configured, running with default configuration");
		} else {
			LOGGER.info("Running with Spring profile(s) : {}", Arrays.toString(this.environment.getActiveProfiles()));
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

}
