package org.app.hackio.rajasthanhackathonprototype;

/**
 * Created by gautam on 19/03/18.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView gridView;
    private TextView cropSelectedtv;
    private String cropSelected;

    private String crops[] = {
            "bajra", "blackgram", "copra", "greengram",
            "groundnut", "jowar", "maize", "onion",
            "paddy", "potato", "ragi", "sunflower", "tur"
    };

    private int imageList[] = {
            R.drawable.bajra, R.drawable.blackgram, R.drawable.copra, R.drawable.greengram,
            R.drawable.groundnut, R.drawable.jowar, R.drawable.maize, R.drawable.onion,
            R.drawable.paddy, R.drawable.potato, R.drawable.ragi, R.drawable.sunflower, R.drawable.tur
    };

    public BuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_buy, container, false);
        cropSelectedtv = (TextView) v.findViewById(R.id.selected_crop);
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<10;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", crops[i]);
            hm.put("flag", Integer.toString(imageList[i]) );
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "flag","txt"};

        // Ids of views in listview_layout
        int[] to = { R.id.flag,R.id.txt};

        SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), aList, R.layout.imageview_item, from, to);

        // Getting a reference to gridview of MainActivity
        GridView gridView = (GridView) v.findViewById(R.id.gridview);

        // Setting an adapter containing images to the gridview
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cropSelected = crops[position];
                cropSelectedtv.setText("Currently selected crop : " + cropSelected);
            }
        });

        return v;
    }
}