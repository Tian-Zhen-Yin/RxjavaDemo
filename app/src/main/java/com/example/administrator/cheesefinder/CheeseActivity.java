package com.example.administrator.cheesefinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class CheeseActivity extends BaseSearchActivity {
    //用于解除订阅
    private Disposable mDisposable;

    //在onStart方法中，把subscribe（）的方法返回值赋给mDisposable
    @Override
    protected void onStart() {
        super.onStart();
//创建被观察者
        Observable<String>buttonClickStream=createButtonClickObservable();
        Observable<String>textChangeStream=createTextChangeObservable();
        //将两个对象合二为一
        Observable<String>searchTextObservable=Observable.merge(buttonClickStream,textChangeStream);

        mDisposable= (Disposable) searchTextObservable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                showProgressBar();

            }
        })
                .observeOn(Schedulers.io())
                .map(new Function<String, List<String>>() {
                    @Override
                    public List<String> apply(String s) throws Exception {
                        return mCheeseSearchEngine.search(s);

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        hideProgressBar();
                        showResult(strings);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!mDisposable.isDisposed())
        {
            mDisposable.dispose();
        }
    }

    private Observable<String> createTextChangeObservable() {

        Observable<String> textChangeObsercvable=Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                final TextWatcher watcher=new TextWatcher()
                {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    emitter.onNext(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
                mQueryEdiText.addTextChangedListener(watcher);

                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        mQueryEdiText.removeTextChangedListener(watcher);
                    }
                });

            }
        });
        return textChangeObsercvable
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String query) throws Exception {
                        return query.length()>=2;

                    }
                }).debounce(1000,TimeUnit.MILLISECONDS);//防止抖动


    }

    //定义一个方法返回一个Observsable，泛型是String类型
    private Observable<String> createButtonClickObservable() {
   return Observable.create(new ObservableOnSubscribe<String>() {
       @Override
       public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
       //给搜索按钮添加一个点击事件
           mSearchButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view)
           {//当点击事件触发后，调用emitter的onNext方法，传递当前的mQueryEditText的值

               emitter.onNext(mQueryEdiText.getText().toString());
           }
       });
           emitter.setCancellable(new Cancellable() {
               @Override
               public void cancel() throws Exception {
                   //通过setOnclickListener(null)来解除监听
                   mSearchButton.setOnClickListener(null);
               }
           });
       }
   });
    }

}

