package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import android.util.Log;

public class ServerThread extends Thread {
	private boolean isRunning;
	private int serverPort;
	private HashMap<String, Information> data = null;

	private ServerSocket serverSocket;

	public ServerThread(int port){
		this.serverPort = port;
		data = new HashMap<String, Information>();
	}

	public synchronized Information setData(String key, Information info) {
		return data.put(key, info);
	}

	public synchronized Information getValue(String key) {
		return data.get(key);
	}

	public void startServer() {
		isRunning = true;
		start();
	}

	public void stopServer() {
		isRunning = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (serverSocket != null) {
						serverSocket.close();
					}
					Log.v(Constants.TAG, "stopServer() method invoked "+serverSocket);
				} catch(IOException ioException) {
					Log.e(Constants.TAG, "An exception has occurred: "+ioException.getMessage());
					if (Constants.DEBUG) {
						ioException.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(serverPort);
			while (isRunning) {
				Socket socket = serverSocket.accept();
				new CommunicationThread(this, socket).start();
			}
		} catch (IOException ioException) {
			Log.e(Constants.TAG, "An exception has occurred: "+ioException.getMessage());
			if (Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
	}
}