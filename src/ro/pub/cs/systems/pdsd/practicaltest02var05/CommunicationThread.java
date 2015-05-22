package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class CommunicationThread extends Thread {
	  private Socket socket;
	  private ServerThread serverThread;
	  
	  public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
	    this.socket = socket;
	  }
	  
	  public DateTime getTimeStamp() throws ClientProtocolException, IOException{
		  
		  HttpClient httpClient = new DefaultHttpClient();
		  HttpGet httpGet = new HttpGet("http://www.timeapi.org/utc/now");
		  ResponseHandler<String> responseHandler = new BasicResponseHandler();
		  String date = httpClient.execute(httpGet, responseHandler);
		  
		 
		  
		  if(date != null && !date.isEmpty()){
			  String[] info = date.split("[-T:+]");
			  
			  return new DateTime(Integer.parseInt(info[0]),
					  Integer.parseInt(info[1]),
					  Integer.parseInt(info[2]),
					  Integer.parseInt(info[3]),
					  Integer.parseInt(info[4]),
					  Integer.parseInt(info[5])
					 );
		  }
			 
		  
		  return null;
	  }
	 
	  @Override
	  public void run() {
	    try {
	      Log.v(Constants.TAG, "Connection opened with "+socket.getInetAddress()+":"+socket.getLocalPort());
	      PrintWriter printWriter = Utilities.getWriter(socket);
	      BufferedReader reader = Utilities.getReader(socket);
	      
	      //TODO
	     if(printWriter != null && reader != null){
	    	
	    	 StringTokenizer tokenizer = new StringTokenizer(reader.readLine()," ,\\");
	    	 String cmd = tokenizer.nextToken();
	    	 
	    	 // get command
	    	 if(cmd.equalsIgnoreCase("get")){
	    		 String key = tokenizer.nextToken();
	    		 DateTime timestamp = getTimeStamp();
	    		 
	    		 if(timestamp != null){
	    			 Information res = serverThread.getValue(key);
	    			 if(res == null){
	    				 printWriter.println("none");
	    				 printWriter.flush();
	    			 }
	    			 else{
	    				 DateTime dbTimeStamp = res.getTimeStamp();
	    				 
	    				 // informatia este inca valabila
	    				 if(timestamp.toLong() - res.getTimeStamp().toLong() < 60){
	    					 printWriter.println(res.getVal());
	    					 printWriter.flush();
	    				 }
	    				 else{
	    					 printWriter.println("none");
		    				 printWriter.flush();
	    				 }
	    			 }
	    				 
	    		 } 	    		 
	    	 }
	    	 
	    	 else if(cmd.equalsIgnoreCase("put")){
	    		 String key = tokenizer.nextToken();
	    		 DateTime timestamp = getTimeStamp();
	    		 String val = tokenizer.nextToken();
	    		 
	    		 if(timestamp != null){
	    			 Information res = serverThread.setData(key, new Information(timestamp, val));
	    			 if(res == null){
	    				 printWriter.println("inserted");
	    			 }
	    			 else
	    				 printWriter.println("modified");
	    			 
	    			 printWriter.flush();
	    		 }
	    		 
	    	 }
	    	 
	    	 
	     }
	      
	      socket.close();
	      Log.v(Constants.TAG, "Connection closed");
	    } catch (IOException ioException) {
	      Log.e(Constants.TAG, "An exception has occurred: "+ioException.getMessage());
	      if (Constants.DEBUG) {
	        ioException.printStackTrace();
	      }
	    }
	  }
	}