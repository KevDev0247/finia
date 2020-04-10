package protect.FinanceLord.NetWorthCardsUtils;

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

public class NetWorthCardsAdapter extends PagerAdapter {

    private List<AssetsCardsDataModel> dataModels;
    private LayoutInflater layoutInflater;
    private Context context;

    public NetWorthCardsAdapter(List<AssetsCardsDataModel> dataModels, Context context){
        this.dataModels = dataModels;
        this.context = context;
    }

    @Override
    public int getCount() { return dataModels.size();}

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.assets_cards, container, false);

        ImageView imageView;
        TextView title, details;

        imageView = view.findViewById(R.id.assets_card_graphics);
        title = view.findViewById(R.id.cards_title);
        details = view.findViewById(R.id.details);

        imageView.setImageResource(dataModels.get(position).getImageId());
        title.setText(dataModels.get(position).getTitle());
        details.setText(dataModels.get(position).getDetails());

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
