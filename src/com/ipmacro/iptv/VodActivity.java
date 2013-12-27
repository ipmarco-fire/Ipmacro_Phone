package com.ipmacro.iptv;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.ipmacro.app.Config;
import com.ipmacro.app.IptvApplication;
import com.ipmacro.model.Episode;
import com.ipmacro.model.Vod;
import com.ipmacro.parser.MovieParser;
import com.linkin.utils.HttpUtil;
import com.linkin.utils.SyncImageLoader;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class VodActivity extends SherlockActivity{
    
    public static final String PARM_VOD = "parm_vod";
    IptvApplication mApp;
    SyncImageLoader imageLoader;
    Vod vod;
    
    Button btnPlay;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_vod);
        getSupportActionBar().setHomeButtonEnabled(true);
        
        mApp = (IptvApplication) getApplication();
        imageLoader = new SyncImageLoader(this);
        if(mApp.hasParam(PARM_VOD)){
            vod = (Vod) mApp.getParam(PARM_VOD);
            
            initView();
            initData();
        }
    }
    
    private void initView(){
        ImageView imgLogo = (ImageView) findViewById(R.id.img_logo);
        imageLoader.displayImage(imgLogo, getResources().getString(R.string.weburl) + vod.getLogo());
        TextView txtName = (TextView) findViewById(R.id.txt_name);
        txtName.setText(vod.getName());
        TextView txtActors = (TextView) findViewById(R.id.txt_actors);
        txtActors.setText(getResources().getString(R.string.vod_actors) + " : "+vod.getActors());
        TextView txtDirector = (TextView) findViewById(R.id.txt_director);
        txtDirector.setText(getResources().getString(R.string.vod_director) + " : "+vod.getDirector());
        TextView txtLanguageRegion = (TextView) findViewById(R.id.txt_language_region);
        txtLanguageRegion.setText(getResources().getString(R.string.vod_language) + " : "+vod.getLanguage() +"  "+getResources().getString(R.string.vod_region) + " : "+vod.getRegion());
   
        btnPlay = (Button) findViewById(R.id.btn_play);
        gridView = (GridView) findViewById(R.id.gridview);
    }
    
    private void initData(){
        String url = getResources().getString(R.string.weburl) + "/channel/stb/getProgramInfoById.htm?programId=" + vod.getId();
        HttpUtil.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    MovieParser.parserVod(response,vod);
                    afterInitData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable error) {
                Toast.makeText(VodActivity.this, R.string.load_data_error, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFinish() {
            }
        });
    }
    
    private void afterInitData(){
        List<Episode> episodeList = vod.getEpisodeList();
        if(episodeList == null || episodeList.size() == 0){
            return ;
        }
        btnPlay.setVisibility(View.VISIBLE);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void play(View view){
        
    }
}
