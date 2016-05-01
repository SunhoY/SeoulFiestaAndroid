package io.harry.seoulfiesta.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.model.VacationItem;

public class VacationListAdapter extends RecyclerView.Adapter<VacationListAdapter.ViewHolder> {
    private List<VacationItem> vacationItems;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vacation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VacationItem vacationItem = this.vacationItems.get(position);
        holder.date.setText(vacationItem.getDateString());
        holder.status.setText(vacationItem.getStatus());
        holder.type.setText(vacationItem.getType());
    }

    @Override
    public int getItemCount() {
        return this.vacationItems.size();
    }

    public void setData(List<VacationItem> vacationItems) {
        this.vacationItems = vacationItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView date;
        public TextView status;
        public TextView type;

        public ViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
            status = (TextView) itemView.findViewById(R.id.vacation_status);
            type = (TextView) itemView.findViewById(R.id.vacation_type);
        }
    }
}
