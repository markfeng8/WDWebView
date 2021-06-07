package com.wdweblib.ui.mediarecorder;

interface MediaRecorderView {
    void 录像前();

    void 录像中不可停止();

    void 录像中可停止();

    void 录像停止(String tip);

    void showContent(String content);

    void finishWithResult(String base64);
}
