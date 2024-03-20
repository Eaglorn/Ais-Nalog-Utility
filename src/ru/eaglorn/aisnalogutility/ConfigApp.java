package ru.eaglorn.aisnalogutility;

import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import lombok.Getter;

public class ConfigApp {
	private static final Logger logger = LoggerFactory.getLogger(ConfigApp.class);
	
	public static void getConfig() {
		try {
			JsonReader reader = new JsonReader(new FileReader("c:\\AisNalogUtility\\config\\config.json"));
			AisNalogUtility.getData().setConfigApp(new Gson().fromJson(reader, ConfigApp.class));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	private @Getter String netPath = "";
	private @Getter String promVersion = "";
	
	private @Getter String oeVersion = "";
}
