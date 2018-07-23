package com.test.myretrofit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {
    public static final String API_URL = "https://api.github.com";
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn= findViewById(R.id.btn);
        Button btn2= findViewById(R.id.btn2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rx();
            }
        });
    }

    //retrofit 默认的使用方式
    private void custom() {
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .build();

        //动态生成一个代理对象
        Api github = retrofit.create(Api.class);

        //生成一个OKHttpCall的代理对象
        Call<ResponseBody> call = github.contributors("square", "retrofit");

        //返回结果
        //Response<ResponseBody> response = call.execute();//同步,android里此处不能运行，需要在单独的线程
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //打印数据
                try {
                    Log.d(TAG,response.body().string());
                }catch (Exception e){
                    Log.e(TAG,e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    //rxjava2的使用方式
    private void rx(){
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())//可选
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//可选
                .build();

        //动态生成一个代理对象
        Api github = retrofit.create(Api.class);

        //生成一个OKHttpCall的代理对象
        Observable<List<ResultBean>> call = github.contributorsRx("square", "retrofit");

        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<ResultBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG,String.valueOf(d.isDisposed()));
            }

            @Override
            public void onNext(List<ResultBean> response) {
                Log.d(TAG,response.toArray().toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"onComplete");
            }
        });
    }
}
