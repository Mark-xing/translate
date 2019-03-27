package com.song.project.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlantBean {
    public String name;
    public String result;
    public String log_id;
    public static List<PlantBean> parsearray(JSONArray array){
        List<PlantBean> resultBeans =  new ArrayList<>();
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
    public static PlantBean prase(JSONObject object){
        PlantBean plantBean = new PlantBean();
        try {
          if(object.getString("name") != null){
                plantBean.name = object.getString("name");
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

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }
}
