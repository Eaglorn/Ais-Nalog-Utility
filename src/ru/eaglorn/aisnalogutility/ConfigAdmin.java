package ru.eaglorn.aisnalogutility;

import java.io.FileReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.io.File;
import java.io.FileNotFoundException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ConfigAdmin {
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
				AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}

	public ConfigAdmin(String login, char[] password) {
		this.LOGIN = login;
		this.PASSWORD = String.valueOf(password);
	}
}
