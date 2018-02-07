package com.dottorsoft.SimpleBlockChain.core;

import java.util.Date;

import com.dottorsoft.SimpleBlockChain.utils.StringUtil;

public class Block {

	public String hash;
	public String previousHash;
	private String data; //a simple message.
	private long timeStamp;
	
	private int nonce;

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
	
	public void mineBlock(int difficulty) {
		String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0" 
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}