

# codestream-kubernetes-plugin

## Overview
This vRealize Code Stream plugin automates CI/CD operations with Kubernetes. This plugin supports operations on Kubernetes objects including Namespace, Deployment, Pod, Replica Set, Replication Controller, Services and Secrets. It also helps perform upgrades of container images. The aim of this open source plugin is to release new capabilities faster and also allow anyone to modify it for their own purposes and to be able to contribute back to the larger vRealize Code Stream community.

## Compatibility with vRealize Code Stream Versions
vRealize Code Stream 2.2 ,vRealize Code Stream 2.3

## Compatibility with Kubernetes Versions
Kubernetes 1.5.2 (*currently basic auth only)

### Prerequisites
1. A Kubernetes Cluster already deployed
2. Kubernetes cluster's master IP address or DNS name with API auth
3. A development machine running on Microsoft Windows, Apple Mac OS X or Linux.
4. A development appliance of vRCS installed in your environment (Recommended).
5. JDK 8
6. Maven 3.1.1+

### Build & Run
Setting Environment to build the plugin
1. Clone vRealize Code Stream Plug-In SDK(https://github.com/vmwaresamples/vrcs-sdk-samples.git) in your development machine
2. Go to the directory /lib from the root of the repository vrcs-sdk-samples and build using mvn clean install 
3. Go back to the root and build using mvn clean install

Building the Kubernetes plugin
1. Clone this repository to the same development machine where above steps where performed.
2. Go under the directory fms-bundle-kubernetes and build using mvn clean install
3. Go to the target/ directory under root and copy the fms-bundle-kubernetes-2.0.2.zip to       VRCS_APPLIANCE_HOST:/var/lib/codestrem/plugins/
4. Unzip the zip file and restart the vcac-server service. (Itwill take 10-15 minutes for all the service to start. You can check in VRBC_INSTANCE_URL:5480)

## Documentation

## Releases & Major Branches

## Contributing

The codestream-kubernetes-plugin project team welcomes contributions from the community. If you wish to contribute code and you have not
signed our Contributor License Agreement (CLA), our bot will update the issue when you open a Pull Request. For any
questions about the CLA process, please refer to our [FAQ](https://cla.vmware.com/faq). For more detailed information,
refer to [CONTRIBUTING.md](CONTRIBUTING.md).

## License
