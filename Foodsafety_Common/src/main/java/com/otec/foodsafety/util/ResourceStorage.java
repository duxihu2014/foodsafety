package com.otec.foodsafety.util;

public enum ResourceStorage {
	LOCAL("1"), 
	DB("2"), 
	REMOTE("3"), 
	OTHER("4");
	
	private String storage;
	
	ResourceStorage(String storage){
		this.storage = storage;
	}
	
	@Override
	public String toString() {
		return storage;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}
	
	/*public static void main(String[]  args) {
		System.out.println(ResourceStorage.LOCAL);
		System.out.println(ResourceStorage.LOCAL.toString().equals("1"));
	}*/
	
}
