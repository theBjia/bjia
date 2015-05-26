package com.sunny.net;

import android.content.Context;
import android.os.Build;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class DHttpClient {

    private final int CONNECTION_TIMEOUT = 20000;
    private final int CON_TIME_OUT_MS = 20000;

    private final String BOUNDARY = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";

    private static String read(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    private static String encodePostBody(
            LinkedHashMap<String, String> parameters, String boundary) {
        if (parameters == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (String key : parameters.keySet()) {
            Object parameter = parameters.get(key);
            if ((!(parameter instanceof String))
                // || (TextUtils.isEmpty((String) parameter))
                    ) {
                continue;
            }
            sb.append("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n\r\n" + (String) parameter);
            sb.append("\r\n" + "--" + boundary + "\r\n");
        }
        return sb.toString();
    }

    public void get() {
    }

    public void post() {
    }

    public String httpPostWithFile(Context context, String url,
                                   LinkedHashMap<String, String> params, HashMap<String, File> files)
            throws MalformedURLException, IOException {
        final String endLine = "\r\n";
        OutputStream os;
        java.net.Proxy p = ProxyUtil.getProxy(context);
        HttpURLConnection conn = (HttpURLConnection) (p == null ? new URL(url)
                .openConnection() : new URL(url).openConnection(p));
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="
                + BOUNDARY);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            conn.setRequestProperty("Connection", "close");
        }
        // conn.setRequestProperty("Cache-Control", "no-cache");
        conn.connect();
        os = new BufferedOutputStream(conn.getOutputStream());
        os.write(("--" + BOUNDARY + endLine).getBytes());
        String content = (encodePostBody(params, BOUNDARY));
        os.write(content.getBytes());
        // write file
        Set<String> keys = files.keySet();
        File file = null;
        for (String key : keys) {
            file = files.get(key);
            if (file != null && file.exists()) {
                os.write((endLine + "--" + BOUNDARY + endLine).getBytes());
                FileInputStream fis = null;
                try {
                    os.write(("Content-Disposition: form-data; name=\"" + key
                            + "\"; filename=\"" + file.getName() + "\"" + endLine)
                            .getBytes());
                    os.write(("Content-Type: image/jpeg" + endLine + endLine)
                            .getBytes());
                    fis = new FileInputStream(file);
                    byte[] buffer = new byte[1024 * 50];
                    int flag = -1;
                    while ((flag = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, flag);
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    if (fis != null) {
                        fis.close();
                    }
                    throw e;
                }
            }
        }
        os.write((endLine + "--" + BOUNDARY + endLine).getBytes());
        os.flush();
        String response = "";
        try {
            response = read(conn.getInputStream());
            final int responseCode = conn.getResponseCode();
        } catch (FileNotFoundException e) {
            // Error Stream contains JSON that we can parse to a FB error
            response = read(conn.getErrorStream());
        }
        return response;
    }

    public void downloadInFile(String urlString, File file, Context context)
            throws IOException {
        /**
         * Workaround for bug pre-Froyo, see here for more info: http://android
         * -developers.blogspot.com/2011/09/androids-http-clients.html
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
        HttpURLConnection urlConnection = null;
        FileOutputStream out = null;
        File cacheFile = file;
        if (!cacheFile.exists()) {
            cacheFile.getParentFile().mkdirs();
        }
        URL url;
        InputStream is = null;
        try {
            url = new URL(urlString);
            java.net.Proxy proxy = ProxyUtil.getProxy(context);
            urlConnection = (proxy != null ? (HttpURLConnection) url
                    .openConnection(proxy) : (HttpURLConnection) url
                    .openConnection());
            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(20000);
            is = urlConnection.getInputStream();
            out = new FileOutputStream(cacheFile);
            byte[] buffer = new byte[8 * 1024];
            int b = -1;
            while ((b = is.read(buffer)) != -1) {
                out.write(buffer, 0, b);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        cacheFile.setLastModified(System.currentTimeMillis());
    }

    public InputStream downloadInMemory(String urlString, Context context)
            throws IOException {
        /**
         * Workaround for bug pre-Froyo, see here for more info: http://android
         * -developers.blogspot.com/2011/09/androids-http-clients.html
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
        HttpURLConnection urlConnection = null;
        URL url;
        InputStream is = null;
        try {
            url = new URL(urlString);
            java.net.Proxy proxy = ProxyUtil.getProxy(context);
            urlConnection = (proxy != null ? (HttpURLConnection) url
                    .openConnection(proxy) : (HttpURLConnection) url
                    .openConnection());
            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(20000);
            is = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return is;
    }

    public static class Brake {
        private volatile boolean stop = false;

        public synchronized void stop() {
            stop = true;
        }

        public boolean hasStop() {
            return stop;
        }
    }


}
