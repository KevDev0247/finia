package protect.FinanceLord.TransactionViewing;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import protect.FinanceLord.Communicators.GroupByCategoryCommunicator;
import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.R;

public class CategoryFiltersAdapter extends RecyclerView.Adapter<CategoryFiltersAdapter.LabelViewHolder> {

    private List<BudgetsType> budgetsTypes;
    private GroupByCategoryCommunicator toTransactionActivityCommunicator;

    public CategoryFiltersAdapter(List<BudgetsType> budgetsTypes, GroupByCategoryCommunicator fromAdapterCommunicator){
        this.budgetsTypes = budgetsTypes;
        this.toTransactionActivityCommunicator = fromAdapterCommunicator;
    }

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_category_labels, parent, false);

        return new LabelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LabelViewHolder holder, int position) {
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

    @Override
    public int getItemCount() {
        return budgetsTypes.size();
    }

    static class LabelViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        LinearLayout label;

        LabelViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.transaction_label_name);
            label = itemView.findViewById(R.id.transaction_category_label);
        }
    }
}
