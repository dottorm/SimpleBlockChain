package com.dottorsoft.SimpleBlockchain.main.core;

import com.dottorsoft.SimpleBlockchain.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {

	private static final Logger LOGGER = LoggerFactory.getLogger(Wallet.class);

	public PrivateKey privateKey;
	public PublicKey publicKey;

	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

	public Wallet() {
		generateKeyPair();
	}

	public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random); //256
			KeyPair keyPair = keyGen.generateKeyPair();
			// Set the public and private keys from the keyPair
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();

		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public float getBalance() {
		float total = 0;
		TransactionOutput UTXO;
		for (Map.Entry<String, TransactionOutput> item: Main.UTXOs.entrySet()){
			UTXO = item.getValue();
			if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
				UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
				total += UTXO.value ;
			}
		}
		return total;
	}

	public Transaction sendFunds(PublicKey _recipient,float value ) {
		if(getBalance() < value) {
			LOGGER.debug("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

		float total = 0;
		TransactionOutput UTXO;
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
			UTXO= item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			if(total > value) break;
		}

		Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
		newTransaction.generateSignature(privateKey);

		for(TransactionInput input: inputs){
			UTXOs.remove(input.transactionOutputId);
		}

		return newTransaction;
	}

}
