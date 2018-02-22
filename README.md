

# codestream-kubernetes-plugin

## Overview
This vRealize Code Stream plugin automates CI/CD operations with Kubernetes. This plugin supports operations on Kubernetes objects including Namespace, Deployment, Pod, Replica Set, Replication Controller, Services and Secrets. It also helps perform upgrades of container images. The aim of this open source plugin is to release new capabilities faster and also allow anyone to modify it for their own purposes and to be able to contribute back to the larger vRealize Code Stream community.

## Compatibility with vRealize Code Stream Versions
vRealize Code Stream 2.2, vRealize Code Stream 2.3, vRealize Code Stream 2.4

## Compatibility with Kubernetes Versions
Kubernetes 1.5, 1.6, 1.7, 1.8, 1.9 

### Prerequisites
1. A Kubernetes Cluster is already deployed.
2. Kubernetes cluster's kubeconfig file.
3. A development machine running on Microsoft Windows, Apple Mac OS X or Linux.
4. A development appliance of vRealize Code Stream installed in your environment (Recommended).
5. JDK 8
6. Maven 3.1.1+

### Build & Run
Setting Environment to build the plugin
1. Clone vRealize Code Stream Plug-In SDK (https://github.com/vmwaresamples/vrcs-sdk-samples.git) in your development machine.
2. Go to the directory /lib from the root of the repository vrcs-sdk-samples and build using mvn clean install.
3. Go back to the root and build using mvn clean install.

Building the plugin
1. Clone this repository to the same development machine where above steps where performed.
2. Go under the directory fms-bundle-kubernetes and build using mvn clean install.
3. Go to the target/ directory under root and copy the fms-bundle-kubernetes-2.0.2.zip to       VRCS_APPLIANCE_HOST:/var/lib/codestrem/plugins/
4. Unzip the zip file and restart the vcac-server service. (It will take 10-15 minutes for all the service to start. You can check VRBC_INSTANCE_URL:5480).

## Current Features
1. Create/Delete Deployment
2. Create/Delete ReplicaSet
3. Create/Delete Replication Controller
4. Create/Delete Pod
5. Create/Delete Namespace
6. Create/Delete Service
7. Create/Delete Persistent Volume
8. Create/Delete Persistent Volume Claim
9. Create/Delete Secrets
10. Update of image versions in a deployment

## Contributing

The codestream-kubernetes-plugin project team welcomes contributions from the community. Before you start working with codestream-kubernetes-plugin, please read our [Developer Certificate of Origin](https://cla.vmware.com/dco). All contributions to this repository must be signed as described on that page. Your signature certifies that you wrote the patch or have the right to pass it on as an open-source patch. For more detailed information, refer to [CONTRIBUTING.md](CONTRIBUTING.md).

NOTE: This plugin uses backward compatibilty of deployment and replicaset. Please give API version for deployment and replicaset as 'extention/v1beta1'. 

Example yaml file:

```
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  replicas: 3
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.7.9
        ports:
        - containerPort: 80
```
