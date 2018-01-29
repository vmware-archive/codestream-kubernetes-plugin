/**
 * Copyright © 2017 VMware, Inc. All Rights Reserved.
 */
package com.vmware.fms.tile.kubernetes.util;
import java.util.ArrayList;
import java.util.List;
public class Container {

    public String name;
    public String image;
    public List<String> command = new ArrayList<>();
    public List<String> args = new ArrayList<>();
    public Resources resources;
    public List<VolumeMount> volumeMounts = new ArrayList<VolumeMount>();
    public String terminationMessagePath;
    public String imagePullPolicy;
    public List<Port> ports = new ArrayList<Port>();
    public List<Env> env = new ArrayList<Env>();
}
