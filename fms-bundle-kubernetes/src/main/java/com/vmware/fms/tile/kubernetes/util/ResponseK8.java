/**
 * Copyright © 2017 VMware, Inc. All Rights Reserved.
 * */
package com.vmware.fms.tile.kubernetes.util;
import java.util.List;
public class ResponseK8 {
    public String kind;
    public String apiVersion;
    public Metadata metadata;
    public Spec spec;
    public Status status;
    public Data data;
    public List<ResponseData> items;

}
