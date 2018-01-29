/**
 * Copyright © 2017 VMware, Inc. All Rights Reserved.
 */
package com.vmware.fms.tile.kubernetes.util;
import java.util.Map;
public class Metadata {

    public String name;
    public String generateName;
    public String namespace;
    public String selfLink;
    public String uid;
    public String resourceVersion;
    public String creationTimestamp;
    public Map<String,String> annotations;
    public Integer generation;
    public Label labels;


}
