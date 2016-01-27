package com.example.hoangjim.quentinder;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Years;

public class Person {
	public final String name;
	public final int age;
	public final String picUrl;

	Person(String name, int age, String picUrl) {
		this.name = name;
		this.age = age;
		this.picUrl = picUrl;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(" ").append(age).append(" ans");
		return sb.toString();
	}
}
