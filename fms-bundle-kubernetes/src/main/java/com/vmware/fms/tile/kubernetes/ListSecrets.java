/**
 * Copyright © 2017 VMware, Inc. All Rights Reserved.
 */
package com.vmware.fms.tile.kubernetes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vmware.fms.bundle.vrcs.common.http.TektonHttpClientImpl;
import com.vmware.fms.tile.common.TileExecutable;
import com.vmware.fms.tile.common.TileExecutableRequest;
import com.vmware.fms.tile.common.TileExecutableResponse;
import com.vmware.fms.tile.common.TileProperties;
import com.vmware.fms.tile.kubernetes.util.ResponseK8;
import service.HttpResponse;
import service.KubernetesMasterConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ListSecrets implements TileExecutable {
    private static final Logger logger = Logger.getLogger(ListSecrets.class.getName());
    public void handleExecute(TileExecutableRequest request, TileExecutableResponse response) {
        logger.info("\nEntered ListSecrets\n");
        TileProperties inputProps = request.getInputProperties().getAsProperties("kubernetesMaster");
        KubernetesMasterConfig config = new KubernetesMasterConfig(inputProps);
        String hostUrl = config.getMaster();
        String user_name = config.getUser_name();
        String password = config.getPassword();
        String namespace = request.getInputProperties().getAsString("nameSpaceVal");
        String createUrl = "api/v1/namespaces/" + namespace + "/secrets";
        String reply = null;
        String url = hostUrl+createUrl;
        //TektonHttpClientImpl tektonHttpClient = new TektonHttpClientImpl(user_name, password);
        HttpResponse client = new HttpResponse(user_name,password);
        try {
            reply = client.get(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        ResponseK8 replyJson = gson.fromJson(reply, ResponseK8.class);
        Integer number_dep =  Integer.valueOf(gson.toJson(replyJson.items.size()));
        ArrayList<String> secrets = new ArrayList<String >();
        for (Integer i= 0;i < number_dep;i++) {
            String secretName = gson.toJson(replyJson.items.get(i).metadata.name);
            logger.info(secretName);
            secretName = secretName.replace("\"", "");
            secrets.add(i,secretName);
        }
        logger.info(String.valueOf(secrets));
        response.getOutputProperties().setStringArray("listsecrets",secrets);
    }
}