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
	private static final Logger logger = LoggerFactory.getLogger(ConfigAdmin.class);
	
	String login = "";
	String password = "";
	
	static void getConfig() {
		try {
			FileReader file = new FileReader(new File("c:\\AisNalogUtility\\config\\auth"));
			try (Scanner scan = new Scanner(file)) {
				String gson = "";
				while (scan.hasNextLine()) {
					gson += scan.nextLine();
				}
				gson = Crypt.decrypt(gson);
				AisNalogUtility.data.setConfigAdmin(new Gson().fromJson(gson, ConfigAdmin.class));
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public ConfigAdmin(String login, char[] password) {
		this.login = login;
		this.password = String.valueOf(password);
	}
}
