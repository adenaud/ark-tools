package com.rcon4games.arktools;


import com.rcon4games.arktools.service.ModService;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


/**
 * Created by Anthony Denaud on 12/12/16.
 * Copyright Personalized-Software Ltd
 */
@PropertySources({@PropertySource("classpath:config.properties")})
@SpringBootApplication
public class Main {

	private static final String OPTION_CHECK_MOD_UPDATE = "mod-updates";

	public static void main(String[] args){
		OptionParser parser = new OptionParser();
		parser.accepts(OPTION_CHECK_MOD_UPDATE);
		OptionSet options = parser.parse(args);

		if(options.has(OPTION_CHECK_MOD_UPDATE)){
			ApplicationContext context = SpringApplication.run(Main.class, args);
			ModService modService = context.getBean(ModService.class);
			modService.checkAll();

		}else{
			System.out.println("No option given.");
		}
	}
}
