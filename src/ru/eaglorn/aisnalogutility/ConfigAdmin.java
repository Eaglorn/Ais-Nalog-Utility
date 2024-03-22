package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigAdmin {
	private @Getter @Setter String login = "";
	private @Getter @Setter String password = "";

	static void getConfig() {
		Data data = AisNalogUtility.getData();
		FileReader file = null;
		try {
			file = new FileReader(new File("c:\\AisNalogUtility\\config\\auth"));
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
		try (Scanner scan = new Scanner(file)) {
			StringBuilder gson = new StringBuilder();
			while (scan.hasNextLine()) {
				gson.append(scan.nextLine());
			}
			data.setConfigAdmin(new Gson().fromJson(Crypt.decrypt(gson.toString()), ConfigAdmin.class));
		} catch (JsonSyntaxException e) {
			log.error(e.getMessage());
		}
	}

	public ConfigAdmin(String login, char[] password) {
		this.login = login;
		this.password = String.valueOf(password);
	}
}
