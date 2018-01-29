/**
 * Copyright © 2017 VMware, Inc. All Rights Reserved.
 */
package com.vmware.fms.tile.kubernetes.util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class Spec {
    public String serviceName;
    public Integer replicas;
    public String terminationGracePeriodSeconds;
    public List<Container> containers = new ArrayList<Container>();
    public String restartPolicy;
    public String type;
    public String persistentVolumeReclaimPolicy;
    public Selector selector;
    public String clusterIP;
    public List<Port> ports;
    public Template template;
    public Capacity capacity;
    public List<Map<String,String>> imagePullSecrets;
    public List<String> accessModes;
    public Resources resources;
    public List<VolumeClaimTemplates> volumeClaimTemplates = new ArrayList<>();

}
