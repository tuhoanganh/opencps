
package org.opencps.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostRequest {

	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args)
	    throws Exception {

		PostRequest http = new PostRequest();
		// Sending get request
		//http.sendingGetRequest();
		// Sending post request
		http.sendingPostRequest();
	}

	private void sendingGetRequest()
	    throws Exception {

		String urlString =
		    "http://qa.opencps.vn/opencps-portlet/VTCPayServlet";
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		// By default it is GET request
		con.setRequestMethod("GET");
		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("Sending get request : " + url);
		System.out.println("Response code : " + responseCode);
		// Reading response from input Stream
		BufferedReader in =
		    new BufferedReader(new InputStreamReader(con.getInputStream()));
		String output;
		StringBuffer response = new StringBuffer();
		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close();
		// printing result from response
		System.out.println(response.toString());
	}

	private void sendingPostRequest()
	    throws Exception {

		String url =
		    "http://qa.opencps.vn/opencps-portlet/VTCPayServlet";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection(); 
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/json");
		String postJsonData =
		    "{\"id\":5,\"countryName\":\"USA\",\"population\":8000}"; 
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postJsonData);
		wr.flush();
		wr.close();
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post Data : " + postJsonData);
		System.out.println("Response Code : " + responseCode);
		BufferedReader in =
		    new BufferedReader(new InputStreamReader(con.getInputStream()));
		String output;
		StringBuffer response = new StringBuffer();
		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close(); // printing result from response
		System.out.println(response.toString());
	}

}
