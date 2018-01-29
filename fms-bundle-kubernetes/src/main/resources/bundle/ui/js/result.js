/*
 * Copyright (c) 2017 VMware, Inc. All Rights Reserved.
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
