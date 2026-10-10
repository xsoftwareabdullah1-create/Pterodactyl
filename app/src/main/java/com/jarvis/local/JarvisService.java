package com.xsoftwareabdullah.jarvis;
import android.app.Service; 
import android.content.Intent; 
import android.os.IBinder;
public class JarvisService extends Service {
    @Override public int onStartCommand(Intent i, int f, int id) { return START_STICKY; }
    @Override public IBinder onBind(Intent i) { return null; }
}
