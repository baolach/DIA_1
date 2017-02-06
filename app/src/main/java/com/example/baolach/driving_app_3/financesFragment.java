package com.example.baolach.driving_app_3;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 * With Fragments, developers now can update another portion of
 * the UI on the screen that corresponds to the user selection,,
 * without needing to move the user to another screen.
 */
public class FinancesFragment extends Fragment {

    public FinancesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_finances, container, false);
    }
}
