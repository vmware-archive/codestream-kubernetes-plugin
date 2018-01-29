/*
 * Copyright (c) 2017-2018 VMware, Inc. All Rights Reserved.
 */

package service;

import org.apache.commons.lang.StringUtils;

public class KubernetesTileUtils {

    public static String prepUrl(String url) {
        if (!StringUtils.isBlank(url)) {
            if (!url.endsWith("/")) {
                return url + "/";
            }
        }
        return url;
    }

}
