package com.dottorsoft.SimpleBlockChain.core;

import com.dottorsoft.SimpleBlockChain.util.StringUtil;

public class TransactionOutput {
	
	public String id;
	public String reciepient; //also known as the new owner of these coins.
	public float value; //the amount of coins they own
	public String parentTransactionId; //the id of the transaction this output was created in
	
	public TransactionOutput(){}
	
	//Constructor
	public TransactionOutput(String reciepient, float value, String parentTransactionId) {
		this.reciepient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = StringUtil.applySha256(reciepient+Float.toString(value)+parentTransactionId);
	}
	
	//Check if coin belongs to you
	public boolean isMine(String publicKey) {
		return (publicKey.equals(reciepient));
}

}
