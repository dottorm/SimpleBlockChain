package com.dottorsoft.SimpleBlockchain.main.core;

import com.dottorsoft.SimpleBlockchain.main.Main;
import com.dottorsoft.SimpleBlockchain.main.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {

	private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

	private String transactionId; //Contains a hash of transaction*
	private PublicKey sender; //Senders address/public key.
	private PublicKey reciepient; //Recipients address/public key.
	private float value; //Contains the amount we wish to send to the recipient.
	private byte[] signature; //This is to prevent anybody else from spending funds in our wallet.

	private ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	private ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

	private static int sequence = 0; //A rough count of how many transactions have been generated

	// Constructor:
	public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}

	public boolean processTransaction() {

		if(!verifiySignature()) {
			LOGGER.debug("#Transaction Signature failed to verify");
			return false;
		}

		//Gathers transaction inputs (Making sure they are unspent):
		for(TransactionInput i : inputs) {
			i.UTXO = Main.UTXOs.get(i.transactionOutputId);
		}

		//Checks if transaction is valid:
		if(getInputsValue() < Main.minimumTransaction) {
			LOGGER.debug("Transaction Inputs to small: {}", getInputsValue());
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

	public void generateSignature(PrivateKey privateKey) {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
		signature = StringUtil.applyECDSASig(privateKey,data);
	}

	public boolean verifiySignature() {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
		return StringUtil.verifyECDSASig(sender, data, signature);
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
				StringUtil.getStringFromKey(sender) +
						StringUtil.getStringFromKey(reciepient) +
						Float.toString(value) + sequence
		);
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public PublicKey getSender() {
		return sender;
	}

	public void setSender(PublicKey sender) {
		this.sender = sender;
	}

	public PublicKey getReciepient() {
		return reciepient;
	}

	public void setReciepient(PublicKey reciepient) {
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