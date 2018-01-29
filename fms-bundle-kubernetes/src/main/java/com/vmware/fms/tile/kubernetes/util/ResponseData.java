/**
 * Copyright © 2017 VMware, Inc. All Rights Reserved.
 */
package com.vmware.fms.tile.kubernetes.util;
import java.util.Map;
public class ResponseData {
    public Metadata metadata;
    public Spec spec;
    public Map<String,Object> status;
}
