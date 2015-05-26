package com.llkj.newbjia.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class AccessNetUtil {

	 public StringBuffer xmlBufer_str = new StringBuffer("");
	 public InputStream in;
	 public BufferedReader buferEder;
	private String result;

	public String showResPonseStr(String city) throws URISyntaxException {
	try {
		//String str  = URLEncoder.encode(URLEncoder.encode(uil, "UTF-8"),"UTF-8"); 
			URL url = new URL( "http://php.weather.sina.com.cn/xml.php?city="+java.net.URLEncoder.encode(city,"gbk")+"&password=DJOYnieT8234jlsK&day=0");
			System.out.println("ssssssssss"+url);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			InputStreamReader in = new InputStreamReader(
					connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = "";
			
			while ((line = bufferedReader.readLine()) != null) {
			
				strBuffer.append(line);
			}
			result = strBuffer.toString();
			
		} catch (Exception e) {
			// TODO: handle exception result
		}
		return  result;
	/* try {
		 HttpGet get = new HttpGet();
		
		 String  a="";
		 try {
			a= java.net.URLEncoder.encode( uil,"utf-8");
				

			} catch (UnsupportedEncodingException e1) {

				// TODO Auto-generated catch block

				e1.printStackTrace();

			}

		 //String  a=new String(uil.getBytes(""),"UTF-8");
		 get.setURI(new URI(a));
		 HttpClient client = new DefaultHttpClient();
		 HttpResponse response = client.execute(get);
		
		
		 if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		
		 HttpEntity hEntity = response.getEntity();
		 in = hEntity.getContent();
		 buferEder = new BufferedReader(new InputStreamReader(in));
		 String redStr = "";
		
		 while ((redStr = buferEder.readLine()) != null) {
		 xmlBufer_str.append(redStr);
		 }
		 in.close();
		 buferEder.close();
		 }
		
		 } catch (IOException e) {
		 e.printStackTrace();
		 }

		return xmlBufer_str.toString();
		*/

	}
}
