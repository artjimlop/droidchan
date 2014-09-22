package utils;

import it.sauronsoftware.ftp4j.FTPClient;
import android.os.AsyncTask;

public class ImageFTPRemover extends AsyncTask<String, Float, Integer>{
	/*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "31.170.160.155";
     
    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "a2902707";
     
    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="br0nx0nn";
    
    
	protected Integer doInBackground(String... filename) {
		Integer res = 0;
		FTPClient client = new FTPClient();
		String file = filename[0];
		
	     
		   try {
		
		       client.connect(FTP_HOST,21);
		
		       client.login(FTP_USER, FTP_PASS);
		
		       client.setType(FTPClient.TYPE_BINARY);
		       client.changeDirectory("/images/");
		       client.deleteFile(file);
//		        client.upload(fileName);
		   } catch (Exception e) {
		       e.printStackTrace();
		       try {
		
		           client.disconnect(true);    
		       } catch (Exception e2) {
		           e2.printStackTrace();
		       }
		   }
		return res;
	}


	
}
