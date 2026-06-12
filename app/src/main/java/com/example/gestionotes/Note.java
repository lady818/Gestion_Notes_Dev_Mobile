package com.example.gestionotes;

import org.json.JSONException;
import org.json.JSONObject;

// une note avec ses infos de base
public class Note {
    public String id;
    public String title;
    public String content;
    public String color;
    public String date;
    public boolean favorite;

    public Note(String id, String title, String content, String color, String date, boolean favorite) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.color = color;
        this.date = date;
        this.favorite = favorite;
    }

    // transforme la note en JSON pour la sauvegarder
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("title", title);
        json.put("content", content);
        json.put("color", color);
        json.put("date", date);
        json.put("favorite", favorite);
        return json;
    }

    // recrée une note à partir du JSON sauvegardé
    public static Note fromJson(JSONObject json) throws JSONException {
        return new Note(
                json.getString("id"),
                json.getString("title"),
                json.getString("content"),
                json.getString("color"),
                json.getString("date"),
                json.optBoolean("favorite", false)
        );
    }
}
