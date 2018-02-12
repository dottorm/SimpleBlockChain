package com.dottorsoft.SimpleBlockchain.main.util;

import com.dottorsoft.SimpleBlockchain.main.core.Block;
import com.dottorsoft.SimpleBlockchain.main.core.Transaction;
import com.dottorsoft.SimpleBlockchain.main.core.TransactionInput;
import com.dottorsoft.SimpleBlockchain.main.core.TransactionOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class ChainUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChainUtils.class);

	public static Boolean isChainValid(ArrayList<Block> blockchain,Transaction genesisTransaction) {
		Block currentBlock;
		Block previousBlock;
		String hashTarget = new String(new char[Parameters.difficulty]).replace('\0', '0');
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<>(); //a temporary working list of unspent transactions at a given block state.
		tempUTXOs.put(genesisTransaction.getOutputs().get(0).id, genesisTransaction.getOutputs().get(0));

		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {

			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(isValidHash(currentBlock.getHash(), currentBlock.calculateHash()) ){
				LOGGER.debug("#Current Hashes not equal");
				return false;
			}
			//compare previous hash and registered previous hash
			if(isValidHash(previousBlock.getHash(),currentBlock.getPreviousHash()) ) {
				LOGGER.debug("#Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.getHash().substring( 0, Parameters.difficulty).equals(hashTarget)) {
				LOGGER.debug("#This block hasn't been mined");
				return false;
			}

			//loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for(int t=0; t <currentBlock.getTransactions().size(); t++) {
				Transaction currentTransaction = currentBlock.getTransactions().get(t);

				if(!currentTransaction.verifiySignature()) {
					LOGGER.debug("#Signature on Transaction( {} ) is Invalid", t);
					return false;
				}
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					LOGGER.debug("#Inputs are note equal to outputs on Transaction( {} )", t);
					return false;
				}

				for(TransactionInput input: currentTransaction.getInputs()) {
					tempOutput = tempUTXOs.get(input.transactionOutputId);

					if(tempOutput == null) {
						LOGGER.debug("#Referenced input on Transaction( {} ) is Missing", t);
						return false;
					}

					if(input.UTXO.value != tempOutput.value) {
						LOGGER.debug("#Referenced input Transaction( {} ) value is Invalid", t);
						return false;
					}

					tempUTXOs.remove(input.transactionOutputId);
				}

				for(TransactionOutput output: currentTransaction.getOutputs()) {
					tempUTXOs.put(output.id, output);
				}

				if( currentTransaction.getOutputs().get(0).reciepient != currentTransaction.getReciepient()) {
					LOGGER.debug("#Transaction( {} ) output reciepient is not who it should be", t);
					return false;
				}
				if( currentTransaction.getOutputs().get(1).reciepient != currentTransaction.getSender()) {
					LOGGER.debug("#Transaction( {} ) output 'change' is not sender.", t);
					return false;
				}

			}

		}
		LOGGER.debug("Blockchain is valid");
		return true;
	}

	private static boolean isValidHash(String hash, String prevHash){
		return !hash.equals(prevHash);
	}

	public static Block getLastBlock(){
		if(Parameters.blockchain.size() == 0 || Parameters.blockchain == null) return null;

		return Parameters.blockchain.get(Parameters.blockchain.size() -1);
	}

}
