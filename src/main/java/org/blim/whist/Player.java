package org.blim.whist;

import javax.persistence.Entity;

@Entity
public class Player extends User {
	private String prettyName;
	private String shortName;

	public void setPrettyName(String prettyName) {
		this.prettyName = prettyName;
	}

	public String getPrettyName() {
		return prettyName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortName() {
		return shortName;
	}
}
