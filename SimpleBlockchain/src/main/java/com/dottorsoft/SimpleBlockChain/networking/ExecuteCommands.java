package com.dottorsoft.SimpleBlockChain.networking;

import com.dottorsoft.SimpleBlockChain.util.Commands;

public class ExecuteCommands {
	
	private Peer2Peer networking;
	
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
	
	public String getLastBlock(){
		networking.send(Commands.GET_LAST_BLOCK.getCommand());
		return networking.receive();
	}
	
	public String receive(){
		return networking.receive();
	}

}
