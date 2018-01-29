package service;
/*
 * Copyright (c) 2018 VMware, Inc. All Rights Reserved.
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class HttpResponse {
    private String user;
    private String pass;
    private CloseableHttpClient httpclient;

    public HttpResponse(String username, String password) {
        this.user = username;
        this.pass = password;
    }
    // Get call uses this method
    public String get(String url) throws IOException {
     String responseString;
     try {
         CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
         credentialsProvider.setCredentials(AuthScope.ANY,
                 new UsernamePasswordCredentials(user, pass));
         this.httpclient =
                 HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
         HttpGet httpget = new HttpGet(url);

         System.out.println("Executing request " + httpget.getRequestLine());
         CloseableHttpResponse response = httpclient.execute(httpget);
         try {
             responseString = EntityUtils.toString(response.getEntity());
         }finally {
             response.close();
         }
     } finally {
            httpclient.close();
     }
     return responseString;
    }

    public String delete(String url) throws IOException {
        String responseString=null;
        try
        {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(user, pass));
            this.httpclient =
                    HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
            HttpDelete httpdelete = new HttpDelete(url);

            System.out.println("Executing request " + httpdelete.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpdelete);
            try {
                responseString = EntityUtils.toString(response.getEntity());
            }finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }

        return responseString;
    }

    public String post(String url,String contents, String payloadType) throws IOException {
        String responseString=null;
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(user, pass));
        this.httpclient =
                HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = null;
        try {
            entity = new StringEntity(contents);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", payloadType);
        httpPost.setHeader("Content-type", payloadType);

        CloseableHttpResponse response = httpclient.execute(httpPost);
        responseString = response.toString();
        try {
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString;
    }

    public String put(String url, Map<String, String> headers, String contents, String payloadType) throws IOException {
        String responseString=null;
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(user, pass));
        this.httpclient =
                HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        HttpPut httpPut = new HttpPut(url);
        StringEntity entity = null;
        try {
            entity = new StringEntity(contents);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPut.setEntity(entity);
        httpPut.setHeader("Accept", payloadType);
        httpPut.setHeader("Content-type", payloadType);

        CloseableHttpResponse response = httpclient.execute(httpPut);
        responseString = response.toString();
        try {
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpclient.close();

        return responseString;
    }


    public String patch(String url, String payload, String payloadType) throws IOException {
        String responseString=null;
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(user, pass));
        this.httpclient =
                HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        this.httpclient =
                HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        HttpPatch httpPatch = new HttpPatch(url);
        StringEntity entity = null;
        try {
            entity = new StringEntity(payload);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPatch.setEntity(entity);
        httpPatch.setHeader("Accept", payloadType);
        httpPatch.setHeader("Content-type", "application/strategic-merge-patch+json");

        CloseableHttpResponse response = httpclient.execute(httpPatch);
        responseString = response.toString();
        try {
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpclient.close();

        return responseString;
    }
}
