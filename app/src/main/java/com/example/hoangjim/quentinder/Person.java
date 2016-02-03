package com.example.hoangjim.quentinder;

public class Person {
	public final String firstName;
	public final String lastName;
	public final int age;
	public final String picUrl;
	public final String email;
	public final String location;

	Person(String firstName, String lastName, String picUrl, int age, String email, String location) {
		this.firstName = firstName;
		this.age = age;
		this.picUrl = picUrl;
		this.lastName = lastName;
		this.email = email;
		this.location = location;

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(firstName).append(" ").append(age).append(" ans");
		return sb.toString();
	}
}
