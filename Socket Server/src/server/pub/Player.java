package server.pub;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintWriter;

public class Player {

	private String playerName;
	private int balance;
	private BufferedReader is;
	private PrintWriter printWriter;

	public Player(PrintWriter printWriter, BufferedReader is) {
		this.is=is;
		this.printWriter = printWriter;
	}

	public String getPlayerName() {
		return playerName;
	}

	public BufferedReader getBufferedReader() {
		return is;
	}

	public void setBufferedReader(BufferedReader is) {
		this.is = is;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

}
