package com.github.simplet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.simplet.R;
import com.github.simplet.activities.MainActivity;
import com.github.simplet.utils.RpistNode;
import com.github.simplet.utils.TemperatureScale;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecycleView for efficiently displaying ana managing individual rpist nodes.
 */
public class RpistAdapter extends RecyclerView.Adapter<RpistAdapter.RpistItem> {
    private final List<RpistNode> rpistList;
    private final MainActivity mainActivity;

    /**
     * Instantiates a new rpist adapter.
     *
     * @param mainActivity the main activity reference
     * @param rpists       the rpists list reference
     */
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

        // sets the appropriate values for the card
        listItemHolder.temperature.setText(String.format("%.2f", rpistNode.getTemperature()));
        listItemHolder.units.setText(rpistNode.getTemperatureScale().symbol);
        listItemHolder.rpistId.setText(rpistNode.getId());

        // listener for changing individual node scale when the card is clicked
        listItemHolder.itemView.setOnClickListener(view -> {
            RpistNode node = rpistList.get(listItemHolder.getAdapterPosition());
            TemperatureScale currentScale = node.getTemperatureScale();

            int scaleIndex = TemperatureScale.scales.indexOf(currentScale);
            // cycles to the next scale
            node.setTemperatureScale(TemperatureScale.scales.get((scaleIndex + 1) % 3));
        });
    }

    @Override
    public int getItemCount() {
        return rpistList.size();
    }

    /**
     * Sets rpist list. Overrides existing value that are stored by the recycleview.
     *
     * @param rpists the rpists to display
     */
    public void setRpistList(List<RpistNode> rpists) {
        rpistList.clear();
        rpistList.addAll(rpists);

        notifyDataSetChanged();
    }

    /**
     * The viewholder for the individual rpist card.
     */
    public class RpistItem extends RecyclerView.ViewHolder {
        /**
         * Reference to the temperature widget.
         */
        TextView temperature;
        /**
         * Reference to the rpist id widget.
         */
        TextView rpistId;
        /**
         * Reference to the temperature scale widget.
         */
        TextView units;

        /**
         * Instantiates a new rpist item.
         *
         * @param itemView the item view from the recycleview
         */
        public RpistItem(@NonNull View itemView) {
            super(itemView);

            temperature = itemView.findViewById(R.id.temperature);
            rpistId = itemView.findViewById(R.id.rpist_id);
            units = itemView.findViewById(R.id.units);
        }
    }
}
