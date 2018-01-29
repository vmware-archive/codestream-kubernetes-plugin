/*
 * Copyright (c) 2017-2018 VMware, Inc. All Rights Reserved.
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
