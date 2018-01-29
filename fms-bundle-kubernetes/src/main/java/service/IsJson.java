package service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/*
 * Copyright (c) 2017-2018 VMware, Inc. All Rights Reserved.
 */
public class IsJson {
    public static boolean isJSONValid(String jsonInString ) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
