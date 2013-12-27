
package com.ipmacro.parser;

import com.ipmacro.model.Plan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlanParser {
    public static List<Plan> parserPlan(JSONObject root) throws JSONException {
        List<Plan> list = new ArrayList<Plan>();

        JSONArray planList = root.getJSONArray("plan");
        for (int i = 0; i < planList.length(); i++) {
            JSONObject obj = planList.getJSONObject(i);
            String liveChannelIds = obj.getString("liveChannelIds");
            String name = obj.getString("name");
            double price = obj.getDouble("price");
            
            Plan plan = new Plan();
            plan.setLiveChannelIds(liveChannelIds);
            plan.setName(name);
            plan.setPrice(price);
            
            list.add(plan);
        }

        return list;
    }
}
