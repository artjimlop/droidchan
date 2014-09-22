package utils;

import it.sauronsoftware.ftp4j.FTPClient;

import java.io.File;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;

public class ImageGETFTPHandler extends AsyncTask<String, Float, Drawable> {
		
		/*********  work only for Dedicated IP ***********/
	    static final String FTP_HOST= "31.170.160.155";
	     
	    /*********  FTP USERNAME ***********/
	    static final String FTP_USER = "a2902707";
	     
	    /*********  FTP PASSWORD ***********/
	    static final String FTP_PASS  ="br0nx0nn";
	    /*
	     * mFTPClient = new FTPClient();
  mFTPClient.connect("tgftp.nws.noaa.gov");      
  mFTPClient.login("anonymous","nobody");
  mFTPClient.enterLocalPassiveMode();
  mFTPClient.changeWorkingDirectory("/data/forecasts/taf/stations");
  InputStream inStream = mFTPClient.retrieveFileStream("KABQ.TXT");
  InputStreamReader isr = new InputStreamReader(inStream, "UTF8");
	     */
	    FTPClient client = null;
		@SuppressLint("SdCardPath")
		protected Drawable doInBackground(String... strings) {
			FTPClient client = new FTPClient();
			String image = strings[0];
			File fileDownload = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),image);
			try {
				client.connect(FTP_HOST,21);
				client.login(FTP_USER, FTP_PASS);
				client.setType(FTPClient.TYPE_BINARY);
				client.changeDirectory("/images/");
				//"31.170.160.155/images/"+image
				client.download(image, fileDownload);
			} catch (Exception e) {
				e.printStackTrace();
				try {
					client.disconnect(true);	
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			   Drawable d = Drawable.createFromPath(fileDownload.getPath());
			   return d;
		}
	}

