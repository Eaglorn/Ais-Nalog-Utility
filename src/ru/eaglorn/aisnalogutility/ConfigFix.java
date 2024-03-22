package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigFix {
	private @Getter @Setter String promVersion = "";
	private @Getter @Setter String oeVersion = "";
	private @Getter @Setter List<String> promFixs = new ArrayList<>();
	private @Getter @Setter List<String> oeFixs = new ArrayList<>();
	private static @val String pathSave = "c:\\AisNalogUtility\\config\\save.json";

	public static void getConfig() {
		Data data = AisNalogUtility.getData();
		App app = AisNalogUtility.getApp();
		if (Files.exists(Paths.get(pathSave))) {
			try {
				JsonReader reader = new JsonReader(new FileReader("c:\\AisNalogUtility\\config\\save.json"));
				data.setConfigFix(new Gson().fromJson(reader, ConfigFix.class));
				if (!data.getConfigApp().getPromVersion().equals(data.getConfigFix().getPromVersion())
						|| !Files.exists(Paths.get(app.getPromPath() + "Client\\CSC.ClientPackage.fix"))) {
					data.getConfigFix().getPromFixs().clear();
					data.getConfigFix().setPromVersion(data.getConfigApp().getPromVersion());
				}
				save();
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		} else {
			data.getConfigFix().setPromVersion(data.getConfigApp().getPromVersion());
			File file = new File(pathSave);
			try {
				if (file.createNewFile()) {
					log.error("Error create save file.");
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
			save();
		}
	}

	public static void save() {
		Data data = AisNalogUtility.getData();
		String str = new Gson().toJson(data.getConfigFix(), ConfigFix.class);
		try (FileWriter file = new FileWriter(pathSave)) {
			file.write(str);
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
}
