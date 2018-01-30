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
import org.apache.commons.lang.StringUtils;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            String masterUrl = inputProps.getAsString("url");

            //To check if the url is empty
            if (StringUtils.isBlank(masterUrl)) {
                logger.log(Level.SEVERE, "Invalid input property. 'url' is not found for given Kubernetes master");
                response.setFailed("Invalid input property. 'url' is not found for given Kubernetes master");
            }
            KubernetesMasterConfig config = new KubernetesMasterConfig(inputProps);
            //To format the serverUrl
            masterUrl = KubernetesTileUtils.prepUrl(config.getMaster());
            // Checking url against the pattern checking its correctness
            String pattern = "\\Ahttp[s]?://.+[:\\d{1,5}]/\\z";
            Pattern urlPattern = Pattern.compile(pattern);
            Matcher matcher = urlPattern.matcher(config.getMaster());
            if (!matcher.find()) {
                logger.log(Level.SEVERE, "Invalid server url");
                response.setFailed("Invalid server url");
            }
        } catch (IllegalArgumentException ie) {
            logger.log(Level.SEVERE, "Failed to parse kubernetes master endpoint");
            response.setFailed("Invalid kubernetes master specification");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to connect to kubernetes master endpoint ",ex);
            response.setFailed("Unable to connect to kubernetes master ");
        }
    }
}
