package server.pri;

import java.math.BigInteger;
import java.net.*;
import java.security.SecureRandom;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerDispatcher extends Thread {

	private boolean roomAlive = true;
	private Vector mMessageQueue = new Vector();
	private Vector mClients = new Vector();
	private ClientInfo senderClientInfo;
	private String roomID;

	// private Player creator,player;
	public ServerDispatcher() {
		roomID = generateRandomID();
	}

	/**
	 * Adds given client to the server's client list.
	 */
	public synchronized void addClient(ClientInfo aClientInfo) {

		System.out.println("new Client has been added");
		mClients.add(aClientInfo);

	if (mClients.size() == 2) {
		
		if(aClientInfo.playerName.equals("")){
			JSONObject creatorJsonObject = new JSONObject();
			try {
				creatorJsonObject.put("successful", false);
				creatorJsonObject.put("role", "Guest");
				creatorJsonObject.put("act", "nothing");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendMessageToAllClients(creatorJsonObject.toString());
			
		}else{
		setSenderIndex((ClientInfo) mClients.get(1));
		JSONObject creatorJsonObject = new JSONObject();
		try {
			creatorJsonObject.put("successful", true);
			creatorJsonObject.put("role", "Creator");
			creatorJsonObject.put("act", "opponentEnteredAct");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sendMessageToAllClients(creatorJsonObject.toString());

		setSenderIndex((ClientInfo) mClients.get(0));
		JSONObject guestJsonObject = new JSONObject();
		try {
			guestJsonObject.put("successful", true);
			guestJsonObject.put("role", "Guest");
			guestJsonObject.put("act", "opponentEnteredAct");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sendMessageToAllClients(guestJsonObject.toString());
	}
	}
	System.out.println("room size: " + mClients.size());

		

	}

	public void setSenderIndex(ClientInfo senderClientInfo) {
		this.senderClientInfo = senderClientInfo;
	}

	/**
	 * Deletes given client from the server's client list if the client is in
	 * the list.
	 */
	public synchronized void deleteClient(ClientInfo aClientInfo) {
		int clientIndex = mClients.indexOf(aClientInfo);
		if (clientIndex != -1)
			mClients.removeElementAt(clientIndex);
	}

	/**
	 * Adds given message to the dispatcher's message queue and notifies this
	 * thread to wake up the message queue reader (getNextMessageFromQueue
	 * method). dispatchMessage method is called by other threads
	 * (ClientListener) when a message is arrived.
	 */
	public synchronized void dispatchMessage(ClientInfo aClientInfo, String aMessage) {
		Socket socket = aClientInfo.mSocket;
		String senderIP = socket.getInetAddress().getHostAddress();
		String senderPort = "" + socket.getPort();
		/* aMessage = senderIP + ":" + senderPort + " : " + aMessage; */
		mMessageQueue.add(aMessage);
		setSenderIndex(aClientInfo);

		notify();
	}

	/**
	 * @return and deletes the next message from the message queue. If there is
	 *         no messages in the queue, falls in sleep until notified by
	 *         dispatchMessage method.
	 */
	private synchronized String getNextMessageFromQueue() throws InterruptedException {
		while (mMessageQueue.size() == 0)
			wait();
		String message="";
		try {
			 message = (String) mMessageQueue.get(0);
			mMessageQueue.removeElementAt(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return message;
	}

	/**
	 * Sends given message to all other clients in the client list. Actually the
	 * message is added to the client sender thread's message queue and this
	 * client sender thread is notified.
	 */
	private synchronized void sendMessageToAllClients(String aMessage) {
		for (int i = 0; i < mClients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			if (senderClientInfo != clientInfo) {
			try{
				clientInfo.mClientSender.sendMessage(aMessage);
				
			}catch(Exception exception){
				
			}
				
			}

		}
	}

	/**
	 * Infinitely reads messages from the queue and dispatch them to all clients
	 * connected to the server.
	 */

	public void run() {
		try {
			while (isRoomAlive()) {
				String message = getNextMessageFromQueue();

				if (message.equals("leaveGame")) {
					System.out.println("player leaved the room");

					/*
					 * if(getPlayersCount()>1){ notifiOfPlayerLeave(); }
					 */
//					closeRoom();

				} else {
					sendMessageToAllClients(message);
				}

			}
			

		} catch (InterruptedException ie) {
			// Thread interrupted. Stop its execution
			notifiOfPlayerLeave();
		}
	}

	void notifiOfPlayerLeave() {

		for (int i = 0; i < mClients.size(); i++) {

			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			clientInfo.mClientSender.sendMessage("{\"act\":\"opponentDisconnected\"}");
		}

	}

	private boolean isRoomAlive() {
		// TODO Auto-generated method stub
		return roomAlive;
	}

	/*public void closeRoom() {
		// TODO Auto-generated method stub
	ClientInfo clientInfo = new ClientInfo();
	clientInfo.playerName="";
	addClient(clientInfo);
		mClients.add((Object) );
		mClients.add((Object) new ClientInfo());
		mClients.add((Object) new ClientInfo());
		roomAlive = false;
		NakovChatServer.removeDispatcher(roomID);
	

	}
*/
	public int getPlayersCount() {
		System.out.println("mClients.size(): " + mClients.size());
		return mClients.size();

	}

	public String generateRandomID() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	public String getRoomID() {
		// TODO Auto-generated method stub
		return roomID;
	}

}