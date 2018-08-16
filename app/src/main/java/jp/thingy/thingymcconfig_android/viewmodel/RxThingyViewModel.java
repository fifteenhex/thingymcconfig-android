package jp.thingy.thingymcconfig_android.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.support.v4.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.subjects.PublishSubject;
import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig_android.ThingyMcConfigApplication;

public class RxThingyViewModel extends ViewModel
        implements SwipeRefreshLayout.OnRefreshListener {

    protected final PublishSubject<Integer> refresh = PublishSubject.create();
    private final ArrayList<Disposable> disposables = new ArrayList<>();
    private final ArrayList<PublishSubject<?>> publishSubjects = new ArrayList<>();
    public ObservableBoolean refreshing = new ObservableBoolean(true);

    @Inject
    protected RxThingyMcConfig thingyMcConfig;

    protected RxThingyViewModel() {
        ThingyMcConfigApplication.getComponent().inject(this);
        registerPublishSubject(refresh);
    }

    protected void handleMaskableExceptions(Throwable t) {
        if (t instanceof IOException)
            t.printStackTrace();
        else
            Exceptions.propagate(t);
    }

    protected void registerDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    protected void registerPublishSubject(PublishSubject subject) {
        publishSubjects.add(subject);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        for (PublishSubject ps : publishSubjects)
            ps.onComplete();
        for (Disposable d : disposables)
            d.dispose();
    }

    @Override
    public void onRefresh() {
        refresh.onNext(-2);
    }
}
