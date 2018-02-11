package com.dottorsoft.SimpleBlockChain.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.dottorsoft.SimpleBlockChain.util.Parameters;
import com.dottorsoft.SimpleBlockChain.util.StringUtil;

public class Peer2Peer {
	
	private int port = 8888;
	private Socket socket;
	private ServerSocket server;
	private ArrayList<Peer> peers;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private Thread peerThread;
	private Thread sendThread;
	
	public Peer2Peer(int port){
		this.port = port;
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
		while(true){
			socket = server.accept();
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());
			System.out.println("Connection Received from: "+socket.toString());
			
			System.out.println(socket.getPort()+" "+socket.getInetAddress().toString());
			
			command = inputStream.readUTF();
			System.out.println(command);
			
			//outputStream = new DataOutputStream(socket.getOutputStream());
			if(command.equals("getblockchain")){
				
				System.out.println("yes");
				outputStream.writeUTF(StringUtil.getJson(Parameters.blockchain));
			}
			
		}
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
	
	public void receive(){
		try {
			
			System.out.println("Receiving: "+inputStream.readUTF());
		} catch (IOException e) {
			e.printStackTrace();
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

}
