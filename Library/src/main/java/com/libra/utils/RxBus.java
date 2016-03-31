package com.libra.utils;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by libra on 16/3/30 下午4:40.
 */
public class RxBus {
    private final Subject bus;


    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }


    // 提供了一个新的事件
    public void post(Object o) {
        bus.onNext(o);
    }


    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Observable<T> toObserverable(final Class<T> eventType) {
        return bus.filter(new Func1<Object, Boolean>() {
            @Override public Boolean call(Object o) {
                return eventType.isInstance(o);
            }
        }).cast(eventType);
    }


    private static class SingletonHolder {
        private static final RxBus INSTANCE = new RxBus();
    }


    public static RxBus getDefault() {
        return SingletonHolder.INSTANCE;
    }
}
