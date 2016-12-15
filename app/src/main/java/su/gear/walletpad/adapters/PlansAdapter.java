package su.gear.walletpad.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import su.gear.walletpad.model.Operation;
import su.gear.walletpad.model.OperationsListItem;
import su.gear.walletpad.model.Plan;
import su.gear.walletpad.model.PlansListItem;
import su.gear.walletpad.model.Separator;
import su.gear.walletpad.utils.IOUtils;

/**
 * Created by Андрей on 14.12.2016.
 */

public class PlansAdapter extends RecyclerView.Adapter {

    public PlansAdapter (Context context, List <PlansListItem> plans) {

    }

    @Override
    public int getItemViewType (int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount () {
        return 0;
    }

}
