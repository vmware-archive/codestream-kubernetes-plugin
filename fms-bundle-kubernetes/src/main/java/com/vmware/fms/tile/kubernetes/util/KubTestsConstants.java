/**
 Copyright (c) 2017-2018 VMware, Inc. All Rights Reserved.
 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
