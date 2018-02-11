package com.dottorsoft.SimpleBlockChain.core;

public class TransactionInput {
	
	public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
	public TransactionOutput UTXO; //Contains the Unspent transaction output
	
	public TransactionInput(){}
	
	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}

}
