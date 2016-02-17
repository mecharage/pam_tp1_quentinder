package com.example.hoangjim.quentinder;

public class CurrentState {
	private static class Holder {
		public static final CurrentState state = new CurrentState();
	}
	public static CurrentState getInstance() {
		return Holder.state;
	}

	private Person currentPerson;

	public enum Decision {
		NONE,
		LIKE,
		NOPE
	}

	private Decision decision = Decision.NONE;

	public Person getCurrentPerson() {
		return currentPerson;
	}

	public void setCurrentPerson(Person p) {
		currentPerson = p;
	}

	public void decideLike() {
		decision = Decision.LIKE;
	}

	public void decideNope() {
		decision = Decision.NOPE;
	}

	public Decision getDecision() {
		Decision d = decision;
		decision = Decision.NONE;
		return d;
	}
}
