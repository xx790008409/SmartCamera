package com.example.a79000.smartcamera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SavePicture {
    private static File mPhotoFile = null;
    public static void setPhotoFile(File photoFile){
        mPhotoFile = photoFile;
    }

    public static File getPhotoFile(){

        return mPhotoFile;
    }

    /*
    save the image to gallery
     */
    public static void saveImageToGallery(Bitmap bmp,String bitName ) {
        File appDir = new File(Environment.getExternalStorageDirectory(),"SmartCamera");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        String fileName = bitName + ".jpg";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setPhotoFile(file);
    }

    /**
     * param:
     * bmp:your bmp file
     * picName:you can decide your file name by yourself
     */
    public static boolean saveBmp2Gallery(Context context, Bitmap bmp, String picName) {
        //saveImageToGallery(bmp,picName);
        String fileName = null;
        //dir
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;

        File file = null;
        // output stream
        FileOutputStream outStream = null;
        try {
            file = new File(galleryPath, picName + ".jpg");
            fileName = file.toString();
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
            }
        }catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //broadcast
        //MediaStore.Images.Media.insertImage(context.getContentResolver(),bmp,fileName,null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        return true;
    }

}
