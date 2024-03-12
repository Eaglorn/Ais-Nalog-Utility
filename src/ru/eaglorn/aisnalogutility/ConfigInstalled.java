package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ConfigInstalled {
	String VERSION = "";
	List<String> INSTALLED = new ArrayList<String>();
	
	static void getConfig() {
		if (Files.exists(Paths.get("c:\\AisNalogUtility\\config\\installed.json"))) {
			try {
				JsonReader reader = new JsonReader(new FileReader("c:\\AisNalogUtility\\config\\installed.json"));
				Data.CONFIG_INSTALLED = new Gson().fromJson(reader, ConfigInstalled.class);
				if(!Data.CONFIG_APP.VERSION.equals(Data.CONFIG_INSTALLED.VERSION)) {
					Data.CONFIG_INSTALLED.INSTALLED.clear();
					Data.CONFIG_INSTALLED.VERSION = Data.CONFIG_APP.VERSION;
				}
				
				if (!Files.exists(Paths.get(AisNalogUtility.AIS_PATH + "Client\\CSC.ClientPackage.fix"))) {
					Data.CONFIG_INSTALLED.INSTALLED.clear();
					Data.CONFIG_INSTALLED.VERSION = Data.CONFIG_APP.VERSION;
				}
				
				save();
			} catch (IOException e) {
				e.printStackTrace();
				AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
			}
		} else {
			Data.CONFIG_INSTALLED.VERSION = Data.CONFIG_APP.VERSION;
			
			File file = new File("c:\\AisNalogUtility\\config\\installed.json");
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
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
			AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}


}
