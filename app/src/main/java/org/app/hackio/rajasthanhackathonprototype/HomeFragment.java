package org.app.hackio.rajasthanhackathonprototype;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FusedLocationProviderClient mFusedLocationClient;
    private String location;
    private JSONObject weatherData;
    private TextView temperatureTv, dateTv, summaryTv;
    private ListView listView;
    private ArrayList<String> forecasts = new ArrayList<>();
    private ArrayAdapter<String> itemsAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        temperatureTv = v.findViewById(R.id.temperature);
        dateTv = v.findViewById(R.id.date);
        summaryTv = v.findViewById(R.id.summary);
        listView = v.findViewById(R.id.forecast);
        itemsAdapter =
                new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, forecasts);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getLastLocation();
        forecasts.clear();
    }

    // TODO check for location permissions and ask for them
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                            }
                        }
                    });
        }catch (SecurityException e){

        }

    }

    private void getLastLocation() throws SecurityException{
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this.getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            location = Double.toString(task.getResult().getLatitude())
                                    + ","+ Double.toString(task.getResult().getLongitude()) ;
                            fetchWeatherData(location);
                            Log.i("MyLogger", location);

                        } else {
                            Log.w("Location", "getLastLocation:exception", task.getException());
                            // location not detected
                        }
                    }
                });
    }

    private void fetchWeatherData(String location){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this.getContext());

        String url = "https://api.darksky.net/forecast/0e9290dfb56bfbe9daa12192edb07acf/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+location,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyLogger" ,response);
                        try {
                            weatherData = new JSONObject(response);
                            updateUI();
                        }catch (JSONException je){
                            // do nothing, play bold
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("MyLogger", error.getMessage());
            }
        });
        mRequestQueue.add(stringRequest);
    }

    private void updateUI(){
        // List all the weather predictions
        try {
            JSONObject currently = weatherData.getJSONObject("currently");
            String date = convertDate(currently.getLong("time")).substring(10);
            dateTv.setText(date);
            summaryTv.setText(currently.getString("icon"));
            temperatureTv.setText(convertTemp(currently.getDouble("temperature"))+ "℃");
            JSONArray daily = weatherData.getJSONObject("daily").getJSONArray("data");

            for(int i=0; i<daily.length(); i++){
                String s = convertDate(daily.getJSONObject(i).getLong("time")).substring(0, 10)
                        + " : " +  daily.getJSONObject(i).getString("summary");
                forecasts.add(s);
            }
            listView.setAdapter(itemsAdapter);
        }catch (JSONException je){

        }
    }

    public String convertDate(long time){
        time = time*1000;
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }

    private String convertTemp(Double far){
        Double cel;
        cel = (far - 32)*5/9;
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(cel);
    }

}
