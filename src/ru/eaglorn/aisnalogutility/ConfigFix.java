package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigFix {
	private @Getter @Setter String promVersion = "";
	private @Getter @Setter String oeVersion = "";
	private @Getter @Setter List<String> promFixs = new ArrayList<>();
	private @Getter @Setter List<String> oeFixs = new ArrayList<>();
	private static String pathSave = "c:\\AisNalogUtility\\config\\save.json";
	
	public static void getConfig() {
		Data data = AisNalogUtility.getData();
		ConfigFix configFix = data.getConfigFix();
		ConfigApp configApp = data.getConfigApp();
		if (Files.exists(Paths.get(pathSave))) {
			try {
				JsonReader reader = new JsonReader(new FileReader("c:\\AisNalogUtility\\config\\save.json"));
				data.setConfigFix(new Gson().fromJson(reader, ConfigFix.class));
				if (!configApp.getPromVersion().equals(configFix.getPromVersion())
						|| !Files.exists(Paths.get(AisNalogUtility.getApp().getPromPath() + "Client\\CSC.ClientPackage.fix"))) {
					configFix.getPromFixs().clear();
					configFix.setPromVersion(configApp.getPromVersion());
				}
				save();
			} catch (IOException e) {
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				log.error(stack.toString());
			}
		} else {
			configFix.setPromVersion(configApp.getPromVersion());
			File file = new File(pathSave);
			try {
				if (file.createNewFile()) {
					log.error("Error create save file.");
				}
			} catch (IOException e) {
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				log.error(stack.toString());
			}
			save();
		}
	}
	
	public static void save() {
		try (FileWriter file = new FileWriter(pathSave)) {
			file.write(new Gson().toJson(AisNalogUtility.getData().getConfigFix(), ConfigFix.class));
			file.flush();
		} catch (IOException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
		}
	}
}
