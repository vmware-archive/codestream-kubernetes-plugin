package com.vmware.fms.tile.kubernetes;

/**
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

import com.vmware.fms.tile.common.TileExecutable;
import com.vmware.fms.tile.common.TileExecutableRequest;
import com.vmware.fms.tile.common.TileExecutableResponse;
import com.vmware.fms.tile.common.TileProperties;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Logger;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.models.ExtensionsV1beta1Deployment;
import io.kubernetes.client.models.ExtensionsV1beta1DeploymentList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import service.KubernetesMasterConfig;


public class ListDeployment implements TileExecutable {
    private static final Logger logger = Logger.getLogger(ListDeployment.class.getName());
    //This function is used to list all the deployments in the given namespace, implemented with rest api calls

    public void handleExecute(TileExecutableRequest request, TileExecutableResponse response) {
        logger.info("\nEntered ListDeployment\n");
        TileProperties inputProps = request.getInputProperties()
                .getAsProperties("kubernetesMaster");
        KubernetesMasterConfig config = new KubernetesMasterConfig(inputProps);

        String namespace = request.getInputProperties().getAsString("nameSpaceVal");
        String kubeconf = config.getKubeconfig();
        Reader targetKubernetes  = new StringReader(kubeconf);
        KubeConfig kubeconfig = KubeConfig.loadKubeConfig(targetKubernetes);
        ApiClient client = Config.fromConfig(kubeconfig);
        Configuration.setDefaultApiClient(client);
        ExtensionsV1beta1Api api = new ExtensionsV1beta1Api();
        ExtensionsV1beta1DeploymentList list = null;
        try {
            list = api.listNamespacedDeployment(namespace,null,null,null,null,20,null);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        ArrayList<String> deployments = new ArrayList<String >();

        for (ExtensionsV1beta1Deployment item : list.getItems()) {
            deployments.add(item.getMetadata().getName());

        }

        logger.info(String.valueOf(deployments));
        response.getOutputProperties().setStringArray("listDeployment",deployments);
    }
}
