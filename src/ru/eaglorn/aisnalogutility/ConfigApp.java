package ru.eaglorn.aisnalogutility;

import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ConfigApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigApp.class);
	
	String NET_PATH = "";
	String PROM_VERSION = "";
	String OE_VERSION = "";
	
	static void getConfig() {
		try {
			JsonReader reader = new JsonReader(new FileReader("c:\\AisNalogUtility\\config\\config.json"));
			Data.CONFIG_APP = new Gson().fromJson(reader, ConfigApp.class);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}
}
