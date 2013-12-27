package com.ipmacro.parser;

import android.content.Context;

import com.ipmacro.iptv.R;
import com.ipmacro.model.Channel;
import com.ipmacro.model.ChannelType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LiveParser {
    /**
     * 返回分类列表
     * @param context
     * @param con
     * @param liveChannelIds
     * @param planChannelIds
     * @return
     * @throws JSONException
     */
    public static List<ChannelType> parser(Context context,String con,String liveChannelIds,String planChannelIds,String favIds) throws JSONException{
        List<ChannelType> typeList = new ArrayList<ChannelType>();
        
        JSONObject root = new JSONObject(con);
        JSONArray typeJSONList = root.getJSONArray("typeList");
        for (int i = 0; i < typeJSONList.length(); i++) {
            JSONObject obj = typeJSONList.getJSONObject(i);
            ChannelType type = new ChannelType(obj.getInt("id"),
                    obj.getString("name"));
            typeList.add(type);
        }
        List<Channel> channelList = parserReleaseList(root,liveChannelIds, planChannelIds);
        
        //归类
        for(int i = 0;i<typeList.size();i++){
            ChannelType type = typeList.get(i);
            for(Channel channel : channelList){
                String typeIds = channel.getTypeIds();
                if (typeIds != null
                        && typeIds.contains("[" + type.getId() + "]")) {
                    type.addChild(channel);
                }
            }
        }
        //全部
        ChannelType all = new ChannelType(0, context.getResources().getString(
                R.string.all_channels));
        all.setChannelList(channelList);
        typeList.add(0, all);
        
        //收藏
        List<Channel> favList = parserFavorites(channelList,favIds);
        ChannelType fav = new ChannelType(0, context.getResources().getString(
                R.string.fav_channels));
        fav.setChannelList(favList);
        typeList.add(1, fav);
        
        return typeList;
    }
    
    /**
     * 收藏归类
     * @param channelList
     * @param favIds
     * @return
     */
    public static List<Channel> parserFavorites(List<Channel> channelList,String favIds){
        if(channelList == null){
            return null;
        }
        List<Channel> favList = new ArrayList<Channel>();
        if(favIds != null && !favIds.equals("")){
            String[] ids = favIds.split(",");
            for(int i = 0;i<ids.length;i++){
                int id  = Integer.parseInt(ids[i]);
                for(Channel ch : channelList){
                    if(ch.getId() == id){
                        favList.add(ch);
                    }
                }
            }
        }
        return favList;
    }
    
    /**
     * 获取所有频道
     * @param root 根json
     * @param liveChannelIds 自购套餐id
     * @param planChannelIds 套餐全部id
     * @return 频道列表
     * @throws JSONException
     */
    private static List<Channel> parserReleaseList(JSONObject root,String liveChannelIds,String planChannelIds) throws JSONException{
        List<Channel> channelList = new ArrayList<Channel>();
        JSONArray channelJSONList = root.getJSONArray("releaseList");
        for (int i = 0; i < channelJSONList.length(); i++) {
            JSONObject obj = channelJSONList.getJSONObject(i);
            
            int id = obj.getInt("id");
            int isFree = obj.getInt("isFree");
            boolean isP2p = obj.getBoolean("isP2p");
            String logo = obj.getString("logo");
            int mode = obj.getInt("mode");
            String name = obj.getString("name");
            String playUrl = obj.getString("playUrl");
            int sort = obj.getInt("sort");
            int typeId = obj.getInt("typeId");
            String typeIds = obj.getString("typeIds");
            
            JSONObject propMap = obj.getJSONObject("propMap");
            JSONObject channelObj = propMap.getJSONObject("channel");
            String fileId = channelObj.getString("channelId");
            
            Channel c = new Channel();
            c.setId(id);
            c.setIsFree(isFree);
            c.setIsP2p(isP2p);
            c.setLogo(logo);
            c.setMode(mode);
            c.setName(name);
            c.setPlayUrl(playUrl);
            c.setSort(sort);
            c.setTypeId(typeId);
            c.setFileId(fileId);
            c.setTypeIds(typeIds);

            if (isP2p) {
                Boolean bo = false;
                if (c.getIsFree() > 0 || isInIDS(c.getId(), liveChannelIds)) {  //如果频道是免费的或者在自购套餐里面
                    bo = true;
                } else if (isInIDS(c.getId(), planChannelIds)) {
                    bo = true;
                }
                if (bo) {
                    if (isInIDS(c.getId(), liveChannelIds)) {
                        c.setState(true);
                    } else {
                        c.setState(false);
                    }

                    channelList.add(c);
                    JSONObject chMap = channelObj.getJSONObject("propMap");
                    if (chMap != null && chMap.has("port")) {
                        String sPort = chMap.getString("port");
                        if (sPort != null && !sPort.equals("")) {
                            Short port = Short.parseShort(sPort);
                            int peerid = chMap.getInt("peerId");
                            String ip = chMap.getString("ip");
                            String key = null;
                            if (chMap.has("accelerationKey")){
                                key = chMap.getString("accelerationKey");
                            }
                            c.setPort(port);
                            c.setPeerid(peerid);
                            c.setIp(ip);
                            c.setKey(key);
                        }
                    }
                }
            }
        }
        return channelList;
    }
    
    public static boolean isInIDS(int id, String ids) {
        if (ids != null && !ids.equals("")) {
            String[] idsArray = ids.split(",");
            int[] idArray = new int[idsArray.length];
            for (int i = 0; i < idsArray.length; i++) {
                if (Integer.parseInt(idsArray[i]) == id) {
                    return true;
                }
            }
        }
        return false;
    }
}
