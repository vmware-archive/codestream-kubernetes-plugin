/**
 * Copyright © 2017 VMware, Inc. All Rights Reserved.
 */
package com.vmware.fms.tile.kubernetes.util;
import java.util.List;
import java.util.Map;
public class Selector {
    public String app,name;
    public Map<String,String> matchLabels;
    public List<Object> matchExpression;

}
