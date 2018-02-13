package com.dottorsoft.SimpleBlockChain.networking;

import com.dottorsoft.SimpleBlockChain.util.Commands;

public class ExecuteCommands {
	
	private Peer2Peer networking;
	
	public ExecuteCommands(){}
	
	public ExecuteCommands(int port){
		networking = new Peer2Peer(port);
		
	}
	
	/**
	 * Used for client mode
	 */
	public void connect(String host, int port){
		networking.connect(host, port);
	}
	
	public String getBlockChain(){
		networking.send(Commands.GET_BLOCKCHAIN.getCommand());
		return networking.receive();
	}
	
	public int getBlockChainSize(){
		networking.send(Commands.GET_BLOCK_CHAIN_SIZE.getCommand());
		return Integer.parseInt(networking.receive());
	}
	
	public String postLastMinedBlock(){
		networking.send(Commands.POST_LAST_MINED_BLOCK.getCommand());
		return networking.receive();
	}
	
	public String receive(){
		return networking.receive();
	}

}
