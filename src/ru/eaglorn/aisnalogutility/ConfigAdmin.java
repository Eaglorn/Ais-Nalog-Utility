package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

public class ConfigAdmin {
	private static @val Logger logger = LoggerFactory.getLogger(ConfigAdmin.class);
	
	private @Getter @Setter String login = "";
	
	private @Getter @Setter String password = "";
	
	static void getConfig() {
		Data data = AisNalogUtility.getData();
		try {
			FileReader file = new FileReader(new File("c:\\AisNalogUtility\\config\\auth"));
			try (Scanner scan = new Scanner(file)) {
				StringBuilder gson = new StringBuilder();
				while (scan.hasNextLine()) {
					gson.append(scan.nextLine());
				}
				data.setConfigAdmin(new Gson().fromJson(Crypt.decrypt(gson.toString()), ConfigAdmin.class));
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
