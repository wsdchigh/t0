package com.wsdc.g_a_0.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsdc.g_a_0.Starter;
import com.wsdc.g_a_0.annotation.AWrap;
import com.wsdc.g_a_0.annotation.EWrapType;
import com.wsdc.g_a_0.plugin.IPlugin;

@AWrap(type=EWrapType.FRAGMENT,wrapKey = 200)
public class BaseFragment extends Fragment {
    IPlugin<Fragment,Integer> plugin;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        plugin = Starter.getInstance().getRouter().currentPlugin();
        plugin.install(getContext(),-1,this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return plugin.viewHolder().install(getContext(),this,container);
    }

}
