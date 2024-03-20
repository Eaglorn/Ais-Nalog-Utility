package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ConfigInstalled {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigInstalled.class);
	
	String PROM_VERSION = "";
	List<String> PROM_INSTALLED = new ArrayList<String>();
	
	String OE_VERSION = "";
	List<String> OE_INSTALLED = new ArrayList<String>();
	
	static void getConfig() {
		if (Files.exists(Paths.get("c:\\AisNalogUtility\\config\\installed.json"))) {
			try {
				JsonReader reader = new JsonReader(new FileReader("c:\\AisNalogUtility\\config\\installed.json"));
				Data.CONFIG_INSTALLED = new Gson().fromJson(reader, ConfigInstalled.class);
				if(!Data.CONFIG_APP.PROM_VERSION.equals(Data.CONFIG_INSTALLED.PROM_VERSION)) {
					Data.CONFIG_INSTALLED.PROM_INSTALLED.clear();
					Data.CONFIG_INSTALLED.PROM_VERSION = Data.CONFIG_APP.PROM_VERSION;
				}
				
				if (!Files.exists(Paths.get(AisNalogUtility.APP_PROM_PATH + "Client\\CSC.ClientPackage.fix"))) {
					Data.CONFIG_INSTALLED.PROM_INSTALLED.clear();
					Data.CONFIG_INSTALLED.PROM_VERSION = Data.CONFIG_APP.PROM_VERSION;
				}
				
				save();
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		} else {
			Data.CONFIG_INSTALLED.PROM_VERSION = Data.CONFIG_APP.PROM_VERSION;
			
			File file = new File("c:\\AisNalogUtility\\config\\installed.json");
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
			
			save();
		}
	}
	
	public static void save() {
		String str = new Gson().toJson(Data.CONFIG_INSTALLED, ConfigInstalled.class);
		try (FileWriter file = new FileWriter("c:\\AisNalogUtility\\config\\installed.json")) {
			file.write(str);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}


}
