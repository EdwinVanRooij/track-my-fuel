package io.github.edwinvanrooij.trackmyfuel.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.edwinvanrooij.trackmyfuel.Record;

/**
 * Author: eddy
 * Date: 21-1-17.
 */

public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    protected MainActivity mainActivity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        mainActivity = ((MainActivity) getActivity());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    public void onContainterActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(mainActivity, "onContainterActivityResult not implemented", Toast.LENGTH_SHORT).show();
    }
}
