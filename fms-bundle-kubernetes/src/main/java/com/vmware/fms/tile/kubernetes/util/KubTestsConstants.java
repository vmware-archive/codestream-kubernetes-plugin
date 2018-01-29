/**
 * Copyright © 2017 VMware, Inc. All Rights Reserved.
 */
package com.vmware.fms.tile.kubernetes.util;
public interface KubTestsConstants {
    String MOCK_URL = "DD";
    String KUB_URL_ = "url";
    String USERNAME_ = "username";
    String MOCK_USERNAME = "f";
    String MOCK_PWD = "f";
    String PASSWORD = "password";
    String MOCK_GET_LISTNS_URL = "DD/api/v1/namespaces";
    String MOCK_GET_LISTPODS_URL = "DD/api/v1/namespaces/default/pods";
    String MOCK_GET_LISTRC_URL = "DD/api/v1/namespaces/default/replicationcontrollers";
    String MOCK_GET_LISTRS_URL = "DD/apis/extensions/v1beta1/namespaces/default/replicasets";
    String MOCK_GET_LISTPV_URL = "DD/api/v1/persistentvolumes";
    String MOCK_GET_LISTPVC_URL = "DD/api/v1/namespaces/default/persistentvolumeclaims";
    String MOCK_GET_LISTSVC_URL = "DD/api/v1/namespaces/default/services";
    String MOCK_GET_LISTIMAGES_URL = "DD/apis/extensions/v1beta1/namespaces/default/deployments/depname";
    String MOCK_GET_LISTDEPLOYMENT_URL = "DD/apis/extensions/v1beta1/namespaces/default/deployments";
}
