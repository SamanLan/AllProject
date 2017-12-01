package com.lansman.allproject;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.nekocode.rxlifecycle.LifecyclePublisher;
import cn.nekocode.rxlifecycle.RxLifecycle;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.schedulers.NewThreadScheduler;
import io.reactivex.internal.subscriptions.ArrayCompositeSubscription;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;
import io.reactivex.subjects.CompletableSubject;

import static com.jakewharton.rxbinding2.widget.RxTextView.textChanges;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        deal();
//        button();
//        web();
//        testConcat();
//        pay();
        merge();
    }

    private void web() {
        WebView mWebView = (WebView) findViewById(R.id.web);
        final WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
//        settings.setUseWideViewPort(true);
//        settings.setDomStorageEnabled(true);
//        if (Util.isNetworkConnected(context)) {
//            //根据cache-control决定是否从网络上取数据。
//            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        } else {
//            //没网，则从本地获取，即离线加载
//            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        }
//        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
//            new Object() {
//                public void setLoadWithOverviewMode(boolean overview) {
//                    settings.setLoadWithOverviewMode(overview);
//                }
//            }.setLoadWithOverviewMode(true);
//        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Toast.makeText(MainActivity.this, "拦截", Toast.LENGTH_SHORT).show();
                view.loadUrl("https://www.baidu.com");
                MainActivity.this.startActivity(new Intent(MainActivity.this,Main2Activity.class));
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        mWebView.loadUrl("https://lhl.linghit.com/h5/collection/zhuanye/index.html?channel=fx_cn");
    }

    private void deal() {
        Observable<String> observable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(new NewThreadScheduler())
                .map(new Function<Object, Integer>() {
                    @Override
                    public Integer apply(@NonNull Object o) throws Exception {
                        return o.toString().length();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(@NonNull Integer integer) throws Exception {
                        return "第" + integer + "项";
                    }
                }).compose(RxLifecycle.bind(this).<String>withObservable());
        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
//                compositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull String o) {
                System.out.println("观察在" + Thread.currentThread().getName());
                System.out.println(o);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("完成");
            }
        });

    }

    private void simple() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                System.out.println("工作空间。我发送的是string类型数据");
            }
        }).observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("已连接观察通道");
                    }

                    @Override
                    public void onNext(@NonNull String o) {
                        System.out.println("接受工作空间发送的事件，接受到的是int类型");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("出错");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("完成");
                    }
                });
    }

    private void button() {
        Observable<CharSequence> observableName = RxTextView.textChanges(((TextView) findViewById(R.id.text)));
        Observable<CharSequence> observablePwd = RxTextView.textChanges(((TextView) findViewById(R.id.text)));
        Observable<Boolean> observableEnd = Observable.combineLatest(observableName, observablePwd, new BiFunction<CharSequence,CharSequence,Boolean>() {

            @Override
            public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2) throws Exception {
                return charSequence.length() > 0 && charSequence2.length() > 0;
            }
        });
//
//        observable.subscribe(new Observer<CharSequence>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//                System.out.println("加入");
//                compositeDisposable.add(d);
//            }
//
//            @Override
//            public void onNext(@NonNull CharSequence charSequence) {
//                System.out.println(charSequence);
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            System.out.println("移除");
            compositeDisposable.dispose();
        }
    }

    private boolean hasCache;

    private void getData() {
        Observable.concat(getFromCache(),getFromNet()).first(1).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Integer integer) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private Observable<Integer> getFromCache() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (hasCache) {
                    e.onNext(0);
                }
                e.onComplete();
            }
        });
    }

    private Observable<Integer> getFromNet() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (!hasCache) {
                    Thread.sleep(3000);
                    e.onNext(1);
                }
                e.onComplete();
            }
        });
    }

    private void pay() {
        Observable.concat(getGoods(), getComment()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        System.out.println(o.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<Object> getGoods() {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                // 获取商品数据
                System.out.println("获取商品数据中");
                Thread.sleep(3000);
                e.onNext("我是商品数据");
                e.onComplete();
            }
        });
    }

    private Observable<Object> getComment() {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                // 获取商品评论
                System.out.println("获取商品评论中");
                Thread.sleep(3000);
                e.onNext("我是商品评论");
                e.onComplete();
            }
        });
    }

    private void filter() {
        Observable.fromArray("123","145",null,"3456").filter(new Predicate<String>() {
            @Override
            public boolean test(@NonNull String s) throws Exception {
                // 这里会过滤掉null值已经不是1开头的字符串
                return !TextUtils.isEmpty(s) && s.startsWith("1");
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void merge() {
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("666");
                e.onComplete();
            }
        });
        Observable<String> observable2 = Observable.fromArray(false,true).map(new Function<Boolean, String>() {
            @Override
            public String apply(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    if (true) {
                        System.out.println("执行路径在" + Thread.currentThread().getName());
                        return "路径";
                    } else {
                        System.out.println("保存出错在" + Thread.currentThread().getName());
                        return "保存出错";
                    }
                } else {
                    System.out.println("没有权限在" + Thread.currentThread().getName());
                    return "没有权限";
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        observable2.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println("----在" + Thread.currentThread().getName());
                System.out.println(s + "-----");
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("完成-----");
            }
        });
    }
}
