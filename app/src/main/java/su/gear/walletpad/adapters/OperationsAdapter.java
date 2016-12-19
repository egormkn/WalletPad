package su.gear.walletpad.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import su.gear.walletpad.R;
import su.gear.walletpad.model.Operation;
import su.gear.walletpad.model.OperationsListItem;
import su.gear.walletpad.model.RecyclerButton;
import su.gear.walletpad.model.Separator;
import su.gear.walletpad.utils.IOUtils;
import su.gear.walletpad.views.IconView;

public class OperationsAdapter extends RecyclerView.Adapter {

    private final int ITEM_OPERATION = R.layout.item_operation;
    private final int ITEM_SEPARATOR = R.layout.item_separator;
    private final int ITEM_BUTTON = R.layout.item_recyclerbutton;

    private final LayoutInflater layoutInflater;
    private final List<OperationsListItem> operations;
    private final Context context;

    public OperationsAdapter(Context context, List<OperationsListItem> operations) {
        this.operations = operations;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        OperationsListItem item = operations.get(position);
        if (item instanceof Operation) {
            return ITEM_OPERATION;
        } else if (item instanceof Separator) {
            return ITEM_SEPARATOR;
        } else if (item instanceof RecyclerButton) {
            return ITEM_BUTTON;
        } else {
            throw new IllegalArgumentException("Unknown type of item");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_OPERATION:
                return OperationViewHolder.newInstance(layoutInflater, parent);
            case ITEM_SEPARATOR:
                return DateViewHolder.newInstance(layoutInflater, parent);
            case ITEM_BUTTON:
                //return ButtonViewHolder.newInstance(layoutInflater, parent);
            default:
                throw new IllegalArgumentException("Unknown View type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OperationsListItem item = operations.get(position);
        if (holder instanceof OperationViewHolder) {
            Operation operation = (Operation) item;
            OperationViewHolder operationHolder = (OperationViewHolder) holder;
            operationHolder.titleView.setText(operation.getDescription());
            operationHolder.sumView.setText(String.valueOf(operation.getAmount()));
            operationHolder.tagsView.setText(IOUtils.join(operation.getTags(), ", "));
            if (position % 4 == 0) {
                operationHolder.categoryIconView.setIconColor(Color.RED);
                operationHolder.categoryIconView.setShapeColor(Color.GREEN);
            } else {
                operationHolder.categoryIconView.setIconColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
                operationHolder.categoryIconView.setShapeColor(ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, null));
            }
            /*if (!movie.isError()) {
                progressHolder.progressBar.setVisibility(View.VISIBLE);
                progressHolder.progressBar.setIndeterminate(true);
                progressHolder.tryAgainButton.setVisibility(View.GONE);
                progressHolder.errorText.setVisibility(View.GONE);
            } else {
                progressHolder.progressBar.setVisibility(View.GONE);
                progressHolder.tryAgainButton.setVisibility(View.VISIBLE);
                progressHolder.errorText.setVisibility(View.VISIBLE);
                progressHolder.errorText.setText(movie.getErrorMessage());
                progressHolder.tryAgainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.loadNextPage(true);
                    }
                });
            }*/
        } else if (holder instanceof DateViewHolder) {
            final DateViewHolder movieHolder = (DateViewHolder) holder;
            Separator separator = (Separator) item;
            movieHolder.dateText.setText(separator.getText());
            /*movieHolder.imageView.setImageURI(movie.posterPath);
            movieHolder.titleView.setText(movie.localizedTitle);
            movieHolder.originalTitleView.setText(movie.originalTitle);
            movieHolder.infoView.setText(movie.overviewText);
            movieHolder.ratingView.setText(String.valueOf(movie.rating));*/
        }
    }

    @Override
    public int getItemCount() {
        return operations.size();
    }

    private static class OperationViewHolder extends RecyclerView.ViewHolder {

        final IconView categoryIconView;
        final TextView titleView, tagsView, sumView;

        private OperationViewHolder(View itemView) {
            super(itemView);
            categoryIconView = (IconView) itemView.findViewById(R.id.operation_category);
            titleView = (TextView) itemView.findViewById(R.id.operation_title);
            tagsView = (TextView) itemView.findViewById(R.id.operation_tags);
            sumView = (TextView) itemView.findViewById(R.id.operation_sum);
        }

        static OperationViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_operation, parent, false);
            return new OperationViewHolder(view);
        }
    }

    private static class DateViewHolder extends RecyclerView.ViewHolder {

        private final TextView dateText;

        private DateViewHolder(View itemView) {
            super(itemView);
            dateText = (TextView) itemView.findViewById(R.id.separator_text);
        }

        static DateViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_separator, parent, false);
            return new DateViewHolder(view);
        }
    }

    private class ButtonViewHolder {
    }
}
