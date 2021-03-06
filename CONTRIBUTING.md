

# Contributing to codestream-kubernetes-plugin

The codestream-kubernetes-plugin project team welcomes contributions from the community. Before you start working with codestream-kubernetes-plugin, please read our [Developer Certificate of Origin](https://cla.vmware.com/dco). All contributions to this repository must be signed as described on that page. Your signature certifies that you wrote the patch or have the right to pass it on as an open-source patch.

## Getting Started
### Prerequisites
1. A Kubernetes Cluster is already deployed.
2. Kubernetes cluster's KUBECONF file is required.
3. A development machine running on Microsoft Windows, Apple Mac OS X or Linux.
4. A development appliance of VMware vRealize Code Stream installed in your environment (Recommended).
5. JDK 8
6. Maven 3.1.1+

### Build & Run
Setting Environment to build the plugin
1. Clone vRealize Code Stream Plug-In SDK(https://github.com/vmwaresamples/vrcs-sdk-samples.git) in your development machine.
2. Go to the directory /lib from the root of the repository vrcs-sdk-samples and build using mvn clean install.
3. Go back to the root and build using mvn clean install.

Building the plugin
1. Clone this repository to the same development machine where above steps were performed.
2. Go under the directory fms-bundle-kubernetes and build using mvn clean install.
3. Go to the target/ directory under root and copy the fms-bundle-kubernetes-2.0.2.zip to       VRCS_APPLIANCE_HOST:/var/lib/codestrem/plugins/
4. Unzip the zip file and restart the vcac-server service. (Itwill take 10-15 minutes for all the service to start. You can check VRBC_INSTANCE_URL:5480).

## Contribution Flow

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

This is a rough outline of what a contributor's workflow looks like:

- Create a topic branch from where you want to base your work
- Make commits of logical units
- Make sure your commit messages are in the proper format (see below)
- Push your changes to a topic branch in your fork of the repository
- Submit a pull request

Example:

``` shell
git remote add upstream https://github.com/vmware/codestream-kubernetes-plugin.git
git checkout -b my-new-feature master
git commit -a
git push origin my-new-feature
```

### Staying In Sync With Upstream

When your branch gets out of sync with the vmware/master branch, use the following to update:

``` shell
git checkout my-new-feature
git fetch -a
git pull --rebase upstream master
git push --force-with-lease origin my-new-feature
```

### Updating pull requests

If your PR fails to pass CI or needs changes based on code review, you'll most likely want to squash these changes into
existing commits.

If your pull request contains a single commit or your changes are related to the most recent commit, you can simply
amend the commit.

``` shell
git add .
git commit --amend
git push --force-with-lease origin my-new-feature
```

If you need to squash changes into an earlier commit, you can use:

``` shell
git add .
git commit --fixup <commit>
git rebase -i --autosquash master
git push --force-with-lease origin my-new-feature
```

Be sure to add a comment to the PR indicating your new changes are ready to review, as GitHub does not generate a
notification when you git push.

### Formatting Commit Messages

We follow the conventions on [How to Write a Git Commit Message](http://chris.beams.io/posts/git-commit/).

Be sure to include any related GitHub issue references in the commit message.  See
[GFM syntax](https://guides.github.com/features/mastering-markdown/#GitHub-flavored-markdown) for referencing issues
and commits.

## Reporting Bugs and Creating Issues

When opening a new issue, try to roughly follow the commit message format conventions above.
