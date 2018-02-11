package com.dottorsoft.SimpleBlockChain.networking;

public class Peer {
	
	private String ip;
	private int port;
	
	public Peer(String ip, int port){
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return String.format("Peer [ip=%s, port=%s]", ip, port);
	}
	

}
