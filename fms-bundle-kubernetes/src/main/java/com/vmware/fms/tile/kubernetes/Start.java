package com.vmware.fms.tile.kubernetes;

import com.vmware.fms.bundle.vrcs.common.http.TektonHttpClientImpl;
import com.vmware.fms.tile.common.TileExecutable;
import com.vmware.fms.tile.common.TileExecutableRequest;
import com.vmware.fms.tile.common.TileExecutableResponse;
import com.vmware.fms.tile.common.TileProperties;
import service.ConvertToJson;
import service.HttpResponse;
import service.IsJson;
import service.KubernetesMasterConfig;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class Start implements TileExecutable {

    private static final Logger logger = Logger.getLogger(Start.class.getName());
    @Override
    public void handleExecute(TileExecutableRequest request, TileExecutableResponse response){
        TileProperties inputProps = request.getInputProperties().getAsProperties("kubernetesMaster");
        KubernetesMasterConfig config = new KubernetesMasterConfig(inputProps);
        String hostUrl = config.getMaster();
        String user_name = config.getUser_name();
        String password = config.getPassword();
        String jobType = request.getInputProperties().getAsString("jobName");
        String jobUrl ;
        String reply = new String();
        if(jobType.equals("createPod")){
            jobUrl = "pods";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "api/v1/namespaces/"+nameSpace+"/";
            String url = hostUrl + createUrl+jobUrl;
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            
            HttpResponse client = new HttpResponse(user_name,password);
            try {
                reply = client.post(url, contents, "application/json");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(jobType.equals("createPv")){
            jobUrl = "persistentvolumes";
            String createUrl = "api/v1/";
            String url = hostUrl + createUrl+jobUrl;
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            HttpResponse client = new HttpResponse(user_name,password);
            try {
                reply = client.post(url, contents, "application/json");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(jobType.equals("createPvc")){
            jobUrl = "persistentvolumeclaims";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "api/v1/namespaces/"+nameSpace+"/";
            String url = hostUrl + createUrl+jobUrl;
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))) {
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            HttpResponse client = new HttpResponse(user_name,password);
            try {
                reply = client.post(url, contents, "application/json");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(jobType.equals("createDp")){
            jobUrl = "deployments";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "apis/extensions/v1beta1/namespaces/"+nameSpace+"/";
            String url = hostUrl + createUrl+jobUrl;
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            TektonHttpClientImpl client = new TektonHttpClientImpl("", "");
            reply = client.post(url, contents, "application/json");
        }
        else if (jobType.equals("createRc")) {
            jobUrl = "replicationcontrollers";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "api/v1/namespaces/"+nameSpace+"/";
            String url = hostUrl + createUrl + jobUrl;
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            TektonHttpClientImpl client = new TektonHttpClientImpl("", "");
            reply = client.post(url, contents, "application/json");
        }
        else if (jobType.equals("createSecrets")) {
            jobUrl = "secrets";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "api/v1/namespaces/"+nameSpace+"/";
            String url = hostUrl + createUrl + jobUrl;
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            TektonHttpClientImpl client = new TektonHttpClientImpl("", "");
            reply = client.post(url, contents, "application/json");
        }
        else if (jobType.equals("createRs")) {
            jobUrl = "replicationcontrollers";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "apis/extensions/v1beta1/namespaces/"+nameSpace+"/";
            String url = hostUrl + createUrl + jobUrl;
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            TektonHttpClientImpl client = new TektonHttpClientImpl("", "");
            reply = client.post(url, contents, "application/json");
        }
        else if (jobType.equals("createNs")){
            jobUrl = "namespaces";
            String createUrl = "api/v1/";
            String url = hostUrl + createUrl + jobUrl;
            TektonHttpClientImpl client = new TektonHttpClientImpl("", "");
            String name  = request.getInputProperties().getAsString("nameSpaceVal");
            String contents = "{\"kind\":\"Namespace\",\"apiVersion\":\"v1\",\"metadata\":{\"name\":\""+name+"\",\"creationTimestamp\":null},\"spec\":{},\"status\":{}}\n";
            reply = client.post(url, contents, "application/json");
        }else if(jobType.equals("createSvc")){
            jobUrl = "services";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "api/v1/namespaces/"+nameSpace+"/";
            String url = hostUrl + createUrl + jobUrl;
            String contents = request.getInputProperties().getAsString("definitionContent");
            if(!(IsJson.isJSONValid(contents))){
                contents = ConvertToJson.convertToJson(contents);
            }
            logger.info(contents);
            TektonHttpClientImpl client = new TektonHttpClientImpl("", "");
            reply = client.post(url, contents, "application/json");
        }
        else if(jobType.equals("deletePod")){
            jobUrl = "pods/";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "api/v1/namespaces/"+nameSpace+"/";
            String podName = request.getInputProperties().getAsString("deleteParam");
            String url = hostUrl + createUrl+jobUrl+podName;
            HttpResponse client = new HttpResponse(user_name,password);
            try {
                reply = client.delete(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(jobType.equals("deleteDp")){
            jobUrl = "deployments/";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "apis/extensions/v1beta1/namespaces/"+nameSpace+"/";
            String podName = request.getInputProperties().getAsString("deleteParam");
            String url = hostUrl + createUrl+jobUrl+podName;
            HttpResponse client = new HttpResponse(user_name,password);
            try {
                reply = client.delete(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(jobType.equals("deleteRc")){
            jobUrl = "replicationcontrollers/";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "api/v1/namespaces/"+nameSpace+"/";
            String podName = request.getInputProperties().getAsString("deleteParam");
            String url = hostUrl + createUrl+jobUrl+podName;
            HttpResponse client = new HttpResponse(user_name,password);
            try {
                reply = client.delete(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(jobType.equals("deleteSecrets")){
            jobUrl = "secrets/";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "api/v1/namespaces/"+nameSpace+"/";
            String secretName = request.getInputProperties().getAsString("deleteParam");
            String url = hostUrl + createUrl+jobUrl+secretName;
            HttpResponse client = new HttpResponse(user_name,password);
            try {
                reply = client.delete(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(jobType.equals("deleteRs")){
            jobUrl = "replicasets/";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "apis/extensions/v1beta1/namespaces/"+nameSpace+"/";
            String podName = request.getInputProperties().getAsString("deleteParam");
            String url = hostUrl + createUrl+jobUrl+podName;
            HttpResponse client = new HttpResponse(user_name,password);
            try {
                reply = client.delete(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(jobType.equals("deleteNs")){
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "api/v1/namespaces/";
            String podName = request.getInputProperties().getAsString("deleteParam");
            String url = hostUrl + createUrl+podName;
            HttpResponse client = new HttpResponse(user_name,password);
            try {
                reply = client.delete(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(jobType.equals("deleteSvc")){
            jobUrl = "services/";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "api/v1/namespaces/"+nameSpace+"/";
            String podName = request.getInputProperties().getAsString("deleteParam");
            String url = hostUrl + createUrl+jobUrl+podName;
            HttpResponse client = new HttpResponse(user_name,password);
            try {
                reply = client.delete(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(jobType.equals("deletePvc")){
            jobUrl = "persistentvolumeclaims/";
            String nameSpace = request.getInputProperties().getAsString("nameSpaceOpt");
            String createUrl = "api/v1/namespaces/"+nameSpace+"/";
            String podName = request.getInputProperties().getAsString("deleteParam");
            String url = hostUrl + createUrl+jobUrl+podName;
            HttpResponse client = new HttpResponse(user_name,password);
            try {
                reply = client.delete(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(jobType.equals("deletePv")){
            jobUrl = "persistentvolumes/";
            String createUrl = "api/v1/";
            String podName = request.getInputProperties().getAsString("deleteParam");
            String url = hostUrl + createUrl+jobUrl+podName;
            HttpResponse client = new HttpResponse(user_name,password);
            try {
                reply = client.delete(url);
            } catch (IOException e) {
                e.printStackTrace();
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
        response.getOutputProperties().setJson("response",reply);
        response.setCompleted(true);

    }
}