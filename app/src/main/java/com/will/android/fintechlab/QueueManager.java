package com.will.android.fintechlab;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QueueManager {

    private ArrayList<QueueGifItem> itemsQueue = new ArrayList<>();
    private MainActivity mainActivity;
    private String url;
    private QueueGifItem item;
    private RequestQueue queue;

    public QueueManager(MainActivity mainActivity, String url) {
        this.mainActivity = mainActivity;
        this.url = url;
        queue = Volley.newRequestQueue(this.mainActivity);
    }

    int index = 0;


    public void init(QueueItemReleaseListener listener) {
        requestJson(listener);
    }

    public void next(QueueItemReleaseListener listener) {
        index++;

        if (index == itemsQueue.size()) {
            requestJson(listener);
        } else if (index < itemsQueue.size()) {
            listener.onItemReleased(itemsQueue.get(index));
        }

    }

    public void prev(QueueItemReleaseListener listener) {
        index--;
        listener.onItemReleased(itemsQueue.get(index));
    }


    private void requestJson(QueueItemReleaseListener listener) {
        String url = this.url;
        item = new QueueGifItem();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String gifUrl = response.getString("gifURL");
                            String desc = response.getString("description");

                            item.setUrl(gifUrl);
                            item.setDesc(desc);
                            itemsQueue.add(item);
                            listener.onItemReleased(item);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onItemReleased(null);
                            index--;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onItemReleased(null);
                index--;
            }
        });

        queue.add(request);
    }

    public int getIndex() {
        return index;
    }

}
