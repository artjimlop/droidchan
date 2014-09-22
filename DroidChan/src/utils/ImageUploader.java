package utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import business.BusinessImage;
import business.BusinessThreadImage;
import domain.Image;
import domain.ThreadImage;

public class ImageUploader {
	
	private Activity activity;
	
	private TextView messageText;
	private int serverResponseCode = 0;
	private ProgressDialog dialog = null;
        
	private String upLoadServerUri = null;
	
	public ImageUploader(Activity activity){
		this.activity = activity;
	}
	

     
    /**********  File Path *************/
//    final String uploadFilePath = "/mnt/sdcard/";ç
    private String uploadFileName;
	
	public int uploadFile(Bitmap bmp, String login) {
        
    	//Direccion a la que lo subo
    	Integer random = (int) Math.random();
    	String randomString = random.toString();
    	final String uploadFilePath = "http://192.168.1.108/images/"+randomString;
    	uploadFileName = randomString;
    	//salvo el bmp como archivo temporal
//    	File outputDir = activity.getCacheDir(); // context being the Activity pointer
//    	File outputFile = null;
//    	try {
//			outputFile = File.createTempFile("prefix", "extension", outputDir);
//			
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
    	File outputFile = new File(activity.getFilesDir(), randomString);
    	FileOutputStream outputStream;
    	try {
    	
    		PhotoUtils pU = new PhotoUtils(activity);
      
    		  outputStream = activity.openFileOutput(randomString, activity.MODE_PRIVATE);
    		  outputStream.write(pU.BitmapToByteArray(bmp));
    		  outputStream.close();
    		} catch (Exception e) {
    		  e.printStackTrace();
    		}
    	//Lo paso a png
//    	FileOutputStream fileOutStr = new FileOutputStream(uploadFilePath);
//        BufferedOutputStream bufOutStr = new BufferedOutputStream(fileOutStr);
//    	FileOutputStream fOut = null;
//		try {
//			fOut = new FileOutputStream(outputFile);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
//    	bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        
    	
        String fileName = randomString;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = outputFile; 
        
        
        if (!sourceFile.isFile()) {
        		
             dialog.dismiss(); 
              
             Log.e("uploadFile", "Source File not exist :"
                                 +uploadFilePath + "" + uploadFileName);
              
             activity.runOnUiThread(new Runnable() {
                 public void run() {
                     messageText.setText("Source File not exist :"
                             +uploadFilePath + "" + uploadFileName);
                 }
             }); 
              
             return 0;
          
        }
        else
        {
             try { 
                  
                   // open a URL connection to the Servlet
                 FileInputStream fileInputStream = new FileInputStream(sourceFile);
                 upLoadServerUri = "http://192.168.1.108/images/uploader.php";
                 URL url = new URL(upLoadServerUri);
                  
                 // Open a HTTP  connection to  the URL
                 conn = (HttpURLConnection) url.openConnection();
                 //Funcionan:
                 conn.setDoInput(true); // Allow Inputs
                 conn.setDoOutput(true); // Allow Outputs
                 conn.setUseCaches(false); // Don't use a Cached Copy
                 conn.setRequestMethod("POST");
                 conn.setRequestProperty("Connection", "Keep-Alive");
                 conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                 conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                 conn.setRequestProperty("uploaded_file", fileName); 
                  
                 //PROBLEMA!
                 
                 dos = new DataOutputStream(conn.getOutputStream());
                 
                 
                 dos.writeBytes(twoHyphens + boundary + lineEnd); 
                 dos.writeBytes("Content-Disposition: form-data; name=+uploaded_file;"+"filename="
                                           + fileName + "" + lineEnd);
                  
                 dos.writeBytes(lineEnd);
        
                 // create a buffer of  maximum size
                 bytesAvailable = fileInputStream.available(); 
        
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                 buffer = new byte[bufferSize];
        
                 // read file and write it into form...
                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                    
                 while (bytesRead > 0) {
                      
                   dos.write(buffer, 0, bufferSize);
                   bytesAvailable = fileInputStream.available();
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                    
                  }
        
                 // send multipart form data necesssary after file data...
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        
                 // Responses from the server (code and message)
                 serverResponseCode = conn.getResponseCode();
                 String serverResponseMessage = conn.getResponseMessage();
                   
                 Log.i("uploadFile", "HTTP Response is : "
                         + serverResponseMessage + ": " + serverResponseCode);
                  
                 if(serverResponseCode == 200){
                      
                     activity.runOnUiThread(new Runnable() {
                          public void run() {
                               
                              String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                            +" http://www.androidexample.com/media/uploads/"
                                            +uploadFileName;
                               
                              messageText.setText(msg);
                              Toast.makeText(activity, "File Upload Complete.", 
                                           Toast.LENGTH_SHORT).show();
                          }
                      });                
                 }    
                  
                 //close the streams //
                 fileInputStream.close();
                 dos.flush();
                 dos.close();
                   
            } catch (MalformedURLException ex) {
                 
                dialog.dismiss();  
                ex.printStackTrace();
                 
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(activity, "MalformedURLException", 
                                                            Toast.LENGTH_SHORT).show();
                    }
                });
                 
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
            } catch (Exception e) {
                 
                dialog.dismiss();  
                e.printStackTrace();
                 
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(activity, "Got Exception : see logcat ", 
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "
                                                 + e.getMessage(), e);  
            }
             BusinessImage businessImage = new BusinessImage(activity);
     	    BusinessThreadImage businessImageThread = new BusinessThreadImage(activity);
             String image = uploadFilePath+randomString;
         	Image photo = new Image(image, 0, login);
         	ThreadImage threadImage = new ThreadImage(image, 0, login, 0, 0);
         	//En primer lugar tengo que insertar la foto, despues inserto el
         	//"vinculo" al tema:
         	businessImage.insertImage(photo);
         	businessImageThread.insertImage(threadImage);
         	

             
             dialog.dismiss();       
            return serverResponseCode; 
             
         } // End else block 
              
	}
	
	public static Drawable LoadImageFromWebOperations(String url) {
	    try {
	        InputStream is = (InputStream) new URL(url).getContent();
	        Drawable d = Drawable.createFromStream(is, "src name");
	        return d;
	    } catch (Exception e) {
	        return null;
	    }
	}
	
}
