package com.github.simplet.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.simplet.R;
import com.github.simplet.activities.MainActivity;

import java.util.List;

public class RpistAdapter extends RecyclerView.Adapter<RpistAdapter.RpistNode> {
    private List<Integer> mRpistList;
    private MainActivity mMainActivity;

    public RpistAdapter (MainActivity mainActivity, List<Integer> rpists) {
        mMainActivity = mainActivity;
        mRpistList = rpists;
    }

    public RpistAdapter(List<Integer> mRpistList) {
        this.mRpistList = mRpistList;
    }

    @NonNull
    @Override
    public RpistAdapter.RpistNode onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rpist_card, viewGroup, false);

        return new RpistNode(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RpistAdapter.RpistNode listItemHolder, int i) {
        listItemHolder.mTemperature.setText(mRpistList.get(i).toString());
    }

    @Override
    public int getItemCount() {
        return mRpistList.size();
    }

    public class RpistNode extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTemperature;

        public RpistNode(@NonNull View itemView) {
            super(itemView);
            mTemperature = itemView.findViewById(R.id.temperature);
        }

        @Override
        public void onClick(View view) {
        }
    }
}
