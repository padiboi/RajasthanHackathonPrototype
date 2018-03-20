package org.app.hackio.rajasthanhackathonprototype;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class SellFragment extends Fragment {

    private TableView tableView;
    private EditText editQuantity;
    private TextView salesDesc;
    private Button sellSubmit;
    private int quantity, chosenIndex = 0;
    private int price_this_week = 2125;
    private String crop, location;
    private Spinner cropSpinner;
    private static final String[] TABLE_HEADERS = { "Duration", "Cost", "Predict", "Profit" };
    private FusedLocationProviderClient mFusedLocationClient;
    private String[][] DATA_TO_SHOW = new String[11][4];

    public SellFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sell, container, false);
        cropSpinner = (Spinner) view.findViewById(R.id.spinner);
        tableView = (TableView) view.findViewById(R.id.tableView);
        editQuantity = (EditText) view.findViewById(R.id.quantity_sell);
        sellSubmit = (Button) view.findViewById(R.id.submit_sell);
        salesDesc = (TextView) view.findViewById(R.id.selling_at);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this.getActivity(), TABLE_HEADERS));
        calculate();
        tableView.setDataAdapter(new SimpleTableDataAdapter(this.getActivity(), DATA_TO_SHOW));
        tableView.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                chosenIndex = rowIndex;
            }
        });
        cropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                crop = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing, play bold
            }
        });
        sellSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = editQuantity.getText().toString();
                try {
                    quantity = Integer.parseInt(temp);
                }catch (NumberFormatException ne){
                    quantity = 0;
                }
                Log.i("MyLogger", String.valueOf(quantity) + crop);
                Order order = new Order(price_this_week, quantity, Order.SELL, crop, true, location);
                commitToBlockchain();
            }
        });

        return view;
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

    @Override
    public void onStart() {
        super.onStart();
            getLastLocation();
    }

    private void getLastLocation() throws SecurityException{
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this.getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            location = Double.toString(task.getResult().getLatitude())
                                        + ","+ Double.toString(task.getResult().getLongitude()) ;
                            Log.i("MyLogger", location);

                        } else {
                            Log.w("Location", "getLastLocation:exception", task.getException());
                            // location not detected
                        }
                    }
                });
    }

    private void commitToBlockchain(){

        RequestQueue mRequestQueue = Volley.newRequestQueue(this.getContext());

        String url = "http://10.42.0.1:8081/";
        String data = crop + location + "";
        StringBuffer bf = new StringBuffer();
        for(int i=0; i<4; i++){
            bf.append(DATA_TO_SHOW[chosenIndex][i]);
        }
        String profit = bf.toString();
        Log.i("MyLogger", profit);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyLogger" ,response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("MyLogger", error.getMessage());
            }
        });
        mRequestQueue.add(stringRequest);

    }

    private void postRequest(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this.getContext());
        final String URL = "http://10.42.0.1:8081";
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("price", Double.toString(price_this_week));
        params.put("quantity", Integer.toString(quantity));
        params.put("crop", crop);
        params.put("location", location);

        JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        mRequestQueue.add(req);
    }

    public void calculate() {
        int quantity = 10;  // quintals
        // TODO : Update table every time quantity is changed
        salesDesc.setText("Currently Selling at " + Integer.toString(price_this_week));
        salesDesc.setVisibility(View.VISIBLE);

        float storageCostPerWeek = 6;

        int predictions[] = {2108, 2146, 2104, 2194, 2188, 2132, 2106, 2163, 2227, 2247, 2282};

        int weeklyRevenue = quantity * price_this_week;

        for (int i = 0; i<11; i++) {

            float storage_cost = storageCostPerWeek*i;

            float predicted_cost = quantity * ((predictions[i])  + storage_cost);

            float profit = predicted_cost - weeklyRevenue;

            //if(profit > 0) {
                // show only rows that make a profit
                addEntry(i,predictions[i],storage_cost,profit);
            //}
        }
    }

    public void addEntry(int week, int predicted_cost, float storage_cost, float profit) {

        int i = week;
        DATA_TO_SHOW[i][0] = Integer.toString(i+1) + " week";
        DATA_TO_SHOW[i][1] = String.valueOf(storage_cost);
        DATA_TO_SHOW[i][2] = String.valueOf(predicted_cost);
        DATA_TO_SHOW[i][3] = String.valueOf(profit);

    }


}