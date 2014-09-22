package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.googlecode.androidannotations.annotations.Background;

public class PhotoUtils {

	Activity activity;
	
	public PhotoUtils(Activity activity){
		super();
		this.activity=activity;
	}
	
	public static File createTemporaryFile(String part, String ext,
            Context myContext) throws Exception {
        File tempDir = myContext.getExternalCacheDir();
        tempDir = new File(tempDir.getAbsolutePath() + "/temp/");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }
	
	//Aqui se ha utilizado del framework android annotations
	/*
	 * Poco que resaltar aquí, aunque sí comentar que el método
	 * se basa en usar un InputStream, que cogerá la foto usando
	 * el Uri, y que si lo considera necesario, lo bajará de internet.
	 * Es muy importante por tanto que este método se ejecute en segundo 
	 * plano. En nuestro caso, utilizamos Android Annotations, lo cual 
	 * nos permite usar el tag @Background para indicar que el método 
	 * se ejecutará de dicho modo
	 */
	@Background
	public Bitmap getImage(Uri uri) {
	    
		Bitmap result = null;
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    InputStream is = null;
	    try { //mContext da problemas
	    	//activity era antes mContext
			is = activity.getContentResolver().openInputStream(uri);
	        result = BitmapFactory.decodeStream(is, null, options);
	        is.close();
	 
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    
	    return result;
	}
	
//	public Bitmap getImage(Uri uri, PhotoSetter photoSetter) {
//        Object[] object = new Object[2];
//        object[0] = (Object) uri;
//        object[1] = (Object) photoSetter;
//        new DownloadTask().execute(object); 
//    }
//private class DownloadTask extends AsyncTask<Object,Integer, Bitmap>{
//    private PhotoSetter photoSetter;
//    @Override
//    protected Bitmap doInBackground(Object...objects){
//        Uri uri = objects[0];
//        this.photoSetter = objects[1];
//        Bitmap result = null;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        InputStream is = null;
//        try {
//            is = mContext.getContentResolver().openInputStream(uri);
//            result = BitmapFactory.decodeStream(is, null, options);
//            is.close();
// 
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return result;
//    }
//    
//    protected void onPostExecute(Bitmap result){
//        photoSetter.onPhotoDownloaded(result);
//    }
    
//}
	
	//Bitmap to ByteArray
	public byte[] BitmapToByteArray(Bitmap bitmap){
//		Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), bitmap);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		
		return byteArray;
	}
	
	//String to bitmap
	public Bitmap StringToBitmap(String stringArray){
		//String to Byte Array:
		
		char[] buffer = stringArray.toCharArray();
		 byte[] b = new byte[buffer.length << 1];
		 CharBuffer cBuffer = ByteBuffer.wrap(b).asCharBuffer();
		 for(int i = 0; i < buffer.length; i++){
			 cBuffer.put(buffer[i]);
		 }
		 //Byte Array to Bitmap:
		 
		 Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
		 return bmp;
	}
	
	//Para crear miniaturas
	public Bitmap createMini(Bitmap bmp){
		byte[] imageData = null;
		Bitmap imageBitmap = bmp;
        try     
        {
            final int THUMBNAIL_SIZE = 64;
                        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            imageData = baos.toByteArray();

        }
        catch(Exception ex) {

        }
        return imageBitmap;
	}
}
