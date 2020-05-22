package com.paulribe.memowords.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.paulribe.memowords.R;

public class RevisionFinishedFragment extends Fragment {

    private Button beginLearningButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_revision_finished, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        beginLearningButton = view.findViewById(R.id.button_begin_learning);

        beginLearningButton.setOnClickListener(view1 ->
                ((MainActivity)getActivity()).comeBackLearningFragment());
    }
}
