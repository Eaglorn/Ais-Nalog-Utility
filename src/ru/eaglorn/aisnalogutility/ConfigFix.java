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

import lombok.Getter;
import lombok.Setter;

public class ConfigFix {
	private static final Logger logger = LoggerFactory.getLogger(ConfigFix.class);
	
	public static void getConfig() {
		Data data = AisNalogUtility.getData();
		App app = AisNalogUtility.getApp();
		
		if (Files.exists(Paths.get("c:\\AisNalogUtility\\config\\installed.json"))) {
			try {
				JsonReader reader = new JsonReader(new FileReader("c:\\AisNalogUtility\\config\\installed.json"));
				data.setConfigFix(new Gson().fromJson(reader, ConfigFix.class));
				if(!data.getConfigApp().getPromVersion().equals(data.getConfigFix().getPromVersion()) || !Files.exists(Paths.get(app.getPromPath() + "Client\\CSC.ClientPackage.fix"))) {
					data.getConfigFix().getPromFixs().clear();
					data.getConfigFix().setPromVersion(data.getConfigApp().getPromVersion());
				}
				
				save();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		} else {
			data.getConfigFix().setPromVersion(data.getConfigApp().getPromVersion());
			File file = new File("c:\\AisNalogUtility\\config\\installed.json");
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
			
			save();
		}
	}
	public static void save() {
		Data data = AisNalogUtility.getData();
		String str = new Gson().toJson(data.getConfigFix(), ConfigFix.class);
		try (FileWriter file = new FileWriter("c:\\AisNalogUtility\\config\\installed.json")) {
			file.write(str);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	private @Getter @Setter String promVersion = "";
	private @Getter @Setter String oeVersion = "";
	
	private @Getter @Setter List<String> promFixs = new ArrayList<>();
	private @Getter @Setter List<String> oeFixs = new ArrayList<>();
}
