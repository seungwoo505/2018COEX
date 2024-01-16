package com.example.cpr_pro.crp_project1;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by YUM on 2015-06-14.
 */
public class WebServerSender {
    String url;
    HttpClient http;
    ArrayList<NameValuePair> nameValuePairs;
    HttpPost httpPost;
    HttpGet httpGet;
    String mode;
    HttpEntity resEntity;
    public WebServerSender(String serverDomain, String phpDocs){
        this(serverDomain,phpDocs,"POST");
    }
    public void setHeader(String key, String value){
        httpPost.addHeader(key, value);
    }
    public void clearArg(){
        nameValuePairs.clear();
    }
    public WebServerSender(String serverDomain, String phpDocs, String mode){

        url = serverDomain+"/"+phpDocs;
        this.mode=mode;
        http = new DefaultHttpClient();
        nameValuePairs = new ArrayList<NameValuePair>();
        if(mode.equals("POST"))
            httpPost = new HttpPost(url);
        else if(mode.equals("GET"))
            httpGet = new HttpGet(url);
        //httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
    }

    public void add(String key, String value) {
        nameValuePairs.add(new BasicNameValuePair(key, value));
    }
    public void changeURL(String url, String docs){
        url=url+"/"+docs;
        if(mode.equals("POST"))
            httpPost = new HttpPost(url);
        else if(mode.equals("GET"))
            httpGet = new HttpGet(url);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        clearArg();

    }
    public void send() {

        HttpParams params = http.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        UrlEncodedFormEntity entityRequest = null;
        try {
            entityRequest = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(mode.equals("POST"))
            httpPost.setEntity(entityRequest);
        HttpResponse responsePost = null;
        try {
            if(mode.equals("POST"))
                responsePost = http.execute(httpPost);
            else if(mode.equals("GET")){
                responsePost = http.execute(httpGet);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        resEntity = responsePost.getEntity();
    }

    public String receiveResult() {

        try {
            return EntityUtils.toString(resEntity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
