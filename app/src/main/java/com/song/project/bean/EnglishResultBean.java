package com.song.project.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EnglishResultBean {
    public String name;
    public String result;
    public String dst;
    public static List<EnglishResultBean> parsearray(JSONArray array){
        List<EnglishResultBean> resultBeans =  new ArrayList<>();
        if(array != null){
            for(int i = 0; i<array.length();i++){
                try {
                    JSONObject ob = (JSONObject) array.get(i);
                    resultBeans.add(prase(ob));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultBeans;
    }
    public static EnglishResultBean prase(JSONObject object){
        EnglishResultBean plantBean = new EnglishResultBean();
        try {
            if(object.getString("dst") != null){
                plantBean.dst = object.getString("dst");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return plantBean;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }
}
