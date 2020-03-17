package com.wet.eyetracking.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wet.eyetracking.utils.MyUtils;
import com.wet.eyetracking.R;
import com.wet.eyetracking.model.ExperimentItem;
import com.wet.eyetracking.ui.fragment.ExperimentFragment;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by wet on 26.05.17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ExperimentItemViewHolder> {

    private List<ExperimentItem> items = new ArrayList<>();

    public HistoryAdapter(List<ExperimentItem> items) {
        this.items = items;
    }

    @Override
    public ExperimentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExperimentItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_experiment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ExperimentItemViewHolder holder, int position) {
        final ExperimentItem item = getItem(position);

        final Context context = holder.itemView.getContext();

        holder.tvName.setText(item.getName());
        holder.tvDate.setText(MyUtils.parseDate(item.getDate(), holder.itemView.getContext()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExperimentFragment chartFragment = ExperimentFragment.Companion.newInstance(item.getId());

                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, chartFragment)
                        .addToBackStack(chartFragment.getTag())
                        .commit();
            }
        });

        holder.imbMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.model_dialog_item);
                //arrayAdapter.add("Редактировать");
                arrayAdapter.add("Удалить");

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        removeItem(item);
                    }
                });

                AlertDialog dialog = builderSingle.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builderSingle.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ExperimentItem getItem(int position) {
        return items.get(position);
    }


    public void removeItem(ExperimentItem item) {
        int position = items.indexOf(item);

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<ExperimentItem> results = realm.where(ExperimentItem.class).equalTo("id", item.getId()).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });

        items.remove(position);

        notifyItemRemoved(position);
    }

    public static class ExperimentItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDate;

        ImageButton imbMenu;

        public ExperimentItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            imbMenu = (ImageButton) itemView.findViewById(R.id.imb_menu);
        }
    }

}
