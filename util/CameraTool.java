package com.example.hp.activitytest.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class CameraTool {

    public static Uri takePhoto(Activity activity, int TAKE_PHOTO){
        Uri imageUri;
        File outputImage = new File(activity.getExternalCacheDir(),"output_image.jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();

        }catch(IOException e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT>=24){
            imageUri = FileProvider.getUriForFile(activity,"com.example.hp.activitytest.fileprovider",outputImage);
        }else{
            imageUri = Uri.fromFile(outputImage);
        }

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        activity.startActivityForResult(intent,TAKE_PHOTO);
        return imageUri;
    }
}
