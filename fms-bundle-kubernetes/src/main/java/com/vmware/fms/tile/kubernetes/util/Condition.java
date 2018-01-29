/**
 * Copyright © 2017 VMware, Inc. All Rights Reserved.
 */
package com.vmware.fms.tile.kubernetes.util;
public class Condition {
    public String type;
    public String status;
    public Object lastProbeTime;
    public String lastTransitionTime;
    public String reason;
}
