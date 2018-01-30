/**
 * Copyright © 2017-2018 VMware, Inc. All Rights Reserved.
 */
package com.vmware.fms.tile.kubernetes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public class ListImages implements TileExecutable {
    private static final Logger logger = Logger.getLogger(ListImages.class.getName());

    public void handleExecute(TileExecutableRequest request, TileExecutableResponse response) {
        //All the images which are present in the pods created by the given deployment are list
        logger.info("\nEntered ListImages\n");
        TileProperties inputProps =
                request.getInputProperties().getAsProperties("kubernetesMaster");
        KubernetesMasterConfig config = new KubernetesMasterConfig(inputProps);
        String hostUrl = config.getMaster();
        String user_name = config.getUser_name();
        String password = config.getPassword();
        String name = request.getInputProperties().getAsString("depName");
        String namespace = request.getInputProperties().getAsString("nameSpaceVal");
        String createUrl =
                "apis/extensions/v1beta1/namespaces/" + namespace + "/deployments/" + name;
        String reply=null;
        String url = hostUrl + createUrl;
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
        Integer number_Img =
                Integer.valueOf(gson.toJson(replyJson.spec.template.spec.containers.size()));
        ArrayList<String> images = new ArrayList<String >();
        for (Integer i = 0;i < number_Img;i++) {
            String imageName = gson.toJson(replyJson.spec.template.spec.containers.get(i).image);
            imageName = imageName.replace("\"", "");
            images.add(i,imageName);
        }
        logger.info(String.valueOf(images));
        response.getOutputProperties().setInteger("imageNum",number_Img);
        response.getOutputProperties().setStringArray("listImages",images);
    }
}
