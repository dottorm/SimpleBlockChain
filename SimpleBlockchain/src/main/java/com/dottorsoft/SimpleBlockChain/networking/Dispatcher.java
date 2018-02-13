package com.dottorsoft.SimpleBlockChain.networking;

import com.dottorsoft.SimpleBlockChain.util.ChainUtils;
import com.dottorsoft.SimpleBlockChain.util.Commands;
import com.dottorsoft.SimpleBlockChain.util.Parameters;
import com.dottorsoft.SimpleBlockChain.util.StringUtil;

public class Dispatcher {
	
	private static Dispatcher dispatcher = null;
	
	private Dispatcher(){}
	
	public static Dispatcher getInstance(){
		
		if(dispatcher == null){
			dispatcher = new Dispatcher();
		}
		
		return dispatcher;
	}
	
	public String elborateCommands(String command){
		
		if(command == null) return null;
		
		if(command.equals(Commands.GET_BLOCKCHAIN.getCommand())){return StringUtil.getJson(Parameters.blockchain);}
		if(command.equals(Commands.POST_LAST_MINED_BLOCK.getCommand())){return StringUtil.getJson(ChainUtils.getLastBlock());}
		if(command.equals(Commands.GET_BLOCK_CHAIN_SIZE.getCommand())){return StringUtil.getJson(Parameters.blockchain.size());}
		
		return null;
	}

}
