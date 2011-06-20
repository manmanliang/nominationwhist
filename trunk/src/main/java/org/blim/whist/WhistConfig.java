package org.blim.whist;

import java.util.HashMap;
import java.util.Map;

public class WhistConfig {
	
	private Map<String, String> computerPlayerTypes = new HashMap<String, String>();

	public Map<String, String> getComputerPlayerTypes() {
		return computerPlayerTypes;
	}

	public void setComputerPlayerTypes(Map<String, String> computerPlayerTypes) {
		this.computerPlayerTypes = computerPlayerTypes;
	}

}
