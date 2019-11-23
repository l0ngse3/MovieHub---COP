package com.example.myapplication.Service;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class UploadTask extends AsyncTask<String, Integer, String> {

    String urlString, idFilm, upload_file_name, uri_video;
    public TaskDone taskDone;

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        taskDone.done(s);
    }

    public UploadTask(String urlString, String idFilm, String upload_file_name, String uri_video, TaskDone taskDone) {
        this.urlString = urlString;
        this.idFilm = idFilm;
        this.upload_file_name = upload_file_name;
        this.uri_video = uri_video;
        this.taskDone = taskDone;
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = null;
        if (!isCancelled() && urls != null && urls.length > 0) {
            String urlString = urls[0];
            try {
                String resultString = post(urlString, idFilm, upload_file_name, uri_video);

                if (resultString != null) {
                    result = resultString;
                } else {
                    throw new IOException("No response received.");
                }
            } catch (Exception e) {
                result = e.toString();
            }
        }
        return result;
    }

    private String post(String urlString, String idFilm, String upload_file_name, String uri_video) throws IOException {
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String existingFileName = uri_video;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(existingFileName));
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + upload_file_name + "\"" + lineEnd); // uploaded_file_name is the Name of the File to be uploaded

            dos.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        } catch (IOException ioe) {
            Log.e("Debug", "error: " + ioe.getMessage(), ioe);
        }
        //------------------ read the SERVER RESPONSE
        try {
            inStream = new DataInputStream(conn.getInputStream());
            String str;
            while ((str = inStream.readUTF()) != null) {
                Log.e("Debug", "Server Response " + str);
                taskDone.done(str);
            }
            inStream.close();
        } catch (IOException ioex) {
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }
        return null;
    }

}

