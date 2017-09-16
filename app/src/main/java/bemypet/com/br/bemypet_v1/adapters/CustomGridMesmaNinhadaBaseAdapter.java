package bemypet.com.br.bemypet_v1.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import bemypet.com.br.bemypet_v1.R;

/**
 * Created by manuelatarouco on 27/08/17.
 */

public class CustomGridMesmaNinhadaBaseAdapter extends BaseAdapter{

    private Context mContext;
    private final List<String> nomes;
    private final List<String>  images;

    public CustomGridMesmaNinhadaBaseAdapter(Context c, List<String> nomes, List<String> images) {
        mContext = c;
        this.images = images;
        this.nomes = nomes;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return nomes.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_mesma_ninhada, null);
            TextView textView = (TextView)grid.findViewById(R.id.gridMinhaNinhadaTxt);
            ImageView imageView = (ImageView)grid.findViewById(R.id.gridMinhaNinhadaImg);
            textView.setText(nomes.get(position));
            if(images.size() > 0) {
                Glide.with(mContext).load(images.get(position)).apply(RequestOptions.circleCropTransform()).into(imageView);
            }
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
