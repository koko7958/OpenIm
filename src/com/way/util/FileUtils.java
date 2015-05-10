package com.way.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class FileUtils   
{  
	private static final String TAG = "FileUtils";

	
	//д���ݵ�SD�е��ļ�
	public static void writeFileSdcardFile(String fileName,String write_str){
		try{
			FileOutputStream fout = new FileOutputStream(fileName);
			byte [] bytes = write_str.getBytes();
			fout.write(bytes);
			fout.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//��SD�е��ļ�
	public static String readFileSdcardFile(String fileName){
		String res="";
		try{
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte [] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}


	public static void storeImageToSdcard(Bitmap bitmap, String ImageName, String path) {
	  File file = new File(path);
	  if (!file.exists()) {
		  file.mkdir();
	  }
	  File imagefile = new File(file, ImageName/* + ".jpg"*/);
	  if (!imagefile.exists()) {
		try {
			imagefile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imagefile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
	   }
	  } else {
		  Log.d(TAG, "imagefile already exist");
	  }
	 }


    public static Bitmap readBitMap(Context context, int resId){  
        BitmapFactory.Options opt = new BitmapFactory.Options();  
        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
       opt.inPurgeable = true;  
       opt.inInputShareable = true;  
          //��ȡ��ԴͼƬ  
       InputStream is = context.getResources().openRawResource(resId);  
           return BitmapFactory.decodeStream(is,null,opt);  
    }

}  