package com.android.tdsoft.trackingapp.places;

import java.util.List;

import com.google.api.client.util.Key;

public class Place {

	@Key
	public String id;
	
	@Key
	public String name;
	
	@Key
	public List<String> types;
	
	@Key
	public String formatted_address;
	
	@Key
	public String reference;

	@Override
	public String toString() {
		return name + " - " + id + " - " + reference;
	}
	
}
