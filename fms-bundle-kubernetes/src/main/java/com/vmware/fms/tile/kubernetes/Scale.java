package com.vmware.fms.tile.kubernetes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vmware.fms.tile.kubernetes.util.ResponseK8;
import service.HttpResponse;
import service.KubernetesMasterConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Scale  {
    private static final Logger logger = Logger.getLogger(Scale.class.getName());

    Scale(KubernetesMasterConfig config,String url,Integer replicas) {

        String hostUrl = config.getMaster();

        String user_name = config.getUser_name();

        String password = config.getPassword();

        System.out.println(replicas);

        System.out.println("\nEnter scale point 1\n");

        System.out.println(hostUrl);


        String createUrl =url;



        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type","application/strategic-merge-patch+json");
        headers.put("accept","application/json, */*");
        headers.put("user-agent","kubectl/v1.5.3 (linux/amd64) kubernetes/029c3a4");


        String reply = null;
        url = hostUrl+createUrl;
        String partialPayload= "";

        System.out.println(url);
        HttpResponse client = new HttpResponse(user_name,password);
        try {
            reply = client.get(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(reply);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        ResponseK8 replyJson = gson.fromJson(reply, ResponseK8.class);


        replyJson.spec.replicas = replicas;

        System.out.println("----Now__");

        System.out.println(replyJson);

        reply = gson.toJson(replyJson);
        System.out.println("----Now__ 1");
        System.out.println(reply);
        System.out.println("----Now___-over");

        try {
            reply = client.put(url,headers,reply,"application/json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nEnter scale point end\n");
    }
}
