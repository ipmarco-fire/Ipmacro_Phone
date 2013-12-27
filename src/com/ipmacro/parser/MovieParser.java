
package com.ipmacro.parser;

import com.ipmacro.model.Episode;
import com.ipmacro.model.Vod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieParser {

    public static List<Vod> parserDataList(JSONObject response) throws JSONException {
        int _curPage = response.getInt("curPage");
        int totalPage = response.getInt("totalPage");
        JSONArray list = response.getJSONArray("list");

        List<Vod> dataList = new ArrayList<Vod>();
        for (int i = 0; i < list.length(); i++) {
            JSONObject obj = list.getJSONObject(i);

            int id = obj.getInt("id");
            String name = obj.getString("name");
            String logo = obj.getString("logoHighlight");
            String actors = obj.getString("actors");
            String director = obj.getString("director");
            String region = obj.getString("region");
            String language = obj.getString("language");

            Vod vod = new Vod();
            vod.setId(id);
            vod.setName(name);
            vod.setLogo(logo);
            vod.setActors(actors);
            vod.setDirector(director);
            vod.setLanguage(language);
            vod.setRegion(region);

            dataList.add(vod);
        }

        return dataList;
    }

    public static void parserVod(JSONObject response, Vod vod) throws JSONException {
        JSONObject program = response.getJSONObject("program");
        if (program.has("propMap")) {
            JSONObject propMap = program.getJSONObject("propMap");
            Boolean isDeductFees = propMap.getBoolean("isDeductFees");
            vod.setDeductFees(isDeductFees);

            JSONArray episodeList = propMap.getJSONArray("episodeList");
            if (episodeList != null) {
                for (int i = 0; i < episodeList.length(); i++) {
                    JSONObject obj = episodeList.getJSONObject(i);
                    String url = obj.getString("url");
                    
                    Episode e = new Episode();
                    e.setUrl(url);
                    
                    vod.addEpisode(e);
                }
            }
        }
    }

}
