package com.wpx.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 视图
 */
public class ViewObject {
    private Map<String,Object> objs = new HashMap<>();
    public void set(String key,Object value){
        objs.put(key,value);
    }
    public Object get(String key){
        return objs.get(key);
    }
}
