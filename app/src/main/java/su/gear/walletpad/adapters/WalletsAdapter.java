package su.gear.walletpad.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import su.gear.walletpad.R;
import su.gear.walletpad.model.Wallet;
import su.gear.walletpad.model.WalletsListItem;

public class WalletsAdapter extends RecyclerView.Adapter {

    private final Context               context;
    private final LayoutInflater        inflater;
    private final List<WalletsListItem> wallets;

    public WalletsAdapter (Context context, List <WalletsListItem> wallets) {
        this.context  = context;
        this.inflater = LayoutInflater.from (context);
        this.wallets  = wallets;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_wallet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return WalletsAdapter.WalletViewHolder.newInstance (inflater, parent);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        WalletsListItem item = wallets.get (position);
        Wallet wallet = (Wallet) item;

        WalletsAdapter.WalletViewHolder walletHolder = (WalletsAdapter.WalletViewHolder) holder;
        walletHolder.title.setText       (wallet.getTitle());
        walletHolder.amount.setText      (wallet.getAmount() + "");
    }

    @Override
    public int getItemCount () {
        return wallets.size ();
    }

    private static class WalletViewHolder extends RecyclerView.ViewHolder {

        final TextView title, amount;

        private WalletViewHolder (View itemView) {
            super (itemView);

            title  = (TextView) itemView.findViewById (R.id.wallet_title);
            amount = (TextView) itemView.findViewById (R.id.wallet_amount);
        }

        static WalletsAdapter.WalletViewHolder newInstance (LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate   (R.layout.item_wallet, parent, false);
            return new WalletsAdapter.WalletViewHolder (view);
        }
    }

}
