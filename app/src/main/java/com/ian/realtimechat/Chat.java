package com.ian.realtimechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chat {
    public String id, title;

    public Chat() {

    }

    public Chat(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        return result;
    }
}
