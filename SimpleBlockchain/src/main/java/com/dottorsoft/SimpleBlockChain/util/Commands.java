package com.dottorsoft.SimpleBlockChain.util;

public enum Commands {
	
	GET_BLOCKCHAIN("getBlockChain"),
	POST_LAST_MINED_BLOCK("postLastMinedBlock"),
	GET_BLOCK_CHAIN_SIZE("getBlockChainSize"),
	UNKNOWN_COMMAND("command unknown");
	
	private String command;
	
	private Commands(String commands){
		this.command = commands;
	}
	
	public String getCommand(){
		return this.command;
	}

}
