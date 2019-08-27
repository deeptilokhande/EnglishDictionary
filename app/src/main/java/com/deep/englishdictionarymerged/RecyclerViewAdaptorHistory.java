package com.deep.englishdictionarymerged;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdaptorHistory extends RecyclerView.Adapter<RecyclerViewAdaptorHistory.HistoryViewHolder > {

    private ArrayList<History> histories;
    private Context context;

    public RecyclerViewAdaptorHistory(Context context,ArrayList histories){
        this.context=context;
        this.histories=histories;

    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{

        TextView en_word;
        TextView en_def;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            en_word = (TextView) itemView.findViewById(R.id.en_word);
            en_def =(TextView) itemView.findViewById(R.id.en_def);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String text = histories.get(position).getEn_word();

                    Intent intent = new Intent(context,WordMeaning.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("en_word",text);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public RecyclerViewAdaptorHistory.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item_layout,viewGroup,false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdaptorHistory.HistoryViewHolder historyViewHolder, int i) {
        historyViewHolder.en_word.setText(histories.get(i).getEn_word());
        historyViewHolder.en_def.setText(histories.get(i).getEn_def());
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }
}
