package ro.pub.cs.brtalk.communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import ro.pub.cs.brtalk.interfaces.ISocket;
import android.util.Log;

public class Socket implements ISocket {
	
	private ServerSocket serverSock = null;

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

	@Override
	public String sendHttpRequest(String params) {
		// TODO Auto-generated method stub
		Boolean notReached = false;
		
		try {
			notReached = InetAddress.getByName("192.168.0.106").isReachable(5000);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (notReached == false){
			return "NOT REACHED";
		}
		
		HttpClient client = new DefaultHttpClient();  
		HttpGet get = new HttpGet("http://192.168.0.106/pdsd/index.php?" + params);
		 
		ResponseHandler<String> handler = new BasicResponseHandler();
		String response = null;
		 
		try {  
		        response = client.execute(get,handler);
		        
		} catch (ClientProtocolException e) {
		        e.printStackTrace();
		} catch (IOException e){
		    	e.printStackTrace();
		}
		
		Log.i("BRTALK", response);
		
		return response;
	}

}
