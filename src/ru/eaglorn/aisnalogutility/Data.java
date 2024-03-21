package ru.eaglorn.aisnalogutility;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Data {
	private @Getter @Setter ConfigAdmin configAdmin;
	private @Getter @Setter ConfigApp configApp;
	private @Getter @Setter ConfigFix configFix = new ConfigFix();
	private @Getter List<Fix> oeFixs = new ArrayList<>();
	private @Getter List<Fix> promFixs = new ArrayList<>();
}
