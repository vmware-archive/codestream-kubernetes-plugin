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

Kubernetes.result = (function() {
    return {
        addFields: function(form, prefix, parameters, readOnly) {
            $.each(parameters, function(index, parameter) {
                var field = prefix + parameter.name;
                var $label = $('<label/>', {
                    for: field,
                    class: 'col-xs-2 control-label mandatory-before'
                }).text(parameter.description ? parameter.description : parameter.name);

                var $input = $('<input/>', {
                    type: 'text',
                    id: field,
                    name: parameter.name,
                    value: parameter.value,
                    class: 'autocomplete form-control input-sm'
                });

                $input.prop('readonly', readOnly);

                var $inputGroup = $('<div/>', {
                    class: 'col-xs-6'
                }).append($input);

                var $formGroup = $('<div/>', {
                    class: 'form-group'
                });

                form.append(($formGroup).append($label).append($inputGroup));
            });
        }
    };
})();

VRCS.view.config({
    onload: function(options) {
        Kubernetes.result.addFields($('#form-input'), 'field_input_', options.context.task.inputParameters.schema, true);
        Kubernetes.result.addFields($('#form-output'), 'field_output_', options.context.task.outputParameters.schema, true);
        options.complete();
    }
});
