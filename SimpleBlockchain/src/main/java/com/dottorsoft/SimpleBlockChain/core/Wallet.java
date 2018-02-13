package com.dottorsoft.SimpleBlockChain.core;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;

import com.dottorsoft.SimpleBlockChain.Main;
import com.dottorsoft.SimpleBlockChain.util.StringUtil;

public class Wallet {
	
	private BCECPrivateKey privateKey;
	private String publicKey;
	
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
	        setPrivateKey((BCECPrivateKey) keyPair.getPrivate());
	        setPublicKey(StringUtil.getStringFromKey(keyPair.getPublic()));
	        
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
	
	public Transaction sendFunds(String _recipient,float value ) throws NoSuchAlgorithmException, InvalidKeySpecException {
		if(getBalance() < value) {
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
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

	public BCECPrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(BCECPrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	
}

