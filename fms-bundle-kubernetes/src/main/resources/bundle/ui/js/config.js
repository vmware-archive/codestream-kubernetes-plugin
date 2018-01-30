/*
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

'use strict';

window.Kubernetes = window.Kubernetes || {};

Kubernetes.config = (function(){
    var _shouldPopulateSavedInput = false;
    var editor, editorOptions;
    var scriptEditorLang = 'Bash';
    var scriptEditorContent = VRCS.context.task.inputParameters.values.definitionContent || '';
    var inputParam = {};
    var input_image= {};
    var deleteVal;
    var _inputValues;


    var populateDeploymentList = function() {
    // This function is to list deployments which are present in a given namespace
        $('#depSelectDiv').show();
        var endpoint =  $('#endpointSelect').val();
        var nameSpace = $('#nsSelect').val();
        if(endpoint==='other'){
                                endpoint = $('#endpointSelectText').val();
                            }

        if(nameSpace==='other'){
                        nameSpace = $('#nsSelectText').val();
                    }

        VRCS.io.callTile({
            tile: 'vrcs.kubernetes:get_deployment',
            inputParameters: {
                kubernetesMaster: endpoint,
                nameSpaceVal: nameSpace
                            },
            complete: function(outputParameters) {
                        // load the options in job select
                var jobs = outputParameters.listDeployment;
                jobs = jobs.map(function(jobName) {
                        return {
                                value: jobName,
                                text: jobName
                                };
                });
                $('#depSelect').html(getDeploymentOptions(jobs));
                // to populate saved config
                if(_shouldPopulateSavedInput){
                        populateSelectedValue($('#depSelect'), _inputValues.depName?_inputValues.depName:_inputValues.deleteParam);
                    }
            },

            fail: function(error) {
                 $('#depSelectDiv').hide();
                 $('#depSelectTextDiv').show();
                VRCS.view.showMessage('Other options is selected as Dep. is not Present');
            }
        });


    };

    var populateSelectedValue = function($select, value) {
        // Here to populate the selected value
        if (!_shouldPopulateSavedInput) {
            return;
        }
        $select.val(value).trigger('change');
    };

    var listDepImage = function(event){
    // To display the images present in seleceted deployment
            var val = $('#depSelect').val();
            if(val==='other'){
                val = $('#depSelectDiv').val();
            }
            $('#imageSelectDiv').show();
            var endpoint =  $('#endpointSelect').val();
            var nameSpace = $('#nsSelect').val();
            if(nameSpace==='other'){
                nameSpace = $('#nsSelectText').val();
            }
            if(endpoint==='other'){
                             endpoint = $('#endpointSelectText').val();
                         }
            VRCS.io.callTile({
            tile: 'vrcs.kubernetes:get_images',
            inputParameters: {
                kubernetesMaster: endpoint,
                depName: val,
                nameSpaceVal: nameSpace
                            },
            complete: function(outputParameters) {
                        // load the options in job select
                var images = outputParameters.listImages;
                var num = outputParameters.imageNum;
               images = images.map(function(imageName) {
                        return {
                                value: imageName,
                                text: imageName
                                };
                });

            fillTable(images);


            },

            fail: function(error) {

                VRCS.view.showMessage('Failed to retrieve deployments images(Endpoint cant be others).');
            }
        });
    };

    var listSvc = function() {
    // To list the services present in the given namespace
            $('#listSvc').show();
            var endpoint =  $('#endpointSelect').val();
            var nameSpace = $('#nsSelect').val();
            if(nameSpace==='other'){
                            nameSpace = $('#nsSelectText').val();
                        }
            if(endpoint==='other'){
                                        endpoint = $('#endpointSelectText').val();
                                    }
            VRCS.io.callTile({
                tile: 'vrcs.kubernetes:list_svc',
                inputParameters: {
                    kubernetesMaster: endpoint,
                    nameSpaceVal: nameSpace
                                },
                complete: function(outputParameters) {
                            // load the options in job select
                    var jobs = outputParameters.listSvc;
                    jobs = jobs.map(function(jobName) {
                            return {
                                    value: jobName,
                                    text: jobName
                                    };
                    });
                    $('#svcSelect').html(getSvcOptions(jobs));
                    if(_shouldPopulateSavedInput){
                        populateSelectedValue($('#svcSelect'), _inputValues.deleteParam);
                    }
                },

                fail: function(error) {
                       $('#listSvc').hide();
                       $('#svcSelectTextDiv').show();
                        VRCS.view.showMessage('Other options is selected as SVC is not Present');
                }
            });
        };
    var listRc = function() {
     // To list the Replication controller present in the given namespace
        $('#listRc').show();
        var endpoint =  $('#endpointSelect').val();
        var nameSpace = $('#nsSelect').val();
        if(nameSpace==='other'){
                        nameSpace = $('#nsSelectText').val();
                    }
        if(endpoint==='other'){
                                endpoint = $('#endpointSelectText').val();
                            }
        VRCS.io.callTile({
            tile: 'vrcs.kubernetes:list_rc',
            inputParameters: {
                kubernetesMaster: endpoint,
                nameSpaceVal: nameSpace
                            },
            complete: function(outputParameters) {
                        // load the options in job select
                var jobs = outputParameters.listRc;
                jobs = jobs.map(function(jobName) {
                        return {
                                value: jobName,
                                text: jobName
                                };
                });
                $('#rcSelect').html(getRcOptions(jobs));
                 if(_shouldPopulateSavedInput){
                        populateSelectedValue($('#rcSelect'), _inputValues.deleteParam);
                    }
            },

            fail: function(error) {
                        $('#listRc').hide();
                        $('#rcSelectTextDiv').show();
                        VRCS.view.showMessage('Other options is selected as RC is not present');
            }
        });
    };

    var listRs = function() {
     // To display the replica sets  present in seleceted namespace
            $('#listRs').show();
            var endpoint =  $('#endpointSelect').val();
            var nameSpace = $('#nsSelect').val();
            if(nameSpace==='other'){
                            nameSpace = $('#nsSelectText').val();
                        }
            if(endpoint==='other'){
                                        endpoint = $('#endpointSelectText').val();
                                    }
            VRCS.io.callTile({
                tile: 'vrcs.kubernetes:list_rs',
                inputParameters: {
                    kubernetesMaster: endpoint,
                    nameSpaceVal: nameSpace
                                },
                complete: function(outputParameters) {
                            // load the options in job select
                    var jobs = outputParameters.listRs;
                    jobs = jobs.map(function(jobName) {
                            return {
                                    value: jobName,
                                    text: jobName
                                    };
                    });
                    $('#rsSelect').html(getRsOptions(jobs));
                     if(_shouldPopulateSavedInput){
                        populateSelectedValue($('#rsSelect'), _inputValues.deleteParam);
                    }
                },

                fail: function(error) {
                        $('#listRs').hide();
                        $('#rsSelectTextDiv').show();
                        VRCS.view.showMessage('Other options is selected as RS is not present');
                }
            });
        };

    var listPvc = function() {
     // To display the Persistent Volume claim present in seleceted namespace.
                $('#listPvc').show();
                var endpoint =  $('#endpointSelect').val();
                var nameSpace = $('#nsSelect').val();
                if(nameSpace==='other'){
                                nameSpace = $('#nsSelectText').val();
                            }
                if(endpoint==='other'){
                                                endpoint = $('#endpointSelectText').val();
                                            }
                VRCS.io.callTile({
                    tile: 'vrcs.kubernetes:list_pvc',
                    inputParameters: {
                        kubernetesMaster: endpoint,
                        nameSpaceVal: nameSpace
                                    },
                    complete: function(outputParameters) {
                                // load the options in job select
                        var jobs = outputParameters.listPvc;
                        jobs = jobs.map(function(jobName) {
                                return {
                                        value: jobName,
                                        text: jobName
                                        };
                        });
                        $('#pvcSelect').html(getPvcOptions(jobs));
                         if(_shouldPopulateSavedInput){
                        populateSelectedValue($('#pvcSelect'), _inputValues.deleteParam);
                    }
                    },

                    fail: function(error) {
                                $('#listPvc').hide();
                                $('#pvcSelectTextDiv').show();
                                VRCS.view.showMessage('Other options is selected as Pvc is not present');
                    }
                });
            };

    var listPv = function() {
     //To display the list of Persistent Volume.
                $('#listPv').show();
                    var endpoint =  $('#endpointSelect').val();

                    VRCS.io.callTile({
                        tile: 'vrcs.kubernetes:list_pv',
                        inputParameters: {
                            kubernetesMaster: endpoint
                                        },
                        complete: function(outputParameters) {
                                    // load the options in job select
                            var jobs = outputParameters.listPv;
                            jobs = jobs.map(function(jobName) {
                                    return {
                                            value: jobName,
                                            text: jobName
                                            };
                            });
                            $('#pvSelect').html(getPvOptions(jobs));
                            if(_shouldPopulateSavedInput){
                               populateSelectedValue($('#pvSelect'), _inputValues.deleteParam);
                            }
                        },

                        fail: function(error) {
                                 $('#listPv').hide();
                                 $('#pvSelectTextDiv').show();
                                VRCS.view.showMessage('Other options is selected as Pv is not present');

                        }
                    });
                };

    var listsecrets = function() {
     //To display the list of Persistent Volume.
                $('#listsecrets').show();
                    var endpoint =  $('#endpointSelect').val();
                    var nameSpace = $('#nsSelect').val();
                    if(nameSpace==='other'){
                        nameSpace = $('#nsSelectText').val();
                    }
                    VRCS.io.callTile({
                        tile: 'vrcs.kubernetes:get_secret',
                        inputParameters: {
                            kubernetesMaster: endpoint,
                            nameSpaceVal: nameSpace
                                        },
                        complete: function(outputParameters) {
                                    // load the options in job select
                            var jobs = outputParameters.listsecrets;
                            jobs = jobs.map(function(jobName) {
                                    return {
                                            value: jobName,
                                            text: jobName
                                            };
                            });
                            $('#secretsSelect').html(getSecretsOptions(jobs));
                            if(_shouldPopulateSavedInput){
                               populateSelectedValue($('#secretsSelect'), _inputValues.deleteParam);
                            }
                        },

                        fail: function(error) {
                                 $('#listsecrets').hide();
                                 $('#secretsSelectTextDiv').show();
                                VRCS.view.showMessage('Other options is selected as secrets is not present');

                        }
                    });
                };

    var listNs = function() {
    // TO list all the namespace present.
        $('#listNamespace').show();
        var endpoint =  $('#endpointSelect').val();

        VRCS.io.callTile({
            tile: 'vrcs.kubernetes:list_namespace',
            inputParameters: {
                kubernetesMaster: endpoint
                            },
            complete: function(outputParameters) {
                        // load the options in job select
                var jobs = outputParameters.listNs;
                jobs = jobs.map(function(jobName) {
                        return {
                                value: jobName,
                                text: jobName
                                };
                });
                $('#nsSelect').html(getNsOptions(jobs));
                   if(_shouldPopulateSavedInput){
                 			if(_inputValues.nameSpaceOpt.substring(0,2)=='${'&&_inputValues.deleteParam==""){
                 				 populateSelectedValue($('#nsSelect'),"other");
                 		    }
                 			else{
                                         populateSelectedValue($('#nsSelect'), _inputValues.nameSpaceOpt?_inputValues.nameSpaceOpt:_inputValues.deleteParam);
                                     }}
                             },

            fail: function(error) {
                    $('#listNamespace').hide();
                    $('#nsSelectTextDiv').show();
                    VRCS.view.showMessage('Other options is selected as NS is not present');
            }
        });
    };

    var listPods = function(){
    //To list all the pods present in the selected namespace.
        $('#listPods').show();
        var endpoint = $('#endpointSelect').val();
        var nameSpace = $('#nsSelect').val();
        if(nameSpace==='other'){
                        nameSpace = $('#nsSelectText').val();
                    }
        if(endpoint==='other'){
                                endpoint = $('#endpointSelectText').val();
                            }
        VRCS.io.callTile({
                    tile: 'vrcs.kubernetes:list_pods',
                    inputParameters: {
                        kubernetesMaster: endpoint,
                        nameSpaceVal: nameSpace
                    },
                    complete: function(outputParameters) {
                                // load the options in job select
                        var pods = outputParameters.listPods;
                        pods = pods.map(function(podName) {
                                return {
                                        value: podName,
                                        text: podName
                                        };
                        });
                        $('#podSelect').html(getPodsOptions(pods));
                        if(_shouldPopulateSavedInput){
                        populateSelectedValue($('#podSelect'), _inputValues.deleteParam);
                    }
                    },
                    fail: function(error) {
                    // If endpoint is filled on runtime other option should be selected
                        $('#listPods').hide();
                        $('#podSelectTextDiv').show();
                        VRCS.view.showMessage('Other options is selected as pod is not present');
                    }
        });
    };

    var getEndpointNameFromExpression = function(expression) {
            if (typeof expression !== 'string') {
                return '';
            }
            var splitPoint = expression.indexOf('@');
            if (splitPoint === -1) {
                // if cannot find the separator, return unmodified string
                return expression;
            }
            // get the name from the first separator "@" and the rest
            return expression.slice(splitPoint + 1, -3);
        };

    function checkVer(str){
        var re = /^((([0-9]+(\.|-)?)+)|latest)$/;
        if(!re.test(str))
            errorResponse('Specify proper version syntax.',$('#imageDiv'));
    }

    function fillTable(images){
    // Form table to list the image name and its version.
        $('#imageDiv').show();
        var table_body  = $("#imageSelectDiv");
        table_body.empty();
        for(var i=0;i<images.length;i++){
            table_body.append("<tr><td id='checkBox"+i+"'><input type = 'checkBox' value ='"+images[i].value+"' onclick = 'document.getElementById(value).disabled=!checked;'></td>;'");
            $("#checkBox"+i).after("<td class='left' id='imageName"+i+"'>"+images[i].value+"</td>");
            $("#imageName"+i).after("<td class='left'> <input type ='text' id='"+images[i].value+"' placeholder='Version' onblur=\"checkVer(this.value)\" pattern = '((([0-9]+(\.|-)?)+)|latest)' disabled='true'> </td></tr>");
         }
        }


    var getPodsOptions = function(options) {
                   var allOptions = [{
                       value: null,
                       text: '-- Select Pod --',
                       disabled: true,
                       selected: true
                   }].concat(options);
           var otherOptions = [{
               value: "other",
               text: '-- Other --'
           }];
           allOptions = allOptions.concat(otherOptions);
                   return getOptionElements(allOptions);
    } ;

    var getPvcOptions = function(options) {
                       var allOptions = [{
                           value: null,
                           text: '- Select PersistentVolumeClaim -',
                           disabled: true,
                           selected: true
                       }].concat(options);
           var otherOptions = [{
               value: "other",
               text: '-- Other --'
           }];
           allOptions = allOptions.concat(otherOptions);
                       return getOptionElements(allOptions);
        } ;

    var getPvOptions = function(options) {
                           var allOptions = [{
                               value: null,
                               text: '-- Select PersistentVolume --',
                               disabled: true,
                               selected: true
                           }].concat(options);
           var otherOptions = [{
               value: "other",
               text: '-- Other --'
           }];
           allOptions = allOptions.concat(otherOptions);
                           return getOptionElements(allOptions);
            } ;

    var getSecretsOptions = function(options) {
                           var allOptions = [{
                               value: null,
                               text: '-- Select Secrets --',
                               disabled: true,
                               selected: true
                           }].concat(options);
           var otherOptions = [{
               value: "other",
               text: '-- Other --'
           }];
           allOptions = allOptions.concat(otherOptions);
                           return getOptionElements(allOptions);
            } ;

    var getRcOptions = function(options) {
                   var allOptions = [{
                       value: null,
                       text: '-- Select Replication Controller --',
                       disabled: true,
                       selected: true
                   }].concat(options);
           var otherOptions = [{
               value: "other",
               text: '-- Other --'
           }];
           allOptions = allOptions.concat(otherOptions);

                   return getOptionElements(allOptions);
    } ;

    var getSvcOptions = function(options) {
                       var allOptions = [{
                           value: null,
                           text: '-- Select Service --',
                           disabled: true,
                           selected: true
                       }].concat(options);
           var otherOptions = [{
               value: "other",
               text: '-- Other --'
           }];
           allOptions = allOptions.concat(otherOptions);

                       return getOptionElements(allOptions);
    };

    var getRsOptions = function(options) {
                   var allOptions = [{
                       value: null,
                       text: '-- Select Replica Set --',
                       disabled: true,
                       selected: true
                   }].concat(options);
           var otherOptions = [{
               value: "other",
               text: '-- Other --'
           }];
           allOptions = allOptions.concat(otherOptions);
                   return getOptionElements(allOptions);
    };

    var getNsOptions = function(options) {
               var allOptions = [{
                   value: null,
                   text: '-- Select Namespace --',
                   disabled: true,
                   selected: true
               }].concat(options);
           var otherOptions = [{
               value: "other",
               text: '-- Other --'
           }];
           allOptions = allOptions.concat(otherOptions);
               return getOptionElements(allOptions);
           } ;

    var getEndpointOptions = function(options) {
            var allOptions = [{
                value: null,
                text: '-- Select Server --',
                disabled: true,
                selected: true
            }].concat(options);
              return getOptionElements(allOptions);
        };

    var getDeploymentOptions = function(options) {
           var allOptions = [{
               value: null,
               text: '-- Select Deployment --',
               disabled: true,
               selected: true
           }].concat(options);
           var otherOptions = [{
               value: "other",
               text: '-- Other --'
           }];
           allOptions = allOptions.concat(otherOptions);
           return getOptionElements(allOptions);
       } ;

    var getImageOptions = function(options){
           var allOptions = [{
               value: null,
               text: '-- Select Images to update --',
               disabled: true,
               selected: true
           }].concat(options);
           return getOptionElements(allOptions);
    };

    var getOptionElements = function(optionArray) {
            return optionArray.map(function(opt) {
                 return $('<option>', {
                     value: opt.value,
                     text: opt.text
                 }).prop({
                     disabled: opt.disabled,
                     selected: opt.selected
                 });
             });
         };

    var registerEventListeners= function(context) {
            $('input[id="definitionName"]').change(function(){
                $('#fileDisplay').show();
            });

            $('#endpointSelect').change(function(event) {
                var endpoint = $('#endpointSelect').val();
                if (!event.isTrigger) {
                    _shouldPopulateSavedInput = false;
                }
                        $('#jobName').val("selectJob");
                        $('#depSelectDiv').hide();
                        $('#imageSelectDiv').hide();
                        $('#scriptOptionDiv').hide();
                        $('#listNamespace').hide();
                        $('#imageSelect').empty();
                        $('#imageDiv').hide();
                        $('#listPods').hide();
                        $('#nameSpaceDiv').hide();
                        $('#listRs').hide();
                        $('#listRc').hide();
                        $('#remoteScriptDiv').hide();
                        $('#fileDisplay').hide();
                        $('#userDefinedScriptDiv').hide();
                        $('#listSvc').hide();
                        $('#listPvc').hide();
                        $('#listPv').hide();
                        $('#listsecrets').hide();
                        $('#depSelectTextDiv').hide();
                        $('#nsSelectTextDiv').hide();
                        $('#rcSelectTextDiv').hide();
                        $('#rsSelectTextDiv').hide();
                        $('#pvSelectTextDiv').hide();
                        $('#secretsSelectTextDiv').hide();
                        $('#pvcSelectTextDiv').hide();
                        $('#svcSelectTextDiv').hide();
                        $('#podSelectTextDiv').hide();
                 if(endpoint==='other'){
                       if(_shouldPopulateSavedInput){
                             populateSelectedValue($('#endpointSelectText'),_inputValues.kubernetesMaster);
                       }
                       $('#endpointSelectTextDiv').show();
                 }else{
                       $('#jobConfigMenuDiv').show();
                       if(_shouldPopulateSavedInput){
                            populateSelectedValue($('#jobName'), _inputValues.jobName);
                       }else{
                        $('#endpointSelectTextDiv').hide();
                        }
                 }
            });


            $('#nsSelect').change(function(event){
                 $('#nsSelectTextDiv').hide();
                var nameSpaceOpt = $('#nsSelect').val();
                if (!event.isTrigger) {
                _shouldPopulateSavedInput = false;
                }
                if(nameSpaceOpt==='other'){
                    if(_shouldPopulateSavedInput){

                                        populateSelectedValue($('#nsSelectText'),_inputValues.nameSpaceOpt);
                                    }
                    $('#nsSelectTextDiv').show();

                }else{
                    var jobType= $('#jobName').val();
                    if(jobType ==="updateImg")
                        if($('#endpointSelect').val()==='others'){
                            errorResponse("Endpoint cant be Others",$('#endpointSelectDiv'))
                        }else{
                        populateDeploymentList();
                        }
                    else if(jobType==="deletePod"){
                        listPods();
                    }else if(jobType==="deleteRc"){
                        listRc();
                    }else if(jobType==="deleteDp"){
                        populateDeploymentList();
                    }else if(jobType==="deleteRs"){
                        listRs();
                    }else if(jobType==='deleteSvc'){
                        listSvc();
                    }else if(jobType==='deletePvc'){
                        listPvc();
                    }else if(jobType==='deleteSecrets'){
                        listsecrets();
                    }
                    else if(jobType==='scaleRc'){
                        listRc();
                    }else if(jobType==='scaleRc'){
                        depSelectDiv();
                    }
                }


            });



            $('#rcSelect').change(function(event){
                $('#rcSelectTextDiv').hide();
                if (!event.isTrigger) {
                _shouldPopulateSavedInput = false;
                }
                var jobType= $('#jobName').val();
                var rcVal = $('#rcSelect').val();
                if(rcVal==='other')
                {
                    $('#rcSelectTextDiv').show();
                }
                if(jobType==='scaleRc'){
                    $('#numList').show();
                }
            });

            $('#rsSelect').change(function(event){
                $('#rsSelectTextDiv').hide();
                if (!event.isTrigger) {
                _shouldPopulateSavedInput = false;
                }

                var rsVal = $('#rsSelect').val();
                if(rsVal==='other')
                {
                    $('#rsSelectTextDiv').show();
                }

            });

            $('#pvSelect').change(function(event){
                $('#pvSelectTextDiv').hide();
                if (!event.isTrigger) {
                _shouldPopulateSavedInput = false;
                }

                var pvVal = $('#pvSelect').val();
                if(pvVal==='other')
                {
                    $('#pvSelectTextDiv').show();
                }

            });
            $('#secretsSelect').change(function(event){
                            $('#secretsSelectTextDiv').hide();
                            if (!event.isTrigger) {
                            _shouldPopulateSavedInput = false;
                            }

                            var secretsVal = $('#secretsSelect').val();
                            if(secretsVal==='other')
                            {
                                $('#secretsSelectTextDiv').show();
                            }

                        });

            $('#pvcSelect').change(function(event){
                $('#pvcSelectTextDiv').hide();
                if (!event.isTrigger) {
                _shouldPopulateSavedInput = false;
                }

                var pvcVal = $('#pvcSelect').val();
                if(pvcVal==='other')
                {
                    $('#pvcSelectTextDiv').show();
                }

            });

            $('#svcSelect').change(function(event){
                $('#svcSelectTextDiv').hide();
                if (!event.isTrigger) {
                _shouldPopulateSavedInput = false;
                }

                var svcVal = $('#svcSelect').val();
                if(svcVal==='other')
                {
                    $('#svcSelectTextDiv').show();
                }

            });

            $('#podSelect').change(function(event){
                $('#podSelectTextDiv').hide();
                if (!event.isTrigger) {
                _shouldPopulateSavedInput = false;
                }

                var podVal = $('#podSelect').val();
                if(podVal==='other')
                {
                    $('#podSelectTextDiv').show();
                }

            });

           $('input[name="scriptOption"]').change(function(event) {
                 if (!event.isTrigger) {
                _shouldPopulateSavedInput = false;
                }
                showScriptView();
            });

           $('input[type="checkbox"]').change(function(event) {
                if (!event.isTrigger) {
                _shouldPopulateSavedInput = false;
                }
                document.getElementById(this.value).enabled=this.checked;
                //document.getElementById(this.value).addEventListener("blur",checkVer(this.value));
                       });


            $('#jobName').change(function(event){
                if (!event.isTrigger) {
                _shouldPopulateSavedInput = false;
                }
                refreshForm(this);
            });

            $('#depSelect').change(function(event){
                if (!event.isTrigger) {
                _shouldPopulateSavedInput = false;
                }
                var depVal = $('#depSelect').val();
                if(depVal==='other')
                {
                    $('#depSelectTextDiv').show();
                }
                var jobType= $('#jobName').val();
                if(jobType==='updateImg'){
                    listDepImage(this);

                }else if(jobType==='scaleDp'){
                    $('#numList').show();
                }

            });

            document.getElementById("nsSelectText").addEventListener("blur", nsFunc);

            document.getElementById("endpointSelectText").addEventListener("blur", epFunc);



    };

    function refreshForm(name){
         var jobType = name.value || name.jobName;
         $('#depSelectDiv').hide();
         $('#imageSelectDiv').hide();
         $('#scriptOptionDiv').hide();
         $('#listNamespace').hide();
         $('#imageSelect').empty();
         $('#imageDiv').hide();
         $('#listPods').hide();
         $('#nameSpaceDiv').hide();
         $('#listRs').hide();
         $('#listRc').hide();
         $('#remoteScriptDiv').hide();
         $('#fileDisplay').hide();
         $('#userDefinedScriptDiv').hide();
         $('#listSvc').hide();
         $('#listPvc').hide();
         $('#listPv').hide();
         $('#listsecrets').hide();
         $('#numList').hide();
         $('#depSelectTextDiv').hide();
         $('#nsSelectTextDiv').hide();
         $('#rcSelectTextDiv').hide();
         $('#rsSelectTextDiv').hide();
         $('#pvSelectTextDiv').hide();
         $('#pvcSelectTextDiv').hide();
         $('#secretsSelectTextDiv').hide();
         $('#svcSelectTextDiv').hide();
         $('#podSelectTextDiv').hide();
         $('#endpointSelectTextDiv').hide();

         if(jobType==='updateImg')
         {
              listNs();

         }else if (jobType==='selectJob')
         {
         }else if (jobType==='createNs')
         {
                $('#nameSpaceDiv').show();
                if(_shouldPopulateSavedInput){
                    populateSelectedValue($('#nameSpace'),_inputValues.nameSpaceVal);
                }
         }
         else if (jobType==='deletePod')
         {
            listNs();
         }
         else if (jobType==='deletePv')
         {
            listPv();
         }else if (jobType==='deleteSecrets')
         {
             listNs();
         }
         else if(jobType==='deleteDp')
         {
            listNs();
         }
         else if(jobType==='deleteNs')
         {
            listNs();
         }
         else if(jobType==='deleteRc')
         {
            listNs();
         }
         else if(jobType==='deleteRs')
         {
             listNs();
         }
         else if(jobType==='deleteSvc')
         {
            listNs();
         }
         else if(jobType==='deletePvc')
         {
             listNs();
         }else if(jobType==='scaleRc'){
            listNs();
         }else if(jobType==='scaleDp'){
            listNs();
         }else{
               if(!(jobType==='createPv')){
                    listNs();
                }
           $('#scriptOptionDiv').show();
           if(_shouldPopulateSavedInput){
                   if (VRCS.context.task.inputParameters.values['isUserDefinedScript'] === 'true') {
                                        $('#userDefinedScript').prop('checked', true);
                                        $('#userDefinedScriptDiv').show();
                                        $('#fileDisplay').hide();
                                    }
                   else {
                           $('#remoteScript').prop('checked', true);


                           if(!(VRCS.context.task.inputParameters.values['definitionContent']==='')) {
                             $('#fileDisplay').show();
                             $('pre').html( _inputValues.definitionContent);
                           }
                   }
           }
           else{
                showScriptView();
           }
         }}

    function nsFunc() {
                var jobType= $('#jobName').val();
                                    if(jobType ==="updateImg"){
                                         if($('#endpointSelect').val()==='others'){
                                         errorResponse("Endpoint cant be Others",$('#endpointSelectDiv'))
                                         }else{
                                              populateDeploymentList();
                                         }
                                    }
                                    else if(jobType==="deletePod"){
                                        listPods();
                                    }else if(jobType==="deleteRc"){
                                        listRc();
                                    }else if(jobType==="deleteDp"){
                                        populateDeploymentList();
                                    }else if(jobType==="deleteRs"){
                                        listRs();
                                    }else if(jobType==='deleteSvc'){
                                        listSvc();
                                    }else if(jobType==='deletePvc'){
                                        listPvc();
                                    }else if(jobType==='deleteSecrets'){
                                        listsecrets();
                                    }
                                    else if(jobType==='scaleRc'){
                                        listRc();
                                    }else if(jobType==='scaleRc'){
                                        depSelectDiv();
                                    }

                }

    function epFunc(){
                var endpoint = $('#endpointSelectText').val();
                if (!event.isTrigger) {
                    _shouldPopulateSavedInput = false;
                }
                        $('#jobName').val("selectJob");
                        $('#depSelectDiv').hide();
                        $('#imageSelectDiv').hide();
                        $('#scriptOptionDiv').hide();
                        $('#listNamespace').hide();
                        $('#imageSelect').empty();
                        $('#imageDiv').hide();
                        $('#listPods').hide();
                        $('#nameSpaceDiv').hide();
                        $('#listRs').hide();
                        $('#listRc').hide();
                        $('#remoteScriptDiv').hide();
                        $('#fileDisplay').hide();
                        $('#userDefinedScriptDiv').hide();
                        $('#listSvc').hide();
                        $('#listPvc').hide();
                        $('#listPv').hide();
                        $('#listsecrets').hide();
                        $('#depSelectTextDiv').hide();
                        $('#nsSelectTextDiv').hide();
                        $('#rcSelectTextDiv').hide();
                        $('#rsSelectTextDiv').hide();
                        $('#pvSelectTextDiv').hide();
                        $('#pvcSelectTextDiv').hide();
                        $('#secretsSelectTextDiv').hide();
                        $('#svcSelectTextDiv').hide();
                        $('#podSelectTextDiv').hide();

                       $('#jobConfigMenuDiv').show();
                       if(_shouldPopulateSavedInput){
                            populateSelectedValue($('#jobName'), _inputValues.jobName);


            }

    }

    function errorResponse(message, errField) {
        //switchToErrorTab(errField);
        var response = {};
        response.isError = true;
        response.msg = message;
        return response;
    }

    function switchToErrorTab(errField) {
        var errTab = errField.parents('.tab-pane');
        errTab.addClass('active in');
        errTab.siblings('.tab-pane').removeClass('active in');
        if (errField.parents('.tab-pane')[0].id == 'generalTab') {
            $('#scriptConfigTabs li').eq(0).addClass('active');
            $('#scriptConfigTabs li').eq(1).removeClass('active');
        }   else  {
                $('#scriptConfigTabs li').eq(0).removeClass('active');
                $('#scriptConfigTabs li').eq(1).addClass('active');
            }
        }

    function showScriptView() {
        if ($("#userDefinedScript").is(':checked')) {
            $('#remoteScriptDiv').hide();
            $('#fileDisplay').hide();
            $('#userDefinedScriptDiv').show();
        } else {
            $('#userDefinedScriptDiv').hide();
            $('#remoteScriptDiv').show();

            var fileDisplayArea = document.getElementById('fileDisplayArea');
            var content = VRCS.context.task.inputParameters.values['definitionContent'];
            if(!(content===''))
            {
               fileDisplayArea.innerText = content;
               $('#fileDisplay').show();
            }


        }

    }

    function initCodeEditor() {
        editor = ace.edit('editor');
        //Set editor theme
        editor.setTheme("ace/theme/chrome");
        editor.$blockScrolling = Infinity;

        editorOptions = {
            selector: 'editor',
            /**
             * Use to get the text from editor till the position at which user started autocomplete for variable binding.
             */
            getTextTillCaret: function() {
                var cursor = editor.selection.getCursor();
                var Range = ace.require('ace/range').Range;
                var textRange = new Range(0, 0, cursor.row, cursor.column);
                var caretIndex = editor.session.getTextRange(textRange).lastIndexOf("$");
                var modifiedText = editor.session.getTextRange(textRange).substr(caretIndex);
                return modifiedText;
            },
            /**
             * Use to replace variable bindings selected from dropdown
             * @param text to be replaced from
             * @param str to be replaced with
             */
            replaceText: function(textTofind, textToreplace) {
                var cursor = editor.selection.getCursor();
                var Range = ace.require('ace/range').Range;
                var textRange = new Range(0, 0, cursor.row, cursor.column);
                editor.find(textTofind, {
                    range: textRange,
                    backwards: true,
                    start: cursor
                });
                editor.replace(textToreplace);
                editor.selection.clearSelection();
                editor.focus();
            },
            /**
             * Use to calculate the correct caret position in terms of total characters before cursor.
             */
            getCursorPositionIndex: function() {
                var cursor = editor.selection.getCursor();
                var Range = ace.require('ace/range').Range;
                var textRange = new Range(0, 0, cursor.row, cursor.column);
                var caretIndex = editor.session.getTextRange(textRange).length;
                return caretIndex;
            },
            /**
             * Use to get all text from editor. Just a abstraction of native method so that it can be used in vrcs.
             */
            getAllEditorText: function() {
                return editor.getValue();
            }
        };


        var HashHandler = ace.require("ace/keyboard/hash_handler").HashHandler;
        var keyboardHandler = new HashHandler();
        // parent jquery is in use for autocomplete
        var parent$ = parent.jQuery;
        var keyHandler = function(keyCode) {
            return function(editor) {
                var ee = parent$.Event('keydown');
                ee.which = ee.keyCode = keyCode;
                // since ace editor swallow the arrow event
                // fire the keydown so autocomplete can capture it
                parent$(editor.container).trigger(ee);
                // only if autocomplete menu is active we override the arrow key
                return !!parent$(parent$(editor.container).autocomplete('widget')).is(':visible');
            };
        };

        keyboardHandler.bindKeys({
            'Up': keyHandler(parent$.ui.keyCode.UP),
            'Down': keyHandler(parent$.ui.keyCode.DOWN)
        });

        editor.keyBinding.addKeyboardHandler(keyboardHandler);

        //remove 'XXXX' from last div - appended by ace itself
        $("#editor div").last().html("");
    }

    function updateCodeEditor(lang, content) {
            editor.session.setMode('ace/mode/sh');
            content !== '' ? editor.session.setValue(content) : editor.session.setValue(editor.session.getValue());
        }

    function enableAutoCompleteFields() {
            VRCS.view.attachAutoComplete('#nsSelectText');
            VRCS.view.attachAutoComplete('#endpointSelectText');
            VRCS.view.attachAutoComplete('#pvcSelectText');
            VRCS.view.attachAutoComplete('#pvSelectText');
            VRCS.view.attachAutoComplete('#secretsSelectTextDiv');
            VRCS.view.attachAutoComplete('#rcSelectText');
            VRCS.view.attachAutoComplete('#rsSelectText');
            VRCS.view.attachAutoComplete('#depSelectText');
            VRCS.view.attachAutoComplete('#podSelectText');
            VRCS.view.attachAutoComplete('#svcSelectText');
            VRCS.view.attachAutoComplete('#editor', editorOptions);
        }

    return {
        init: function(context,successFn,failFn) {
            initCodeEditor();
            updateCodeEditor(scriptEditorLang, scriptEditorContent);
            enableAutoCompleteFields();
            $('#jobConfigMenuDiv').hide();
            $('#depSelectDiv').hide();
            $('#imageSelectDiv').hide();
            $('#scriptOptionDiv').hide();
            $('#listNamespace').hide();
            $('#imageSelect').empty();
            $('#imageDiv').hide();
            $('#listPods').hide();
            $('#nameSpaceDiv').hide();
            $('#listRs').hide();
            $('#listRc').hide();
            $('#remoteScriptDiv').hide();
            $('#fileDisplay').hide();
            $('#userDefinedScriptDiv').hide();
            $('#listSvc').hide();
            $('#listPvc').hide();
            $('#listPv').hide();
            $('#listsecrets').hide();
            $('#numList').hide();
            $('#depSelectTextDiv').hide();
            $('#nsSelectTextDiv').hide();
            $('#endpointSelectTextDiv').hide();
            $('#rcSelectTextDiv').hide();
            $('#rsSelectTextDiv').hide();
            $('#pvSelectTextDiv').hide();
            $('#secretsSelectTextDiv').hide();
            $('#pvcSelectTextDiv').hide();
            $('#svcSelectTextDiv').hide();
            $('#podSelectTextDiv').hide();

            if (!context || !context.task || $.isEmptyObject(context.task)) {
                VRCS.log.error('Required context not available', context);
                failFn('Required context not available');
                return;
            }
             _inputValues = context.task.inputParameters.values;

            if ($.isEmptyObject(_inputValues)) {
                failFn('Input parameters not available.');
                return;
            }

            _shouldPopulateSavedInput = !!(_inputValues.kubernetesMaster && _inputValues.jobName);

            if(!(context.readOnly)){
                VRCS.io.queryRacks({
                     dataType: 'vrcs.kubernetes:KubernetesMaster',
                     complete: function(servers) {
                     registerEventListeners(context);
                     var endpoints = servers.map(function(server) {
                     // for matching UUID
                        var re = /@[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/;
                        var serverName = server.name;
                        var serverExpression = server.reference.replace(re, '@' + serverName + '#' + serverName);
                        VRCS.log.debug('serverExpression: ' + serverExpression);
                           return {
                              text: serverName,
                              value: serverExpression
                           };
                     });

                     $('#endpointSelect').html(getEndpointOptions(endpoints));
                     // Check if the persisted value still match the remote data
                    if (_shouldPopulateSavedInput) {

                            populateSelectedValue($('#endpointSelect'), _inputValues.kubernetesMaster);
                            }
                     successFn();

                     },
                     fail: function(error) {
                         VRCS.log.error(error);
                         failFn('Failed to retrieve kubernetes Server.');
                     }
                });
            }
            else{
                 registerEventListeners(context);
                 $('input, select, textarea').prop('disabled', true);
                 var outputParameters = context.task.outputParameters.schema;
                 var epServer = _inputValues['kubernetesMaster'];
                 var epName = epServer ? getEndpointNameFromExpression(epServer) : '';
                 var epOption = {text: epName, value: epServer};
                 $('#endpointSelect').html(getEndpointOptions(epOption));
                 populateSelectedValue($('#endpointSelect'), _inputValues.kubernetesMaster);
                 populateSelectedValue($('#jobName'), _inputValues.jobName);
            }



            var i=0;

            function readSingleFile(evt) {


                //Retrieve the first (and only!) File from the FileList object
                var f = evt.target.files[0];
                inputParam['definitionName'] = f.name;
                var fileDisplayArea = document.getElementById('fileDisplayArea');
                if (f) {
                  var r = new FileReader();
                  r.onload = function(evt) {
            	   var contents = evt.target.result;
                   inputParam['definitionContent'] = contents;
                   fileDisplayArea.innerText = evt.target.result;


                  }
                  r.readAsText(f);
                  $('#fileDisplay').show();
                } else {
                  alert("Failed to load file");
                }

              }


            document.getElementById('definitionName').addEventListener('change', readSingleFile, false);
        },
        validate: function(context,completeFn,failFn) {
            var response = {};
            // clear previous errors
            $('#kubernetesConfigContainer *').removeClass('mandatory-failed');
            //Validate user defined script / remote script file configuration
            var isUserDefinedScript = $("#userDefinedScript").is(':checked');
            var definitionContent = editor.getValue();
            var scriptNameField = $('#definitionName');
            var $endpointSelect = $('#endpointSelect');
            var $jobSelect = $('#jobName');

            $endpointSelect.removeClass('mandatory-failed');
            $jobSelect.removeClass('mandatory-failed');

            var endpointVal = $('#endpointSelect').val();
            var jobVal = $('#jobName').val();
            if (!endpointVal) {
                $endpointSelect.addClass('mandatory-failed');
                failFn('Endpoint not selected.');
                return;
            }
            if (!jobVal) {
                $jobSelect.addClass('mandatory-failed');
                failFn('Job not selected.');
                return;
            }
            var jobType = $('#jobName').val();
            if(jobType ==='createDp' || jobType ==='createRc'|| jobType==='createRs'|| jobType==='createPvc'|| jobType==='createPv'|| jobType==='createSvc'|| jobType==='createPod'|| jobType==='createSecrets'){
                if (isUserDefinedScript && ValidationUtil.isEmptyString(definitionContent)) {
                    $("#editor").addClass('mandatory-failed');
                    return errorResponse('Please specify definition content.', $("#editor"));
                } else if (!isUserDefinedScript && ValidationUtil.isEmptyString(scriptNameField.val())&&!_shouldPopulateSavedInput){
                    scriptNameField.addClass('mandatory-failed');
                    return errorResponse('Please specify file name.', scriptNameField);
                }
            }
            return response;
        },
        getInputParameters: function() {

            var isUserDefinedScript = $("#userDefinedScript").is(':checked');
            if (isUserDefinedScript) {
                /*$("#definitionName").val('');*/
                inputParam['definitionName'] = '';
                inputParam['definitionContent'] = editor.getValue();
            } else {
                editor.setValue('');
            }
            if (Kubernetes.config.schemaHasProperty("isUserDefinedScript")) {
                inputParam['isUserDefinedScript'] = isUserDefinedScript;
            }

            var job = $('#jobName').find('option:selected').val();
            inputParam['jobName'] = job;

            inputParam['nameSpaceVal'] = $('#nameSpace').val();
            var val = $('#endpointSelect').val();
            if(val==='other'){
                              val = $('#endpointSelectText').val();
            }
            inputParam['kubernetesMaster'] = val;


            var val = $('#nsSelect').val();
            if(val==='other'){
                  val = $('#nsSelectText').val();
            }
            inputParam['nameSpaceOpt'] = val;

            if(job!='deleteDp'){
                val = $('#depSelect').val();
                if(val==='other'){
                      val = $('#depSelectText').val();
                }
                inputParam['depName'] = val;
            }

            inputParam['replicas'] = $('#replica').val();

            var listChecked = [].slice.apply(document.querySelectorAll("input[type=checkbox]"))
                                         .filter(function(c){ return c.checked; })
                                         .map(function(c){ return c.value; });
            var imageVersion = [].slice.apply(document.querySelectorAll("input[name=version]"))
                                                                        .map(function(c){ if(c.value="") return c.value; });

            var imageVer = [];



            for(var i=0; i<listChecked.length; i++){
                var askedVer = document.getElementById(listChecked[0]).value;
                imageVer.push(askedVer);
            }
            var jobType = $('#jobName').val();
            inputParam['imagesName']=listChecked;
            inputParam['imagesReqVer'] = imageVer;

            if(jobType==='deletePod'){
                var val = $('#podSelect').val();
                if(val==='other'){
                    val = $('#podSelectText').val();
                }
                inputParam['deleteParam'] = val;
            }else if(jobType==='deleteDp'){
                var val = $('#depSelect').val();
                if(val==='other'){
                    val = $('#depSelectText').val();
                }
                inputParam['deleteParam'] = val;
            }else if(jobType==='deleteNs'){
                var val = $('#nsSelect').val();
                if(val==='other'){
                    val = $('#nsSelectText').val();
                }
                inputParam['deleteParam'] = val;
            }else if(jobType==='deleteRc'){
                var val = $('#rcSelect').val();
                if(val==='other'){
                    val = $('#rcSelectText').val();
                }
                inputParam['deleteParam'] = val;
            }else if(jobType==='deleteRs'){
                var val = $('#rsSelect').val();
                if(val==='other'){
                    val = $('#rsSelectText').val();
                }
                inputParam['deleteParam'] = val;
            }else if(jobType==='deleteSvc'){
                var val = $('#svcSelect').val();
                if(val==='other'){
                    val = $('#svcSelectText').val();
                }
                inputParam['deleteParam'] = val;
            }else if(jobType==='deletePvc'){
                var val = $('#pvcSelect').val();
                if(val==='other'){
                    val = $('#pvcSelectText').val();
                }
                inputParam['deleteParam'] = val;
            }else if(jobType==='deleteSecrets'){
                var val = $('#secretsSelect').val();
                if(val==='other'){
                   val = $('#secretsSelectText').val();
                }
                inputParam['deleteParam'] = val;
            }
            else if(jobType==='deletePv'){
                var val = $('#pvSelect').val();
                if(val==='other'){
                    val = $('#pvSelectText').val();
                }
                inputParam['deleteParam'] = val;
            }else if(jobType==='scaleRc'){
                var val = $('#rcSelect').val();
                if(val==='other'){
                    val = $('#rcSelectText').val();
                }
                inputParam['depName'] = val;
            }else if(jobType==='scaleDp'){
                inputParam['depName'] = $('#depSelect').val();
            }

            return inputParam;
        },
        schemaHasProperty: function(propertyName) {
            var schema = VRCS.context.task.inputParameters.schema;
            for (var i = 0; i < schema.length; i++) {
                if (schema[i].name === propertyName && schema[i].additional === false) {
                    return true;
                }
            }
            return false;
        },
        getOutputParameters: function() {
            var outputParam = {};

            $('#scriptOutputPanel .prop-row').each(function(){
                var paramName = $(this).find('.col-name').text();

                if (paramName) {
                    outputParam[paramName] = true;
                }
            });

            return outputParam;
        }
    };
})();

VRCS.view.config({
    onload: function(options) {
        Kubernetes.config.init(options.context,options.complete,options.fail);
        options.complete();
    },
    onsave: function(options) {
        var response = Kubernetes.config.validate(options.context,options.complete,options.fail);
        if (!response.isError) {
            options.complete({
                inputParameters: {
                    values: Kubernetes.config.getInputParameters(),
                    //additionalSchema: Kubernetes.config.getAdditionalInputParameters()
                },
                outputParameters: {
                    values: Kubernetes.config.getOutputParameters()
                }
            });
        } else {
            options.fail(response.msg);
        }
    }
});