package com.dottorsoft.SimpleBlockChain.core;

import java.util.Date;

import com.dottorsoft.SimpleBlockChain.utils.StringUtil;

public class Block {

	public String hash;
	public String previousHash;
	private String data; //a simple message.
	private long timeStamp;

	//Block Constructor.
	public Block(String data,String previousHash ) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}
	
	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256( 
				previousHash +
				Long.toString(timeStamp) +
				data 
				);
		return calculatedhash;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}