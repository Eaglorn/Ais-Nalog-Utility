package ru.eaglorn.aisnalogutility;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigApp {
	private @Getter String netPath = "";
	private @Getter String oeVersion = "";
	private @Getter String promVersion = "";

	public static void getConfig() {
		try {
			JsonReader reader = new JsonReader(new FileReader("c:\\AisNalogUtility\\config\\config.json"));
			AisNalogUtility.getData().setConfigApp(new Gson().fromJson(reader, ConfigApp.class));
		} catch (IOException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
		}
	}
}
