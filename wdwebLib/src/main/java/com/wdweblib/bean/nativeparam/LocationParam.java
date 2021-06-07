package com.wdweblib.bean.nativeparam;

import com.wdweblib.bean.NativeBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-03 11:08
 */
public class LocationParam extends NativeBean<LocationParam> {

    private String coordinate;

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String backJsonStr(String lng, String lat) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("longitude", lng);
            jsonObject.put("latitude", lat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
