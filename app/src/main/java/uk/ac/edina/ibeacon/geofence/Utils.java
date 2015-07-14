package uk.ac.edina.ibeacon.geofence;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by murray on 06/09/14.
 */
public class Utils {

    private File offlineMap;

    public File getOfflineMap(){
        if (offlineMap==null){
            //alert wait till db created
            throw new RuntimeException("offline db not created yet");
        }
        return  offlineMap;
    }

    private static Utils instance = new Utils();

    public Utils(){}

    public static Utils getInstance(){
        return instance;
    }

    public void copyOfflineMap(String filename, AssetManager assetManager, String packageName){
        offlineMap = copyFileFromAssets(filename, assetManager, packageName);
    }

    public File copyFileFromAssets(String filename, AssetManager assetManager, String packageName) {

        String baseDir = Environment.getExternalStorageDirectory().getPath() + "/" + packageName;


        File baseDirectory = new File(baseDir);
        if(!baseDirectory.exists()){
            baseDirectory.mkdirs();

        }

        File dbFile = new File(baseDirectory, filename);

        if(dbFile.exists()){
           return dbFile;
        }



        InputStream in;
        OutputStream out;

        try {
            Log.i("tag", "copyFile() " + filename);
            in = assetManager.open(filename);

            out = new FileOutputStream(dbFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e("tag", "Exception in copyFile() of "+dbFile);
            Log.e("tag", "Exception in copyFile() "+e.toString());
        }

        return dbFile;

    }
}
