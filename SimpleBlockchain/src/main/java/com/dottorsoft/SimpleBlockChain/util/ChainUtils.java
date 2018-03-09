package com.dottorsoft.SimpleBlockChain.util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.dottorsoft.SimpleBlockChain.core.Block;
import com.dottorsoft.SimpleBlockChain.core.Transaction;
import com.dottorsoft.SimpleBlockChain.core.TransactionInput;
import com.dottorsoft.SimpleBlockChain.core.TransactionOutput;

public class ChainUtils {
	
	public static Boolean isChainValid(LinkedHashMap<String, Block> blockchain,Transaction genesisTransaction) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
		Block currentBlock; 
		
		Iterator<Entry<String, Block>> it = blockchain.entrySet().iterator();
		
		Block previousBlock = it.next().getValue();
		
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
		tempUTXOs.put(genesisTransaction.getOutputs().get(0).id, genesisTransaction.getOutputs().get(0));
		
		//loop through blockchain to check hashes:
		while(it.hasNext()) {
			
			currentBlock = it.next().getValue();
			
			//compare registered hash and calculated hash:
			if(isValidHash(currentBlock.getHash(), currentBlock.calculateHash()) ){
				System.out.println("#Current Hashes not equal");
				return false;
			}
			//compare previous hash and registered previous hash
			if(previousBlock != null && (isValidHash(previousBlock.getHash(),currentBlock.getPreviousHash())) ) {
				System.out.println("#Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(containsDifficulty(currentBlock.getHash())) {
				System.out.println("#This block hasn't been mined");
				return false;
			}
			
			//loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for(int t=0; t <currentBlock.getTransactions().size(); t++) {
				Transaction currentTransaction = currentBlock.getTransactions().get(t);
				
				if(!currentTransaction.verifySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false; 
				}
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are not equal to outputs on Transaction(" + t + ")");
					return false; 
				}
				
				for(TransactionInput input: currentTransaction.getInputs()) {	
					tempOutput = tempUTXOs.get(input.transactionOutputId);
					
					if(tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}
					
					if(input.UTXO.value != tempOutput.value) {
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}
					
					tempUTXOs.remove(input.transactionOutputId);
				}
				
				for(TransactionOutput output: currentTransaction.getOutputs()) {
					tempUTXOs.put(output.id, output);
				}
				
				if( currentTransaction.getOutputs().get(0).reciepient != currentTransaction.getReciepient()) {
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}
				if( currentTransaction.getOutputs().get(1).reciepient != currentTransaction.getSender()) {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}
				
			}
			previousBlock = currentBlock;
			
		}
		System.out.println("Blockchain is valid");
		return true;
	}
	
	private static boolean isValidHash(String hash, String prevHash){
		return !hash.equals(prevHash);
	}
	
	public static boolean containsDifficulty(String hash){
		String hashTarget = new String(new char[Parameters.difficulty]).replace('\0', '0');
		return !hash.substring( 0, Parameters.difficulty).equals(hashTarget);
	}
	
	public static Block getLastBlock(){
		if(Parameters.blockchain.size() == 0 || Parameters.blockchain == null) return null;
		return Parameters.blockchain.get(Parameters.blockchain.size() -1);
	}

}
