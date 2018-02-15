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
import com.google.gson.GsonBuilder;
import com.vmware.fms.tile.common.TileExecutable;
import com.vmware.fms.tile.common.TileExecutableRequest;
import com.vmware.fms.tile.common.TileExecutableResponse;
import com.vmware.fms.tile.common.TileProperties;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.models.*;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import service.ConvertToJson;
import service.IsJson;
import service.KubernetesMasterConfig;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;

public class Start implements TileExecutable {

    private static final Logger logger = Logger.getLogger(Start.class.getName());
    @Override
    public void handleExecute(TileExecutableRequest request, TileExecutableResponse response){
        TileProperties inputProps = request.getInputProperties().getAsProperties("kubernetesMaster");
        KubernetesMasterConfig config = new KubernetesMasterConfig(inputProps);
        String kubeconf = config.getKubeconfig();
        Reader targetKubernetes  = new StringReader(kubeconf);
        KubeConfig kubeconfig = KubeConfig.loadKubeConfig(targetKubernetes);
        ApiClient client = Config.fromConfig(kubeconfig);
        Configuration.setDefaultApiClient(client);

        Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();

        String jobType = request.getInputProperties().getAsString("jobName");
        String jobUrl ;
        String reply = new String();

        if(jobType.equals("createPod")){
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            CoreV1Api api = new CoreV1Api();

            V1Pod body = gson.fromJson(contents,V1Pod.class);
            try {
                V1Pod value = (api.createNamespacedPod(nameSpace,body,null));
                reply = String.valueOf(value.getStatus());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(jobType.equals("createPv")){

            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            CoreV1Api api = new CoreV1Api();
            V1PersistentVolume body = gson.fromJson(contents,V1PersistentVolume.class);

            try {
                reply = String.valueOf(api.createPersistentVolume(body,null));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(jobType.equals("createPvc")){
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))) {
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            CoreV1Api api = new CoreV1Api();
            V1PersistentVolumeClaim body = gson.fromJson(contents,V1PersistentVolumeClaim.class);

            try {
                reply = String.valueOf(api.createNamespacedPersistentVolumeClaim(nameSpace,body,null));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(jobType.equals("createDp")){

            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");


            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            ExtensionsV1beta1Api api = new ExtensionsV1beta1Api();
            ExtensionsV1beta1Deployment body = gson.fromJson(contents,ExtensionsV1beta1Deployment.class);
            try {
                reply  = String.valueOf(api.createNamespacedDeployment(nameSpace,body,null));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (jobType.equals("createRc")) {
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            CoreV1Api api = new CoreV1Api();
            V1ReplicationController body = gson.fromJson(contents,V1ReplicationController.class);

            try {
                reply = String.valueOf(api.createNamespacedReplicationController(nameSpace,body,null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (jobType.equals("createSecrets")) {
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            CoreV1Api api = new CoreV1Api();
            V1Secret body = gson.fromJson(contents,V1Secret.class);
            try {
                reply = String.valueOf(api.createNamespacedSecret(nameSpace,body,null));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (jobType.equals("createRs")) {

            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");


            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            ExtensionsV1beta1Api api = new ExtensionsV1beta1Api();
            V1beta1ReplicaSet body = gson.fromJson(contents,V1beta1ReplicaSet.class);


            try {
                reply = String.valueOf(api.createNamespacedReplicaSet(nameSpace,body,null));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        else if (jobType.equals("createNs")){
            String name  = request.getInputProperties().getAsString("nameSpaceVal");
            String contents = "{\"kind\":\"Namespace\",\"apiVersion\":\"v1\",\"metadata\":{\"name\":\""+name+"\",\"creationTimestamp\":null},\"spec\":{},\"status\":{}}\n";
            CoreV1Api api = new CoreV1Api();
            V1Namespace body  = gson.fromJson(contents,V1Namespace.class);
            try {
                reply = String.valueOf(api.createNamespace(body,null));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(jobType.equals("createSvc")){

            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            CoreV1Api api = new CoreV1Api();
            V1Service body = gson.fromJson(contents,V1Service.class);
            try {
                reply = String.valueOf(api.createNamespacedService(nameSpace,body,null));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(jobType.equals("deletePod")){

            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String podName = request.getInputProperties().getAsString("deleteParam");
            CoreV1Api api = new CoreV1Api();
            V1DeleteOptions options = new V1DeleteOptions();
            options.setApiVersion("v1");
            options.setKind("DeleteOptions");
            try {
                api.deleteNamespacedPod(podName,nameSpace,options,null,5,null,null);
            } catch (Exception e) {
                reply  = "{\n" +
                        "  \"Response\" : \"No Response code for deletion\"\n" +
                        "}";
            }

        }
        else if(jobType.equals("deleteDp")){
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String depName = request.getInputProperties().getAsString("deleteParam");
            ExtensionsV1beta1Api api = new ExtensionsV1beta1Api();
            V1DeleteOptions options = new V1DeleteOptions();
            options.setApiVersion("extensions/v1beta1");
            options.setKind("DeleteOptions");
            try {
                api.deleteNamespacedDeployment(depName,nameSpace,options,null,05,null,null);
            } catch (Exception e) {
                reply = "{\n" +
                        "  \"Response\" : \"No Response code for deletion\"\n" +
                        "}";
            }

        }
        else if(jobType.equals("deleteRc")){
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String rcName = request.getInputProperties().getAsString("deleteParam");
            CoreV1Api api = new CoreV1Api();
            V1DeleteOptions options = new V1DeleteOptions();
            options.setApiVersion("v1");
            options.setKind("DeleteOptions");
            try {
                api.deleteNamespacedReplicationController(rcName,nameSpace,options,null,5,null,null);
            } catch (Exception e) {
                reply  = "{\n" +
                        "  \"Response\" : \"No Response code for deletion\"\n" +
                        "}";
            }

        }
        else if(jobType.equals("deleteSecrets")){

            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String secretName = request.getInputProperties().getAsString("deleteParam");
            CoreV1Api api = new CoreV1Api();
            V1DeleteOptions options = new V1DeleteOptions();
            options.setApiVersion("v1");
            options.setKind("DeleteOptions");
            try {
                api.deleteNamespacedSecret(secretName,nameSpace,options,null,5,null,null);
            } catch (Exception e) {
                reply  = "{\n" +
                        "  \"Response\" : \"No Response code for deletion\"\n" +
                        "}";
            }
        }
        else if(jobType.equals("deleteRs")){
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String rsName = request.getInputProperties().getAsString("deleteParam");
            ExtensionsV1beta1Api api = new ExtensionsV1beta1Api();
            V1DeleteOptions options = new V1DeleteOptions();
            options.setApiVersion("extensions/v1beta1");
            options.setKind("DeleteOptions");
            try {
                api.deleteNamespacedReplicaSet(rsName,nameSpace,options,null,05,null,null);
            } catch (Exception e) {
                reply = "{\n" +
                        "  \"Response\" : \"No Response code for deletion\"\n" +
                        "}";
            }


        }
        else if(jobType.equals("deleteNs")){
            String nsName = request.getInputProperties().getAsString("deleteParam");
            CoreV1Api api = new CoreV1Api();
            V1DeleteOptions options = new V1DeleteOptions();
            options.setApiVersion("v1");
            options.setKind("DeleteOptions");
            try {
                api.deleteNamespace(nsName,options,null,5,null,null);
            } catch (Exception e) {
                reply  = "{\n" +
                        "  \"Response\" : \"No Response code for deletion\"\n" +
                        "}";
            }

        }else if(jobType.equals("deleteSvc")){
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String svcName = request.getInputProperties().getAsString("deleteParam");
            CoreV1Api api = new CoreV1Api();
            try {
                api.deleteNamespacedService(svcName,nameSpace,null);
            } catch (Exception e) {
                reply  = "{\n" +
                        "  \"Response\" : \"No Response code for deletion\"\n" +
                        "}";
            }

        }else if(jobType.equals("deletePvc")){
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String pvcName = request.getInputProperties().getAsString("deleteParam");
            CoreV1Api api = new CoreV1Api();
            V1DeleteOptions options = new V1DeleteOptions();
            options.setApiVersion("v1");
            options.setKind("DeleteOptions");
            try {
                api.deleteNamespacedPersistentVolumeClaim(pvcName,nameSpace,options,null,5,null,null);
            } catch (Exception e) {
                reply  = "{\n" +
                        "  \"Response\" : \"No Response code for deletion\"\n" +
                        "}";
            }

        }else if(jobType.equals("deletePv")){
            String podName = request.getInputProperties().getAsString("deleteParam");
            CoreV1Api api = new CoreV1Api();
            V1DeleteOptions options = new V1DeleteOptions();
            options.setApiVersion("v1");
            options.setKind("DeleteOptions");
            try {
                api.deletePersistentVolume(podName,options,null,5,null,null);
            } catch (Exception e) {
                reply  = "{\n" +
                        "  \"Response\" : \"No Response code for deletion\"\n" +
                        "}";
            }

        }else if(jobType.equals("updateImg")) {
            String depName = request.getInputProperties().getAsString("depName");
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            List<String> listChecked= request.getInputProperties().getAsStringArray("imagesName");
            List<String> imageVer= request.getInputProperties().getAsStringArray("imagesReqVer");
            UpdateImg updating = new UpdateImg(config,depName,listChecked,imageVer,nameSpace,reply);
        }
        // TODO: Scaling of replication controller and deployment
        /*else if(jobType.equals("scaleDp")){
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String name = request.getInputProperties().getAsString("depName");
            Integer replicaCount = request.getInputProperties().getAsInteger("replicas");
            String url = "apis/extensions/v1beta1/namespaces"+nameSpace+"/deployments/"+name;
            Scale scaling = new Scale(config,url,replicaCount);
        }else if(jobType.equals("scaleRc")){
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String rcname = request.getInputProperties().getAsString("depName");
            Integer replicaCount = request.getInputProperties().getAsInteger("replicas");
            String url = "api/v1/namespaces/"+nameSpace+"/replicationcontrollers/"+rcname;
            Scale scaling = new Scale(config,url,replicaCount);
        }*/
        logger.info(reply);
        response.getOutputProperties().setString("response",reply);
        response.setCompleted(true);
    }
}