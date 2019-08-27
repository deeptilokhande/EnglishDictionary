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

public class FragmentSynonyms extends Fragment {

    public FragmentSynonyms(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_definition,container,false);
        Context context = getActivity();
        TextView text = (TextView) view.findViewById(R.id.textViewD);
        String synonym = ((WordMeaning)context).antonyms;


        if(synonym!=null){
            synonym =synonym.replaceAll(",","\n");
            text.setText(synonym);
        }

        if(synonym==null){
            text.setText("Synonym not found");
        }
        return view;
    }
}
