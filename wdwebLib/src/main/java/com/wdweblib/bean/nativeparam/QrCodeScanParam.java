package com.wdweblib.bean.nativeparam;

import com.wdweblib.bean.NativeBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-02 15:21
 */
public class QrCodeScanParam extends NativeBean<QrCodeScanParam> {

    private String needResult;

    public String getNeedResult() {
        return needResult;
    }

    public void setNeedResult(String needResult) {
        this.needResult = needResult;
    }

    public  String backJsonStr(String scanResult) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("qrcode", scanResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
