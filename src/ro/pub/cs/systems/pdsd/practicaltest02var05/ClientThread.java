package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.widget.TextView;


public class ClientThread extends Thread{
	
	private String address;
	private int port;
	private String command; 
	private TextView resultTextView;
	
	public ClientThread(String adr, int port, String cmd, TextView textView){
		this.address = adr;
		this.port = port;
		this.command = cmd;
		this.resultTextView = textView;
	}
	
	@Override
	public void run(){
		Socket socket = null;
		try {
			socket = new Socket(address, port);
			
			if(socket != null){
				PrintWriter writer = Utilities.getWriter(socket);
				BufferedReader reader = Utilities.getReader(socket);
				
				if(writer != null && reader != null){
					
					writer.println(command);
					writer.flush();
					
					// primire rezultat
					final String res = reader.readLine();
					
					resultTextView.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							resultTextView.setText(res);
						}
					});

				}
				
			
				
				socket.close();
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
