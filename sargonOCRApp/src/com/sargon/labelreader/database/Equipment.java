package com.sargon.labelreader.database;

public class Equipment {
	public int id;
	public String SERIAL_NUMBER;
	public String PART_NUMBER;
	public String ASSET_NUMBER;
	public String ENGINE_MODEL_TYPE;
	
	public Equipment()
	{
	}
	
	public Equipment(String SERIAL_NUMBER, String PART_NUMBER, String ASSET_NUMBER, String ENGINE_MODEL_TYPE)
	{
		super();
		this.SERIAL_NUMBER = SERIAL_NUMBER;
		this.PART_NUMBER = PART_NUMBER;
		this.ASSET_NUMBER = ASSET_NUMBER;
		this.ENGINE_MODEL_TYPE = ENGINE_MODEL_TYPE;
	}
	public String toString() 
	{
		return "Book [id=" + id + ", SERIAL NUMBER=" + SERIAL_NUMBER + 
			", PART NUMBER=" + PART_NUMBER+
			", ASSET NUMBER=" + ASSET_NUMBER+
			", ENGINE MODEL TYPE="+ENGINE_MODEL_TYPE
			+ "]";
	}
	
}
