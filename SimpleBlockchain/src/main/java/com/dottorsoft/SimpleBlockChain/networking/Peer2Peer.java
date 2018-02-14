package com.dottorsoft.SimpleBlockChain.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class Peer2Peer {
	
	private int port = 8888;
	private Socket socket;
	private ServerSocket server;
	private LinkedList<Peer> peers = new LinkedList<Peer>();
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
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
		
		Peer peer;
		while(isRunning){
			
			Socket clientSocket = server.accept();
			DataInputStream input = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
			System.out.println("Connection Received from: "+clientSocket.toString());
			
			peer = new Peer(clientSocket.getInetAddress().getHostAddress(),clientSocket.getPort());
			peers.add(peer);
			
			System.out.println(peer.toString());
			
			System.out.println("connected peers "+peers.size());
			
			command = input.readUTF();
			System.out.println(command);
			
			output.writeUTF(Dispatcher.getInstance().elborateCommands(command));
			
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
			outputStream = new DataOutputStream(socket.getOutputStream());
			inputStream = new DataInputStream(socket.getInputStream());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String receive(){
		String data = null;
		try {
			data = inputStream.readUTF();
			inputStream.close();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
			return data;
		}
	}
	
	public void send(String data){
		try {
			outputStream.writeUTF(data);
			outputStream.flush();
			
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
	
	public void ping(){
		
	}
	

}
