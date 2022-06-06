package net.ashald.envfile.providers.k8s;


import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YamlTool {
    public static final String NAME = "name";
    public static final String VALUE = "value";
    private static final String envPath = "spec.template.spec.containers.env";
    Map<String, Object> properties = new HashMap<>();


    public YamlTool() {
    }

    public YamlTool(String filePath) {
        initWithString(filePath);
    }


    public Map<String, String> getEnvList() {
        Map<String, String> resultList = new HashMap<>();
        if( !properties.isEmpty()) {
            ArrayList<Map<String, String>> envList = getValueByKey(envPath, new ArrayList<Map<String, String>>());
            envList.stream().forEach(e -> resultList.put(e.get(NAME), e.get(VALUE)));
        }
        return resultList;
    }

    public void initWithString(String filePath) {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Iterable<Object> syncState = new Yaml().loadAll(inputStream);
        for (Object o : syncState) {
            if (o instanceof Map) {
                Map<String, Object> s = (Map) o;
                properties.putAll(s);
            }
        }
    }

    public void initWithStream(InputStream stream) {
        Iterable<Object> syncState = new Yaml().loadAll(stream);
        for (Object o : syncState) {
            if (o instanceof Map) {
                Map<String, Object> s = (Map) o;
                properties.putAll(s);
            }
        }
    }




    public <T> T getValueByKey(String key, T defaultValue) {
        String separator = ".";
        String[] separatorKeys = null;
        if (key.contains(separator)) {
            separatorKeys = key.split("\\.");
        } else {
            Object res = properties.get(key);
            return res == null ? defaultValue : (T) res;
        }
        String finalValue = null;
        Object tempObject = properties;
        for (int i = 0; i < separatorKeys.length; i++) {
            String innerKey = separatorKeys[i];
            Integer index = null;
            if (innerKey.contains("[")) {
                index = Integer.valueOf(StringUtils.substringsBetween(innerKey, "[", "]")[0]);
                innerKey = innerKey.substring(0, innerKey.indexOf("["));
            }
            Map<String, Object> mapTempObj = (Map) tempObject;
            Object object = mapTempObj.get(innerKey);
            if (object == null) {
                return defaultValue;
            }
            Object targetObj = object;
            if (index != null) {
                targetObj = ((ArrayList) object).get(index);
            }
            tempObject = targetObj;
            if (i == separatorKeys.length - 1) {
                return (T) targetObj;
            }

        }
        return null;
    }
}
