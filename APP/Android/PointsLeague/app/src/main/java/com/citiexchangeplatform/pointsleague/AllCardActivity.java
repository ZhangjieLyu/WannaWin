package com.citiexchangeplatform.pointsleague;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.study.xuan.xvolleyutil.base.XVolley;
import com.study.xuan.xvolleyutil.callback.CallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AllCardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ProgressDialog dialog;
    AllCardAdapter allCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_card);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_all_card);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        allCardAdapter = new AllCardAdapter(AllCardActivity.this);
        recyclerView.setAdapter(allCardAdapter);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //getInfos();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_all_card);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        allCardAdapter.addData("Nike旗舰店购物积分","http://www.never-give-it-up.top/wp-content/uploads/2018/07/nike_logo.png",123,2);
        allCardAdapter.addData("APPLE中国","http://www.never-give-it-up.top/wp-content/uploads/2018/07/apple_logo.png",555,0.5);
        allCardAdapter.addData("中国移动账户积分","http://www.never-give-it-up.top/wp-content/uploads/2018/07/yidong_logo.png",142,1.5);
        allCardAdapter.addData("中国电信账户积分","http://www.never-give-it-up.top/wp-content/uploads/2018/07/dianxin_logo.png",1322,0.8);

    }

    private void getInfos() {
        XVolley.getInstance()
                .doPost()
                .url("http://193.112.44.141:80/citi/getMSInfo")
                .addParam("userID", LogStateInfo.getInstance(AllCardActivity.this).getUserID())
                .addParam("n", "40")
                .build()
                .execute(AllCardActivity.this, new CallBack<String>() {
                    @Override
                    public void onSuccess(Context context, String response) {
                        System.out.println(response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jobj = jsonArray.getJSONObject(i);
                                String name = jobj.getString("merchantName");
                                String logoURL = jobj.getString("logoURL");
                                int point = jobj.getInt("points");
                                double proportion = jobj.getDouble("proportion");
                                allCardAdapter.addData(name, logoURL, point, proportion);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        super.onError(error);
                        dialog.dismiss();
                        Toast.makeText(AllCardActivity.this, "服务器连接失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onBefore() {
                        super.onBefore();
                        dialog = ProgressDialog.show(AllCardActivity.this, "", "正在获取卡列表...");
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        allCardAdapter.getFilter().filter(newText);
        return true;
    }
}
