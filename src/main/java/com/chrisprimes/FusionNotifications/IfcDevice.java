package com.chrisprimes.FusionNotifications;

import java.io.Serializable;

@SuppressWarnings("serial")
public class IfcDevice implements Serializable {
	public String id;
	public String description;

	public IfcDevice(String id, String description) {
		this.id = id;
		this.description = description;
	}
}
