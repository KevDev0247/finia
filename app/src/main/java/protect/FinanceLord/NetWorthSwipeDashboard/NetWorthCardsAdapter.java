package protect.FinanceLord.NetWorthSwipeDashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import protect.FinanceLord.R;

/**
 * The pager adapter to deliver the data of net worth item to the UI dashboard cards.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class NetWorthCardsAdapter extends PagerAdapter {

    private List<NetWorthCardsDataModel> dataModels;
    private Context context;

    public NetWorthCardsAdapter(List<NetWorthCardsDataModel> dataModels, Context context){
        this.dataModels = dataModels;
        this.context = context;
    }

    /**
     * Get the number of items.
     * @return the size of the list of data models.
     */
    @Override
    public int getCount() {
        return dataModels.size();
    }

    /**
     * Determine whether the view is from the object.
     *
     * @param view view of the item.
     * @param object The same object that was returned by instantiateItem(View, int).
     * @return whether the view is from the object
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    /**
     * Instantiate and load all the data to the item.
     * All the UI widgets are initialized first, and data is loaded onto each widget.
     *
     * @param container the container of the item
     * @param position the current position of the item
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.net_worth_dashboard_cards, container, false);

        ImageView imageView = view.findViewById(R.id.net_worth_card_graphics);
        TextView title = view.findViewById(R.id.cards_title);
        TextView value = view.findViewById(R.id.value);
        TextView dollarSign = view.findViewById(R.id.dollar_sign);
        TextView initializationMessage = view.findViewById(R.id.dashboard_initialization_message);

        imageView.setImageResource(dataModels.get(position).getImageId());
        title.setText(dataModels.get(position).getTitle());
        value.setText(dataModels.get(position).getValue());

        if (dataModels.get(position).getValue().equals(context.getString(R.string.no_data_message))) {
            value.setVisibility(View.GONE);
            dollarSign.setVisibility(View.GONE);
        } else {
            initializationMessage.setVisibility(View.GONE);
        }

        container.addView(view, 0);

        return view;
    }

    /**
     * Remove a page for the given position.
     *
     * @param container the container of the item
     * @param position the current position of the item
     * @param object The same object that was returned by instantiateItem(View, int).
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
