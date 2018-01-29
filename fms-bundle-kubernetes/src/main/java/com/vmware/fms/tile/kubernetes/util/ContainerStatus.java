/**
 * Copyright © 2017 VMware, Inc. All Rights Reserved.
 */
package com.vmware.fms.tile.kubernetes.util;
public class ContainerStatus {

    public String name;
    public State state;
    public LastState lastState;
    public Boolean ready;
    public Integer restartCount;
    public String image;
    public String imageID;
    public String containerID;
}
