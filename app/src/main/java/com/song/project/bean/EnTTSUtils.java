package com.song.project.bean;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;

public class EnTTSUtils {
    private SpeechSynthesizer mTts;
    private String TAG = "ttsutil.calss";
    public EnTTSUtils(){

    }
    public EnTTSUtils(Context context){
        init(context);

    }
    public void init(Context context){
        mTts = SpeechSynthesizer.createSynthesizer(context,mTtsInitListener);
    }
    private InitListener mTtsInitListener = new InitListener() {

        @Override
        public void onInit(int i) {
            Log.i(TAG, "onInit: "+i);
            if(i == ErrorCode.SUCCESS){
                Log.i(TAG, "onInit: "+"初始化成功");
                setParam();
            }
            else{
                Log.i(TAG, "onInit: "+i);
            }
        }

    };
    /**
     * 参数设置
     * @return
     */
    private void setParam(){
        //直接在线合成
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        mTts.setParameter(SpeechConstant.LANGUAGE,"en_us");
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "aisjinger");
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "40");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH,"50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "50");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.pcm");
    }
    public void play(Context context,String text){
        FlowerCollector.onEvent(context, "tts_play");
        setParam();
        int code = mTts.startSpeaking(text,mTtsListener);
        if(code != 0){
            Log.i(TAG, "play: "+"失败原因"+code);
        }
        else{
            Log.i(TAG, "play: "+"成功");
        }
    }
    public void onResume(Context context){
        FlowerCollector.onResume(context);
        FlowerCollector.onPageStart(TAG);
        System.out.println();

    }
    public void onStop(){
        mTts.stopSpeaking();
        System.out.println("关闭播放！！！");

    }
    public void pause(Context context){
        //移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(context);
    }
    public void cancel(){

    }
    public void destory(){
        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {

        }

        @Override
        public void onEvent(int eventType, int i1, int i2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}

            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
                Log.e("MscSpeechLog", "buf is =" + buf);
            }
        }
    };
}
