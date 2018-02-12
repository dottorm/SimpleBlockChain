package com.dottorsoft.SimpleBlockchain.main.networking;

import com.dottorsoft.SimpleBlockchain.main.util.ChainUtils;
import com.dottorsoft.SimpleBlockchain.main.util.Commands;
import com.dottorsoft.SimpleBlockchain.main.util.Parameters;
import com.dottorsoft.SimpleBlockchain.main.util.StringUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Peer2Peer {

	private int port = 8888;
	private Socket socket;
	private ServerSocket server;
	private ArrayList<Peer> peers = new ArrayList<Peer>();
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

		Peer peer;

		while(true){

			socket = server.accept();
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());
			System.out.println("Connection Received from: "+socket.toString());

			peer = new Peer(socket.getInetAddress().toString(),socket.getPort());
			peers.add(peer);

			System.out.println("New Peer: "+peer.toString());

			command = inputStream.readUTF();
			System.out.println(command);

			outputStream.writeUTF(elborateCommands(command));

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

	public String receive(){
		String data = null;
		try {
			data = inputStream.readUTF();
			System.out.println("Receiving: "+data);
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

	private String elborateCommands(String command){

		if(command == null) return null;

		if(command.equals(Commands.GET_BLOCKCHAIN.getCommand())){return StringUtil.getJson(Parameters.blockchain);}
		if(command.equals(Commands.GET_LAST_BLOCK.getCommand())){return StringUtil.getJson(ChainUtils.getLastBlock());}

		return null;
	}

}