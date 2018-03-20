package org.app.hackio.rajasthanhackathonprototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by gautam on 20/03/18.
 */

public class ImageAdapter extends ArrayAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private String images[];

    public ImageAdapter(Context mContext, String[] images) {
        super(mContext, R.layout.imageview_item, images);
        this.mContext = mContext;
        this.images = images;
        inflater = LayoutInflater.from(mContext);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.imageview_item, parent, false);
        }

        return view;
    }

}
