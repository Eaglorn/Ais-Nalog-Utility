package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ConfigAdmin {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigAdmin.class);
	
	String LOGIN = "";
	String PASSWORD = "";
	
	static void getConfig() {
		try {
			FileReader file = new FileReader(new File("c:\\AisNalogUtility\\config\\auth"));
			try (Scanner scan = new Scanner(file)) {
				String gson = "";
				while (scan.hasNextLine()) {
					gson += scan.nextLine();
				}
				gson = Crypt.decrypt(gson);
				Data.CONFIG_ADMIN = new Gson().fromJson(gson, ConfigAdmin.class);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}

	public ConfigAdmin(String login, char[] password) {
		this.LOGIN = login;
		this.PASSWORD = String.valueOf(password);
	}
}
