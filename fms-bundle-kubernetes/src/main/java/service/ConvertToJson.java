package service;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
/*
 * Copyright (c) 2017-2018 VMware, Inc. All Rights Reserved.
 */
public class ConvertToJson {
 public static String convertToJson(String yamlString) {
        Yaml yaml= new Yaml();
        Map<String,Object> map= (Map<String, Object>) yaml.load(yamlString);
        JSONObject jsonObject=new JSONObject(map);
        return jsonObject.toString();
    }
}
