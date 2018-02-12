package com.dottorsoft.SimpleBlockchain.main.util;

public enum Commands {

	GET_BLOCKCHAIN("getblockcain"),
	GET_LAST_BLOCK("getlastblock");

	private String command;

	private Commands(String commands){
		this.command = commands;
	}

	public String getCommand(){
		return this.command;
	}

}
