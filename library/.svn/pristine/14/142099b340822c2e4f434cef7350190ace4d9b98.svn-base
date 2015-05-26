package com.sunny.net;

/**
 * Created by ning.dai on 14-9-22.
 */
public interface Callback<Result> {

    void onFinish(Result result);

    void onError();

    public interface ProgressCallback<Result> extends Callback<Result>{

        void onProgress(int percent);
    }

    public interface PreprocessCallback<Result> extends Callback<Result>{
        void onPreprocess(byte[] data);
    }
}
