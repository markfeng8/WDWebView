package com.wdweblib.utils;

import android.content.Context;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.wdweblib.asr.RecogResult;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-08 10:47
 */
public class BDAsrManger {

    private EventManager asr;

    private BDAsrManger() {
    }

    private static class BDAsrMangerInstance {
        private static final BDAsrManger INSTANCE = new BDAsrManger();
    }

    public static BDAsrManger getInstance() {
        return BDAsrManger.BDAsrMangerInstance.INSTANCE;
    }

    public BDAsrManger init(Context context) {
        // this是Activity或其它Context类
        asr = EventManagerFactory.create(context, "asr");
        asr.registerListener(eventListener);
        return getInstance();
    }

    public void start() {
        String json = "{\"accept-audio-data\":false,\"disable-punctuation\":false,\"accept-audio-volume\":true,\"pid\":1736}";
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
    }

    public void stop() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
        //发送停止录音事件，提前结束录音等待识别结果
    }

    public void cancel() {
        asr.send(SpeechConstant.ASR_CANCEL, null, null, 0, 0);
        //取消本次识别，取消后将立即停止不会返回识别结果
    }

    public void unregisterListener() {
        asr.unregisterListener(eventListener);
    }

    private static EventListener eventListener = new EventListener() {
        @Override
        public void onEvent(String name, String params, byte[] data, int offset, int length) {
            if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
                // 引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了
            }
            if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
                // 一句话的临时结果，最终结果及语义结果
                RecogResult recogResult = RecogResult.parseJson(params);
                // 识别结果
                String[] results = recogResult.getResultsRecognition();
                if (recogResult.isFinalResult()) {
                    // 最终识别结果，长语音每一句话会回调一次
                    String str = null;
                    for (String result : results) {
                        str += result;
                    }
                } else if (recogResult.isPartialResult()) {
                    // 临时识别结果

                } else if (recogResult.isNluResult()) {
                    // 语义理解结果
                }
            }
            // ... 支持的输出事件和事件支持的事件参数见“输入和输出参数”一节
        }
    };
}
