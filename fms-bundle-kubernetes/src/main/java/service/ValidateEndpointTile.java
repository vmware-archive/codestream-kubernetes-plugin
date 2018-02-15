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

package service;

import com.vmware.fms.tile.common.TileExecutable;
import com.vmware.fms.tile.common.TileExecutableRequest;
import com.vmware.fms.tile.common.TileExecutableResponse;
import com.vmware.fms.tile.common.TileProperties;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import org.apache.commons.lang.StringUtils;

import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ValidateEndpointTile implements TileExecutable {

    private static final Logger logger = Logger.getLogger(ValidateEndpointTile.class.getName());

    @Override
    public void handleExecute(TileExecutableRequest request, TileExecutableResponse response) {
        TileProperties inputProps = request.getInputProperties().getAsProperties("kubernetesMaster");
        try {
            if(inputProps==null)
            {
                response.setFailed("Invalid input property. 'Kubernetes' input parameter not found.");
            }
            KubernetesMasterConfig config = new KubernetesMasterConfig(inputProps);
            String kubeconf = config.getKubeconfig();

            //To check if the kubeconfig is empty
            if (StringUtils.isBlank(kubeconf)) {
                logger.log(Level.SEVERE, "Invalid input property. 'kubeconfig' is not found for given Kubernetes master");
                response.setFailed("Invalid input property. 'kubeconfig' is not found for given Kubernetes endpoint");
            }

            Reader targetKubernetes  = new StringReader(kubeconf);
            KubeConfig kubeconfig = KubeConfig.loadKubeConfig(targetKubernetes);
            ApiClient client = Config.fromConfig(kubeconfig);
            Configuration.setDefaultApiClient(client);

            CoreV1Api api = new CoreV1Api();
            logger.log(Level.INFO, String.valueOf(api.listNode(null,null,null,null,20,null)));

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to get the list of Nodes  ",ex);
            response.setFailed("Unable to fetch the List of Nodes from the given kubeconfig");
        }
    }
}
