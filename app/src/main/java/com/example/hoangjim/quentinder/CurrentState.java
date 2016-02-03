package com.example.hoangjim.quentinder;

import java.util.ArrayList;
import java.util.List;

public class CurrentState {
	private static class Holder {

		public static final CurrentState state = new CurrentState();
	}
	public static CurrentState getInstance() {
		return Holder.state;
	}

	private final List<Person> _persons = new ArrayList<>();

	private Person currentPerson;
	public void addPerson(Person person) {
		_persons.add(person);
		if(currentPerson == null)
			currentPerson = _persons.get(0);
	}

	public Person getCurrentPerson() {
		return currentPerson;
	}

	public Person popNextPerson() {
		_persons.remove(0);
		currentPerson = _persons.get(0);
		return currentPerson;
	}
}
