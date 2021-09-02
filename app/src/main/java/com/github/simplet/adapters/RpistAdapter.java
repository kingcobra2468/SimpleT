package com.github.simplet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.simplet.R;
import com.github.simplet.activities.MainActivity;
import com.github.simplet.utils.RpistNode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RpistAdapter extends RecyclerView.Adapter<RpistAdapter.RpistItem> {
    private final List<RpistNode> rpistList;
    private final MainActivity mainActivity;

    public RpistAdapter(MainActivity mainActivity, List<RpistNode> rpists) {
        this.mainActivity = mainActivity;
        rpistList = rpists;
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
        RpistNode rpistNode = rpistList.get(i);

        listItemHolder.temperature.setText(String.valueOf(rpistNode.getTemperature()));
        listItemHolder.units.setText(rpistNode.getTemperatureScale().symbol);
    }

    @Override
    public int getItemCount() {
        return rpistList.size();
    }

    public void setRpistList(List<RpistNode> rpists) {
        rpistList.clear();
        rpistList.addAll(rpists);

        notifyDataSetChanged();
    }

    public class RpistItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView temperature;
        TextView rpistId;
        TextView units;

        public RpistItem(@NonNull View itemView) {
            super(itemView);

            temperature = itemView.findViewById(R.id.temperature);
            rpistId = itemView.findViewById(R.id.rpist_id);
            units = itemView.findViewById(R.id.units);
        }

        @Override
        public void onClick(View view) {
            //TODO: Change units wnen clicked
        }
    }
}
