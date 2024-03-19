package ru.eaglorn.aisnalogutility;

import java.util.ArrayList;
import java.util.List;

public class Data {
	public static ConfigAdmin CONFIG_ADMIN;
	public static ConfigApp CONFIG_APP = new ConfigApp();
	public static ConfigInstalled CONFIG_INSTALLED = new ConfigInstalled();
	public static List<Fix> PROM_FIXS = new ArrayList<>();
	public static List<Fix> OE_FIXS = new ArrayList<>();
}
