package com.dottorsoft.SimpleBlockChain.networking;

public class Peer {
	
	private String ip;
	private String port;
	
	public Peer(String ip, String port){
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	

}
