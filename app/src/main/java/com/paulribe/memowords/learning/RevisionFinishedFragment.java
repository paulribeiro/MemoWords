package com.paulribe.memowords.learning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.paulribe.memowords.MainActivity;
import com.paulribe.memowords.R;

public class RevisionFinishedFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_revision_finished, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Button beginLearningButton = view.findViewById(R.id.button_begin_learning);

        beginLearningButton.setOnClickListener(view1 ->
                ((MainActivity)getActivity()).comeBackLearningFragmentLearnNewWords());
    }
}
