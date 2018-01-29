package com.vmware.fms.tile.kubernetes;

import com.vmware.fms.bundle.vrcs.common.http.TektonHttpClientImpl;
import service.HttpResponse;
import service.KubernetesMasterConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UpdateImg  {
    private static final Logger logger = Logger.getLogger(UpdateImg.class.getName());

    UpdateImg(KubernetesMasterConfig config, String depName, List<String> listArr, List<String> imageVer, String nameSpace,String reply) {
        logger.info("\nEntered UpdateImg\n");
        String hostUrl = config.getMaster();
        String user_name = config.getUser_name();
        String password = config.getPassword();
        String createUrl ="apis/extensions/v1beta1/namespaces/"+nameSpace+"/deployments/";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type","application/strategic-merge-patch+json");
        headers.put("accept","application/json");
        String url = hostUrl+createUrl+depName;
        String partialPayload= "";
        Integer size = listArr.size();
        for(Integer i =0;i<size;i++)
        {
            String val = listArr.get(i);
            String[] str_array = val.split(":");
            String image = str_array[0];
            String ver = imageVer.get(i);
            String load = "{\n\t\t\t\t\t\t\"image\":\""+image+":"+ver+"\",\n\t\t\t\t\t\t\"name\":\""+image+"\"\n\t\t\t\t\t\t}";
            if(i==0)
            {
                partialPayload = load;
            }
            else {
                partialPayload=partialPayload+",\n\t\t\t\t\t\t"+load;
            }
        }
        String payload= payload = "{\n\t\"metadata\":{\n\t\"creationTimestamp\":null\n\t},\n\t\"spec\":{\n\t\t\"template\":{\n\t\t\t\"spec\":{\n\t\t\t\t\"containers\":["+partialPayload+"]\n\t\t\t\t\t}\n\t\t\t\t\t}\n\t\t\t}\n}\n";
        HttpResponse client = new HttpResponse(user_name,password);
        try {
            reply = client.patch(url,payload,"application/json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(reply);
    }
}
