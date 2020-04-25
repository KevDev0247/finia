package protect.FinanceLord.NetWorthEditReportsUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Date;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.LiabilitiesTypeDao;
import protect.FinanceLord.R;

public class Edit_LiabilitiesFragment extends Fragment {

    String title;
    Date currentTime;

    public Edit_LiabilitiesFragment(String title, Date currentTime) {
        this.title = title;
        this.currentTime = currentTime;
    }

    ExpandableListView expandableListView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View liabilitiesView = inflater.inflate(R.layout.fragment_edit_liabilities, null);
        expandableListView = liabilitiesView.findViewById(R.id.liabilities_list_view);

        initLiabilitiesCategory();

        return liabilitiesView;
    }

    private void initLiabilitiesCategory() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(Edit_LiabilitiesFragment.this.getContext());
                LiabilitiesTypeDao liabilitiesTypeDao = database.liabilitiesTypeDao();
            }
        });
    }
}
