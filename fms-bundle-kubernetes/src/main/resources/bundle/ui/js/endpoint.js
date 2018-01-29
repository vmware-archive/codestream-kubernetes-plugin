/*
 * Copyright (c) 2017 VMware, Inc. All Rights Reserved.
 */

VRCS.view.config(VRCS.view.alpaca.createConfig({
    alpacaForm: {
        schema: {
            title: VRCS.view.alpaca.getTitleWithHelpIcon('Endpoint Properties', 'vrcs_012'),
            type: 'object',
            properties: {
                // inputParameters go here
                url: {
                    title: 'Url',
                    required: true
                },
                username: {
                     title: 'Username'
                     },
                password: {
                     title: 'Password'
                     }

            }
        },
        options: {
            fields: {
                url: {
                    type: 'url',
                    allowIntranet: true,
                    placeholder: 'eg: protocol://host:port'
                },
                username: {
                    placeholder: 'username'
                },
                password: {
                    type: 'password',
                    placeholder: 'password'
                }
            }
        },
        postRender: function(control) {}
    }

}));
