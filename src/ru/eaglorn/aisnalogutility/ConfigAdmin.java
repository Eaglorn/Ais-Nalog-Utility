package ru.eaglorn.aisnalogutility;

public class ConfigAdmin {
	String LOGIN = "";
	String PASSWORD = "";

	public ConfigAdmin(String login, char[] password) {
		this.LOGIN = login;
		this.PASSWORD = String.valueOf(password);
	}
}
