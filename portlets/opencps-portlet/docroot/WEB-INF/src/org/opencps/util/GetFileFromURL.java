package org.opencps.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.opencps.servicemgt.model.TemplateFile;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;


public class GetFileFromURL {
	
	private static final String USER_AGENT = "Mozilla/5.0";

	
	private static String getFileURL(long fileId, String baseURL)
	    throws IOException, JSONException {

		String fileURL = StringPool.BLANK;

		String urlString =
		    baseURL + "/api/jsonws/dlapp/get-file-entry/file-entry-id/" +
		        Long.toString(fileId);

		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		// By default it is GET request
		con.setRequestMethod("GET");
		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		String userpass = "vanthu1.cps@gmail.com" + ":" + "cps12345";
		String basicAuth =
		    "Basic " + new String(new Base64().encode(userpass.getBytes()));
		con.setRequestProperty("Authorization", basicAuth);

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

		JSONObject fileJSON =
		    JSONFactoryUtil.createJSONObject(response.toString());

		long groupId = fileJSON.getLong("groupId");

		String uuid = fileJSON.getString("uuid");

		fileURL = getURLDownloadFile(baseURL, groupId, uuid);
		
		return fileURL;
	}

	/**
	 * @param baseURL
	 * @param groupId
	 * @param uuid
	 * @return
	 */
	private static String getURLDownloadFile(
	    String baseURL, long groupId, String uuid) {

		String result = StringPool.BLANK;

		DLFileEntry dlFileEntry = null;

		TemplateFile templateFile = null;

		try {
			if (Validator.isNotNull(templateFile)) {

				dlFileEntry =
				    DLFileEntryLocalServiceUtil.getDLFileEntry(templateFile.getFileEntryId());

				result =
				    baseURL + "/c/document_library/get_file?uuid=" + uuid +
				        "&groupId=" + groupId;

			}

		}
		catch (Exception e) {

		}

	return result;
	}

	
	private static byte[] getFileFromURL(long fileId, String baseURL) throws IOException, JSONException {
		
		String fileURL = getFileURL(fileId, baseURL);
		
		HttpURLConnection connection = null;
		byte[] bytes = null;
		if(Validator.isNotNull(fileURL)) {
			
			try {
				URL url = new URL(fileURL);
				
				connection = (HttpURLConnection) url.openConnection();
				connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
				connection.addRequestProperty("User-Agent", "Mozilla");
				connection.addRequestProperty("Referer", "google.com");
				
				connection.setInstanceFollowRedirects(false);
				connection.setConnectTimeout(5000);	// 5s
				connection.setReadTimeout(5000);	// 5s
				
				int status = connection.getResponseCode();
				
				boolean redirect = false;

				// normally, 3xx is redirect
				if (status != HttpURLConnection.HTTP_OK) {
					if (status == HttpURLConnection.HTTP_MOVED_TEMP
						|| status == HttpURLConnection.HTTP_MOVED_PERM
							|| status == HttpURLConnection.HTTP_SEE_OTHER)
					redirect = true;
				}
				
				if (redirect) {

					// get redirect url from "location" header field
					String newUrl = connection.getHeaderField("Location");

					// get the cookie if need, for login
					String cookies = connection.getHeaderField("Set-Cookie");

					// open the new connnection again
					connection = (HttpURLConnection) new URL(newUrl).openConnection();
					
					connection.setRequestProperty("Cookie", cookies);
					connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
					connection.addRequestProperty("User-Agent", "Mozilla");
					connection.addRequestProperty("Referer", "google.com");
					
					connection.setConnectTimeout(5000);	// 5s
					connection.setReadTimeout(5000);	// 5s
											
					status = connection.getResponseCode();
				}
			
				if(status == HttpURLConnection.HTTP_OK) {
					InputStream is = connection.getInputStream();
					//File file = FileUtil.createTempFile(is);
					//long size = connection.getContentLengthLong();
					//_log.info("===fileURL===" + fileURL + "===" + file.getAbsolutePath());
					//_log.info("===fileURL===" + fileURL + "===" + size);
					
					bytes = FileUtil.getBytes(is);
					
					//FileUtil.createTempFile(bytes);
				}
			} catch(IOException ioe) {
				throw new IOException(ioe.getMessage());
			}finally{
				connection.disconnect();
			}
		}
		
		return bytes;
	}
	

}
