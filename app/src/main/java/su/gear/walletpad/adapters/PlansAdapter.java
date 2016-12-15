package su.gear.walletpad.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import su.gear.walletpad.R;
import su.gear.walletpad.model.Plan;
import su.gear.walletpad.model.PlansListItem;

/**
 * Created by Андрей on 14.12.2016.
 */

public class PlansAdapter extends RecyclerView.Adapter {

    private final Context              context;
    private final LayoutInflater       inflater;
    private final List <PlansListItem> plans;

    public PlansAdapter (Context context, List <PlansListItem> plans) {
        this.context  = context;
        this.inflater = LayoutInflater.from (context);
        this.plans    = plans;
    }

    @Override
    public int getItemViewType (int position) {
        return R.layout.item_plan;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return PlanViewHolder.newInstance (inflater, parent);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        PlansListItem item = plans.get (position);
        Plan plan = (Plan) item;

        PlanViewHolder planHolder =    (PlanViewHolder) holder;
        planHolder.title.setText       (plan.title ());
        planHolder.amount.setText      (plan.amount () + "");
        planHolder.description.setText (plan.description ());
    }

    @Override
    public int getItemCount () {
        return plans.size ();
    }

    private static class PlanViewHolder extends RecyclerView.ViewHolder {
        final TextView title, description, amount;

        private PlanViewHolder (View itemView) {
            super (itemView);

            title       = (TextView) itemView.findViewById (R.id.plan_title);
            description = (TextView) itemView.findViewById (R.id.plan_tags);
            amount      = (TextView) itemView.findViewById (R.id.plan_sum);
        }

        static PlansAdapter.PlanViewHolder newInstance (LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate (R.layout.item_plan, parent, false);
            return new PlansAdapter.PlanViewHolder   (view);
        }
    }

}
