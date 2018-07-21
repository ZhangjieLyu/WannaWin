package com.citiexchangeplatform.pointsleague;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderTabUsedFragment extends Fragment {

    RecyclerView usedOrderRecyclerView;
    private MyOrderAdapter orderAdapter;


    public OrderTabUsedFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        OrderTabUsedFragment fragment = new OrderTabUsedFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_tab_used, container, false);

        usedOrderRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_myOrder);

        //设置RecyclerView管理器
        setRecyclerView();

        //获得初始化数据
        initData();

        return view;
    }

    /*订单：totalPoints,description,usePoints*/
    protected void initData()
    {
        //orderAdapter.addData("niki","5元代金券","2018-7-23");
        //orderAdapter.addData("中国联通","5元代金券","2018-7-26");
        //orderAdapter.addData("中国移动","5元代金券","2018-7-25");
        //orderAdapter.addData("中国电信","5元代金券","2018-7-27");
        //orderAdapter.addData("niki","5元代金券","2018-7-25");
        //orderAdapter.addData("中国联通","5元代金券","2018-7-27");
        getHistoryOrderByCoupon();




    }

    protected void setRecyclerView(){
        orderAdapter = new MyOrderAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        usedOrderRecyclerView.setLayoutManager(layoutManager);
        orderAdapter = new MyOrderAdapter(getActivity());
        usedOrderRecyclerView.setAdapter(orderAdapter);
    }

    private void getHistoryOrderByCoupon() {
        String url="http://193.112.44.141:80/citi/userCoupon/getUsedCoupons";
        RequestQueue queue = MyApplication.getHttpQueues();
        //RequestQueue queue=Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("success",s);
                System.out.println(s);
                try {
                    JSONArray jsonArray = new JSONArray(s);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);


                        String description = jObj.getString("description");
                        String itemName = jObj.getString("itemName");
                        String time = "兑换时间：" +jObj.getString("getTime");
                        String validityTerm = "使用时间：" +jObj.getString("useTime");
                        //String logoURL  = "http://www.never-give-it-up.top/wp-content/uploads/2018/07/zhouheiya_logo.png";
                        String logoURL = jObj.getString("logoURL");
                        String itemID = jObj.getString("ItemID");

                        orderAdapter.addData(itemName,description,time,validityTerm,logoURL,itemID,"used");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();

                map.put("userID",LogStateInfo.getInstance(getContext()).getUserID());


                return map;
            }
        };
        queue.add(request);


    }

}
