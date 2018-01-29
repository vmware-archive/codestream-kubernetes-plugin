/**
 * Copyright © 2017 VMware, Inc. All Rights Reserved.
 */
package com.vmware.fms.tile.kubernetes.util;
import java.util.ArrayList;
import java.util.List;
public class Status {
    public String phase;
    public List<Condition> conditions = new ArrayList<Condition>();
    public String hostIP;
    public String podIP;
    public String startTime;
    public List<ContainerStatus> containerStatuses = new ArrayList<ContainerStatus>();
    public String message;
    public String reason;
    public Integer replicas;
    public Integer fullyLabeledReplicas;
    public Integer readyReplicas;
    public Integer availableReplicas;
    public Integer observedGeneration;
}
