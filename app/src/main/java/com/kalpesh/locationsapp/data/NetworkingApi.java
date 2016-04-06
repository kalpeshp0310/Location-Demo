package com.kalpesh.locationsapp.data;

import android.util.Log;

import com.kalpesh.locationsapp.BaseApplication;
import com.kalpesh.locationsapp.BuildConfig;

import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class NetworkingApi {


    public static Observable<Response> get(final String url) {
        return Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                if (BuildConfig.DEBUG) Log.e("url", url);
                Request request = new Request.Builder().url(url).get().build();
                try {
                    Response response = BaseApplication.getInstance().getOkHttpClient().newCall(request).execute();
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
