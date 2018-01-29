package com.vmware.fms.tile.kubernetes;

/**
 * Copyright © 2017-2018 VMware, Inc. All Rights Reserved.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vmware.fms.tile.common.TileExecutable;
import com.vmware.fms.tile.common.TileExecutableRequest;
import com.vmware.fms.tile.common.TileExecutableResponse;
import com.vmware.fms.tile.common.TileProperties;
import com.vmware.fms.tile.kubernetes.util.ResponseK8;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import service.HttpResponse;
import service.KubernetesMasterConfig;


public class ListDeployment implements TileExecutable {
    private static final Logger logger = Logger.getLogger(ListDeployment.class.getName());
    //This function is used to list all the deployments in the given namespace, implemented with rest api calls

    public void handleExecute(TileExecutableRequest request, TileExecutableResponse response) {
        logger.info("\nEntered ListDeployment\n");
        TileProperties inputProps = request.getInputProperties()
                .getAsProperties("kubernetesMaster");
        KubernetesMasterConfig config = new KubernetesMasterConfig(inputProps);
        String hostUrl = config.getMaster();
        String user_name = config.getUser_name();
        String password = config.getPassword();
        String namespace = request.getInputProperties().getAsString("nameSpaceVal");
        String createUrl = "apis/extensions/v1beta1/namespaces/" + namespace + "/deployments";
        String reply = null;
        String url = hostUrl + createUrl;
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
        ArrayList<String> deployment = new ArrayList<String >();
        for (Integer i = 0; i < number_dep; i++) {
            String depName = gson.toJson(replyJson.items.get(i).metadata.name);
            depName = depName.replace("\"", "");
            deployment.add(i,depName);
        }
        logger.info(String.valueOf(deployment));
        response.getOutputProperties().setStringArray("listDeployment",deployment);
    }
}