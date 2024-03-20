package ru.eaglorn.aisnalogutility;

import lombok.Getter;
import lombok.Setter;

public class Fix {
	private @Getter @Setter boolean checked = false;
	private @Getter @Setter int index = 0;
	private @Getter @Setter String name = "";

	public Fix(int index, String name) {
		this.index = index;
		this.name = name;
	}
}
