package protect.FinanceLord.TranscationUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.R;

public class CategoryLabelsAdapter extends RecyclerView.Adapter<CategoryLabelsAdapter.LabelViewHolder> {

    Context context;
    List<BudgetsType> budgetsTypes;

    public CategoryLabelsAdapter(Context context, List<BudgetsType> budgetsTypes){
        this.context = context;
        this.budgetsTypes = budgetsTypes;
    }

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_category_labels, parent, false);

        return new LabelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelViewHolder holder, int position) {
        holder.categoryName.setText(budgetsTypes.get(position).getBudgetsName());

        holder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //query database for all transactions under this category
            }
        });
    }

    @Override
    public int getItemCount() {
        return budgetsTypes.size();
    }

    public class LabelViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        LinearLayout label;

        public LabelViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.transaction_label_name);
            label = itemView.findViewById(R.id.transaction_category_label);
        }
    }
}
