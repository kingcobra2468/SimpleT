package com.github.simplet.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.simplet.R;
import com.github.simplet.activities.MainActivity;
import com.github.simplet.network.clients.RpistNodeClient;
import com.github.simplet.utils.RpistNode;

import java.util.List;

public class RpistAdapter extends RecyclerView.Adapter<RpistAdapter.RpistItem> {
    private List<RpistNode> mRpistList;
    private MainActivity mMainActivity;

    public RpistAdapter (MainActivity mainActivity, List<RpistNode> rpists) {
        mMainActivity = mainActivity;
        mRpistList = rpists;
    }

    public RpistAdapter(List<RpistNode> mRpistList) {
        this.mRpistList = mRpistList;
    }

    @NonNull
    @Override
    public RpistAdapter.RpistItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_rpist_card, viewGroup, false);

        return new RpistItem(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RpistAdapter.RpistItem listItemHolder, int i) {
        RpistNode rpistNode = mRpistList.get(i);

        listItemHolder.mTemperature.setText(String.valueOf(rpistNode.getTemperature()));
        listItemHolder.mUnits.setText(rpistNode.getTemperatureScale().symbol);
    }

    @Override
    public int getItemCount() {
        return mRpistList.size();
    }

    public class RpistItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTemperature;
        TextView mRpistId;
        TextView mUnits;

        public RpistItem(@NonNull View itemView) {
            super(itemView);

            mTemperature = itemView.findViewById(R.id.temperature);
            mRpistId = itemView.findViewById(R.id.rpist_id);
            mUnits = itemView.findViewById(R.id.units);
        }

        @Override
        public void onClick(View view) {
            //TODO: Change units wnen clicked
        }
    }
}
