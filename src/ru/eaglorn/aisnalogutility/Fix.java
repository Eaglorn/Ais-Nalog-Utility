package ru.eaglorn.aisnalogutility;

import lombok.Getter;
import lombok.Setter;

public class Fix {
	private @Getter @Setter int index = 0;
	private @Getter @Setter String name = "";
	private @Getter @Setter boolean checked = false;

	public Fix(int index, String name) {
		this.index = index;
		this.name = name;
	}
}
