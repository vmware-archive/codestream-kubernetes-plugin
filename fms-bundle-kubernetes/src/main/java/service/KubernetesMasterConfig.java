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

import com.vmware.fms.tile.common.TileProperties;
import org.apache.commons.lang.StringUtils;

public class KubernetesMasterConfig {

    private final String master;

    private final String user_name;

    private final String password;

    public KubernetesMasterConfig(TileProperties inputProps) {
        if (inputProps == null) {
            throw new IllegalArgumentException(
                    "Invalid input property. 'Master' input paramter not found.");
        }

        String serverString = inputProps.getAsString("url");
        if (StringUtils.isBlank(serverString)) {
            throw new IllegalArgumentException(
                    "Invalid input property. 'url' is not found for given Jenkins Server");
        }

        user_name = inputProps.getAsString("username");
        password = inputProps.getAsString("password");


        master = KubernetesTileUtils.prepUrl(serverString);

    }


    public String getMaster() {
        return master;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPassword() {
        return password;
    }
}
