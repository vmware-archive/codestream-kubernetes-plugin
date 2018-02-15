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
package com.vmware.fms.tile.kubernetes;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

public class UpdateImg  {
    public static Object deserialize(String jsonStr, Class<?> targetClass) {
        Object obj = (new Gson()).fromJson(jsonStr, targetClass);
        return obj;
    }
    private static final Logger logger = Logger.getLogger(UpdateImg.class.getName());

    UpdateImg(KubernetesMasterConfig config, String depName, List<String> listChecked, List<String> imageVer, String nameSpace,String reply) {
        logger.info("\nEntered UpdateImg\n");
        String kubeconf = config.getKubeconfig();
        Reader targetKubernetes = new StringReader(kubeconf);
        KubeConfig kubeconfig = KubeConfig.loadKubeConfig(targetKubernetes);
        ApiClient client = Config.fromConfig(kubeconfig);
        Configuration.setDefaultApiClient(client);
        ExtensionsV1beta1Api api = new ExtensionsV1beta1Api();
        ExtensionsV1beta1Deployment body = new ExtensionsV1beta1Deployment();
        try {
            body = api.readNamespacedDeployment("nginx-deployment", "default", null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<V1Container> list = body.getSpec().getTemplate().getSpec().getContainers();
        for (Integer i = 0; i < listChecked.size(); i++) {
            String value = listChecked.get(i);
            String[] str_array = value.split(":");
            String image = str_array[0];
            String ver = imageVer.get(i);
            String newVal = image + ":" + ver;
            for (V1Container iter : list) {
                String presentVal = iter.getImage();
                if (presentVal.matches(value)) {
                    body.getSpec().getTemplate().getSpec().getContainers().get(list.indexOf(iter)).setImage(newVal);
                    String payload = "{\"op\":\"replace\",\"path\":\"/spec/template/spec/containers/" + list.indexOf(iter) + "/image\",\"value\":\"" + newVal + "\"}";
                    ArrayList<JsonObject> arrayval = new ArrayList<>();
                    arrayval.add(((JsonElement) deserialize(payload, JsonElement.class)).getAsJsonObject());

                    //list.indexOf("")
                    try {
                        reply = reply + String.valueOf(api.patchNamespacedDeployment("nginx-deployment", "default", arrayval, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println(reply);
                    continue;
                }
            }

            logger.info(reply);
        }
    }
}
