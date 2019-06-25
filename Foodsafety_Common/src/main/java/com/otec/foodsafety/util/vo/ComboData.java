package com.otec.foodsafety.util.vo;

import java.io.Serializable;

public class ComboData implements Serializable {

	private String text;
	private String value;

	public ComboData(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
