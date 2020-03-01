package protect.FinanceLord.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import protect.FinanceLord.R;

public class NetWorthFragment extends Fragment {
    String title;
    NetWorthFragment(String title){this.title = title;}

    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_networth, null);
        TextView title = view.findViewById(R.id.item_number);
        title.setText(this.title);
        return view;
    }
}
