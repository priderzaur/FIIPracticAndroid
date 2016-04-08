package com.example.riordachioaia.fiips1.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;

import com.example.riordachioaia.fiips1.ContactsActivity;
import com.example.riordachioaia.fiips1.persistance.Contact;
import com.example.riordachioaia.fiips1.persistance.DatabaseHelperFIIPRacticAndroid;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Monica on 02.04.2016.
 */
public class ImportService extends IntentService {

    public ImportService() {
        super("ImportService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("fjh", "url ");
        String urlString = intent.getStringExtra("url");
        URL url = null;
        String email = intent.getStringExtra("email");
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        String returnString  = readBuffer (in,200);

        String [] lines = returnString.split("\n");
        Pattern p = Pattern.compile("(.*) (.*) (.*)");

            DatabaseHelperFIIPRacticAndroid contactHelper = DatabaseHelperFIIPRacticAndroid.getInstance(this.getApplicationContext());

            int i = 1;
        for (String line : lines)
        {
            Matcher m = p.matcher(line);
            if (m.matches()){
                contactHelper.addContact(new Contact(m.group(1),m.group(2),m.group(3), "None"), email);
                notifyProgress(i, lines.length);
                Thread.sleep(1000);
                i++;
                Log.d("log1",m.group(1));
                Log.d("log2",m.group(2));
                Log.d("log3",m.group(3));
            }
        }
            sendBroadcast(new Intent(ContactsActivity.ACTION_REFRESH));
    }
        catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        urlConnection.disconnect();
    }

    public void notifyProgress(int i, int n){
        Notification notification  = new Notification.Builder(this)
                .setContentTitle("Download")
                .setContentText(i + " din " + n)
                .setSmallIcon(android.R.drawable.bottom_bar).build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);
    }

    public static String readBuffer(final InputStream is, final int bufferSize) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try  {
            Reader in = new InputStreamReader(is, "UTF-8");
            for (;;) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
        }
        catch (UnsupportedEncodingException ex) {
   /* ... */
        }
        catch (IOException ex) {
   /* ... */
        }
        return out.toString();
    }


}
