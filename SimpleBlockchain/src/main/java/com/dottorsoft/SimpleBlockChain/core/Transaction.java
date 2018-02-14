package com.dottorsoft.SimpleBlockChain.core;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;

import com.dottorsoft.SimpleBlockChain.Main;
import com.dottorsoft.SimpleBlockChain.util.StringUtil;

public class Transaction {
	
	private String transactionId; //Contains a hash of transaction*
	private String sender; //Senders address/public key.
	private String reciepient; //Recipients address/public key.
	private float value; //Contains the amount we wish to send to the recipient.
	private byte[] signature; //This is to prevent anybody else from spending funds in our wallet.
	
	private ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	private ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	
	private static int sequence = 0; //A rough count of how many transactions have been generated 
	
	public Transaction(){}
	
	// Constructor: 
	public Transaction(String from, String to, float value,  ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}
	
	public boolean processTransaction() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
		
		if(verifySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}
				
		//Gathers transaction inputs (Making sure they are unspent):
		for(TransactionInput i : inputs) {
			i.UTXO = Main.UTXOs.get(i.transactionOutputId);
		}

		//Checks if transaction is valid:
		if(getInputsValue() < Main.minimumTransaction) {
			System.out.println("Transaction Inputs to small: " + getInputsValue());
			return false;
		}
		
		//Generate transaction outputs:
		float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
		transactionId = calulateHash();
		outputs.add(new TransactionOutput( this.reciepient, value,transactionId)); //send value to recipient
		outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender		
				
		//Add outputs to Unspent list
		for(TransactionOutput o : outputs) {
			Main.UTXOs.put(o.id , o);
		}
		
		//Remove transaction inputs from UTXO lists as spent:
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
			Main.UTXOs.remove(i.UTXO.id);
		}
		
		return true;
	}
	
	public float getInputsValue() {
		float total = 0;
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it, This behavior may not be optimal.
			total += i.UTXO.value;
		}
		return total;
	}
	
	public void generateSignature(BCECPrivateKey privateKey) {
		String data = sender + reciepient + Float.toString(value);
		signature = StringUtil.applyECDSASig(privateKey,data);		
	}
	
	public boolean verifySignature() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
		String data = sender + reciepient + Float.toString(value);
		return StringUtil.verifyECDSASig(StringUtil.getPublicKeyfromString(sender), data, signature);
	}
	
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutput o : outputs) {
			total += o.value;
		}
		return total;
	}
	
	private String calulateHash() {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return StringUtil.applySha256(
				sender +
				reciepient +
				Float.toString(value) + sequence
				);
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReciepient() {
		return reciepient;
	}

	public void setReciepient(String reciepient) {
		this.reciepient = reciepient;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public ArrayList<TransactionInput> getInputs() {
		return inputs;
	}

	public void setInputs(ArrayList<TransactionInput> inputs) {
		this.inputs = inputs;
	}

	public ArrayList<TransactionOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(ArrayList<TransactionOutput> outputs) {
		this.outputs = outputs;
	}

	public static int getSequence() {
		return sequence;
	}

	public static void setSequence(int sequence) {
		Transaction.sequence = sequence;
	}
}