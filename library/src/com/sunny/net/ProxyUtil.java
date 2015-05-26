package com.sunny.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;

import java.net.InetSocketAddress;


public class ProxyUtil {

    /* 2014-04-03 Kang, Leo 字符串修改为字符串常量 */
    private static final String HTTP_PROXY_HOST = "http.proxyHost";
    private static final String HTTP_PROXY_PORT = "http.proxyPort";

    /**
     * Set APN for HTTP Client. Use old way to get host and port.
     *
     * @param httpClient
     * @param context
     */
    public static void setProxyHttpHost(HttpClient httpClient, Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        HttpHost proxyhost = null;
        if (networkInfo != null
                && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

            if (proxyhost == null) {
                final String port = System.getProperty(HTTP_PROXY_PORT);
                int p = -1;
                String host;
                if ((!TextUtils.isEmpty(port)) && TextUtils.isDigitsOnly(port)) {
                    p = Integer.valueOf(port);
                }
                // GPRS: APN http proxy
                host = System.getProperty(HTTP_PROXY_HOST);
                if (!TextUtils.isEmpty(host) && p > 0) {
                    proxyhost = new HttpHost(host, p);
                }
            }
            if (proxyhost != null) {
                httpClient.getParams().setParameter(
                        ConnRoutePNames.DEFAULT_PROXY, proxyhost);
            }
        }
    }

    public static java.net.Proxy getProxy(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        java.net.Proxy result = null;
        if (networkInfo != null
                && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

            if (result == null) {
                final String port = System.getProperty(HTTP_PROXY_PORT);
                int p = -1;
                String host;
                if ((!TextUtils.isEmpty(port)) && TextUtils.isDigitsOnly(port)) {
                    p = Integer.valueOf(port);
                }
                // GPRS: APN http proxy
                host = System.getProperty(HTTP_PROXY_HOST);
                if (!TextUtils.isEmpty(host) && p > 0) {
                    result = new java.net.Proxy(java.net.Proxy.Type.HTTP,
                            new InetSocketAddress(host, p));
                }
            }
        }
        return result;
    }

}