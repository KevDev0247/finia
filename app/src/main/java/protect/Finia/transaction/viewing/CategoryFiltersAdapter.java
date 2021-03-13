package protect.Finia.transaction.viewing;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import protect.Finia.communicators.GroupByCategoryCommunicator;
import protect.Finia.models.BudgetsType;
import protect.Finia.R;

/**
 * The recycler view adapter to deliver the data of each budget type name to the filter UI widget.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/05/18
 */
public class CategoryFiltersAdapter extends RecyclerView.Adapter<CategoryFiltersAdapter.FilterViewHolder> {

    private List<BudgetsType> budgetsTypes;
    private GroupByCategoryCommunicator toTransactionActivityCommunicator;

    public CategoryFiltersAdapter(List<BudgetsType> budgetsTypes, GroupByCategoryCommunicator fromAdapterCommunicator){
        this.budgetsTypes = budgetsTypes;
        this.toTransactionActivityCommunicator = fromAdapterCommunicator;
    }

    /**
     * Create the view holder of the filter item.
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     *
     * @param viewType the type of the view
     * @param parent the ViewGroup of this list
     * @return the view holder of this filter
     */
    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_category_labels, parent, false);

        return new FilterViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder the view holder of the filter
     * @param position the position of the current item
     */
    @Override
    public void onBindViewHolder(@NonNull final FilterViewHolder holder, int position) {
        holder.categoryName.setText(budgetsTypes.get(position).getBudgetsName());

        holder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CategoryAdapter", "the label was clicked");
                String categoryLabel = holder.categoryName.getText().toString();
                toTransactionActivityCommunicator.message(categoryLabel);
            }
        });
    }

    /**
     * Get the number of items.
     *
     * @return the size of the list of budget types.
     */
    @Override
    public int getItemCount() {
        return budgetsTypes.size();
    }

    /**
     * The class of view holder for the filter of category types.
     */
    static class FilterViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        LinearLayout label;

        FilterViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.transaction_label_name);
            label = itemView.findViewById(R.id.transaction_category_label);
        }
    }
}
