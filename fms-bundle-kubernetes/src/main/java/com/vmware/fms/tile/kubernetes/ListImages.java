/*
Copyright (c) 2017-2018 VMware, Inc. All Rights Reserved.
 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.vmware.fms.tile.kubernetes;
import com.vmware.fms.tile.common.TileExecutable;
import com.vmware.fms.tile.common.TileExecutableRequest;
import com.vmware.fms.tile.common.TileExecutableResponse;
import com.vmware.fms.tile.common.TileProperties;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.models.ExtensionsV1beta1Deployment;
import io.kubernetes.client.models.V1Container;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import service.KubernetesMasterConfig;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ListImages implements TileExecutable {
    private static final Logger logger = Logger.getLogger(ListImages.class.getName());

    public void handleExecute(TileExecutableRequest request, TileExecutableResponse response) {
        //All the images which are present in the pods created by the given deployment are list
        logger.info("\nEntered ListImages\n");
        TileProperties inputProps = request.getInputProperties().getAsProperties("kubernetesMaster");
        KubernetesMasterConfig config = new KubernetesMasterConfig(inputProps);

        String name = request.getInputProperties().getAsString("depName");
        String namespace = request.getInputProperties().getAsString("nameSpaceVal");
        String reply=null;
        String kubeconf = config.getKubeconfig();
        Reader targetKubernetes  = new StringReader(kubeconf);
        KubeConfig kubeconfig = KubeConfig.loadKubeConfig(targetKubernetes);
        ApiClient client = Config.fromConfig(kubeconfig);
        Configuration.setDefaultApiClient(client);
        ExtensionsV1beta1Api api = new ExtensionsV1beta1Api();
        ExtensionsV1beta1Deployment Deployment =null;
        try {
            Deployment = api.readNamespacedDeployment(name,namespace,null,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<V1Container> list = Deployment.getSpec().getTemplate().getSpec().getContainers();
        ArrayList<String> images = new ArrayList<String >();


        for (V1Container item : list){
            images.add(item.getImage());
        }

        Integer number_Img = list.size();
        logger.info(String.valueOf(images));
        response.getOutputProperties().setInteger("imageNum",number_Img);
        response.getOutputProperties().setStringArray("listImages",images);
    }
}
