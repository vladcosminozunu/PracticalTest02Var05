package ro.pub.cs.systems.pdsd.practicaltest02var05;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02Var05MainActivity extends Activity {

	private EditText serverPortEditText, clientPortEditText, clientAddressEditText, cmdEditText;
	private Button startServerButton, connectToServerButton;
	private TextView resultTextView;
	
	private ServerThread serverThread;
	
	private class StartServerListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			String port = serverPortEditText.getText().toString();
			if(port != null && !port.isEmpty()){
				
				serverThread = new ServerThread(Integer.parseInt(port));
				serverThread.startServer();
				
			}
			else{
				Toast.makeText(getApplicationContext(), "Server port not specified", Toast.LENGTH_LONG).show();
			}
			
		}
		
	}
	
	private class ConnectToServerListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String port = clientPortEditText.getText().toString();
			String address = clientAddressEditText.getText().toString();
			String cmd = cmdEditText.getText().toString();
			
			if(port != null && !port.isEmpty() && address != null && !address.isEmpty() && cmd != null
					&& !cmd.isEmpty()){
				//TODO 
				
				new ClientThread(address, Integer.parseInt(port), cmd, resultTextView).start();
			}
			else{
				Toast.makeText(getApplicationContext(), "Not all info for client specified", Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test02_var05_main);
		
		serverPortEditText = (EditText) findViewById(R.id.server_port_edit_text);
		clientPortEditText= (EditText) findViewById(R.id.client_port_edit_text);
		clientAddressEditText = (EditText) findViewById(R.id.client_address_edit_text);
		cmdEditText = (EditText) findViewById(R.id.command_edit_text);
		
		startServerButton = (Button) findViewById(R.id.start_server_button);
		connectToServerButton = (Button) findViewById(R.id.connect_to_server_button);
		
		startServerButton.setOnClickListener(new StartServerListener());
		connectToServerButton.setOnClickListener(new ConnectToServerListener());
		
		resultTextView = (TextView) findViewById(R.id.result_text_view);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test02_var05_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onDestroy() {
	  super.onDestroy();
	  serverThread.stopServer();
	}
}
