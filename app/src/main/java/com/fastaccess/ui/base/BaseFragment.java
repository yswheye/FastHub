package com.fastaccess.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fastaccess.data.dao.LoginModel;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import net.grandcentrix.thirtyinch.TiFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;

/**
 * Created by Kosh on 27 May 2016, 7:54 PM
 */

public abstract class BaseFragment<V extends BaseMvp.FAView, P extends BasePresenter<V>> extends TiFragment<P, V> implements BaseMvp.FAView {

    protected BaseMvp.FAView callback;

    @Nullable private Unbinder unbinder;

    @LayoutRes protected abstract int fragmentLayout();

    protected abstract void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState);

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseMvp.FAView) {
            callback = (BaseMvp.FAView) context;
        }
    }

    @Override public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentLayout() != 0) {
            View view = inflater.inflate(fragmentLayout(), container, false);
            unbinder = ButterKnife.bind(this, view);
            return view;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (LoginModel.getUser() != null) {
            onFragmentCreated(view, savedInstanceState);
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) unbinder.unbind();
    }

    @Override public void showProgress(@StringRes int resId) {
        callback.showProgress(resId);
    }

    @Override public void hideProgress() {
        callback.hideProgress();
    }

    @Override public void showMessage(@StringRes int titleRes, @StringRes int msgRes) {
        callback.showMessage(titleRes, msgRes);
    }

    @Override public void showMessage(@NonNull String titleRes, @NonNull String msgRes) {
        callback.showMessage(titleRes, msgRes);
    }

    @Override public void showErrorMessage(@NonNull String msgRes) {
        callback.showErrorMessage(msgRes);
    }

    @Override public boolean isLoggedIn() {
        return callback.isLoggedIn();
    }

    @Override public void onRequireLogin() {
        callback.onRequireLogin();
    }

    @Override public void onMessageDialogActionClicked(boolean isOk, @Nullable Bundle bundle) {

    }

    @Override public void onDialogDismissed() {

    }

    protected boolean isSafe() {
        return getView() != null && getActivity() != null && !getActivity().isFinishing();
    }
}
