package com.dottorsoft.SimpleBlockChain.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import com.dottorsoft.SimpleBlockChain.util.Commands;

public class Peer2Peer {
	
	private int port = 8888;
	private Socket socket;
	private ServerSocket server;
	private LinkedList<Peer> peers = new LinkedList<Peer>();
	private DataInputStream inputStreamClient;
	private DataOutputStream outputStreamClient;
	private Thread peerThread;
	private Thread sendThread;
	
	private boolean isRunning;
	
	public Peer2Peer(int port){
		this.port = port;
		isRunning = true;
		peerThread = new Thread(new Runnable() {
			   public void run() {
			       try {
					listen();
				} catch (IOException e) {
					e.getStackTrace();
				}
			   }
			});
		
		peerThread.start();
	}
	
	private void listen() throws IOException{
		
		System.out.println("Server Starting");
		server = new ServerSocket(port);
		System.out.println("Server Started port: "+port);
		String command;
		
		Socket clientSocket;
		
		DataInputStream input;
		DataOutputStream output;
		
		while(isRunning){
			
			clientSocket = server.accept();
			input = new DataInputStream(clientSocket.getInputStream());
			output = new DataOutputStream(clientSocket.getOutputStream());
			System.out.println("Connection Received from: "+clientSocket.toString());
			
			command = input.readUTF();
			System.out.println(command);
			
			Peer peer = null;
			
			if(command.contains(Commands.REGISTERING.getCommand())){
				output.writeUTF("registered");
				peer = new Peer(clientSocket.getInetAddress().getHostAddress(),clientSocket.getLocalPort());
				System.out.println(peer.toString());
				peers.add(peer);
			}else{
				output.writeUTF(CommandsDispatcher.getInstance().elaborateCommands(command));
			}
			
			output.close();
			input.close();
		}
	}
	
	public void stop(){
		isRunning = false;
	}
	
	public void connect(String host, int port){
		try {
			socket = new Socket(host, port);
			outputStreamClient = new DataOutputStream(socket.getOutputStream());
			inputStreamClient = new DataInputStream(socket.getInputStream());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String receive(){
		String data = null;
		try {
			data = inputStreamClient.readUTF();
			inputStreamClient.close();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
			return data;
		}
	}
	
	public void send(String data){
		try {
			outputStreamClient.writeUTF(data);
			outputStreamClient.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LinkedList<Peer> getPeers() {
		return peers;
	}

	public void setPeers(LinkedList<Peer> peers) {
		this.peers = peers;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	

}
