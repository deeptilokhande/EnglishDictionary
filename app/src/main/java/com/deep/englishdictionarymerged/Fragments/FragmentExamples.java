package com.deep.englishdictionarymerged.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deep.englishdictionarymerged.R;
import com.deep.englishdictionarymerged.WordMeaning;

public class FragmentExamples extends Fragment {

    public FragmentExamples(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_definition,container,false);
        Context context = getActivity();
        TextView text = (TextView) view.findViewById(R.id.textViewD);
        String example = ((WordMeaning)context).example;
        text.setText(example);

        if(example==null){
            text.setText("Example not found");
        }
        return view;
    }
}
