package com.example.gestionotes;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

// gère la sauvegarde des notes dans le téléphone (SharedPreferences)
public class NoteStorage {
    private static final String NOM_FICHIER = "notes_preferences";
    private static final String CLE_NOTES = "notes_list";

    private final SharedPreferences prefs;

    public NoteStorage(Context context) {
        prefs = context.getSharedPreferences(NOM_FICHIER, Context.MODE_PRIVATE);
    }

    // lit toutes les notes enregistrées
    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        String json = prefs.getString(CLE_NOTES, "[]");

        try {
            JSONArray tableau = new JSONArray(json);
            for (int i = 0; i < tableau.length(); i++) {
                notes.add(Note.fromJson(tableau.getJSONObject(i)));
            }
        } catch (JSONException e) {
            notes.clear();
        }

        return notes;
    }

    // cherche une note par son id
    public Note findNoteById(String id) {
        for (Note note : getAllNotes()) {
            if (note.id.equals(id)) {
                return note;
            }
        }
        return null;
    }

    // ajoute une nouvelle note ou met à jour une existante
    public void saveNote(Note note) {
        ArrayList<Note> notes = getAllNotes();
        boolean existe = false;

        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).id.equals(note.id)) {
                notes.set(i, note);
                existe = true;
                break;
            }
        }

        if (!existe) {
            notes.add(0, note); // la nouvelle note en haut de la liste
        }

        ecrire(notes);
    }

    // supprime une note
    public void deleteNote(String id) {
        ArrayList<Note> notes = getAllNotes();

        for (int i = notes.size() - 1; i >= 0; i--) {
            if (notes.get(i).id.equals(id)) {
                notes.remove(i);
            }
        }

        ecrire(notes);
    }

    // écrit la liste complète dans SharedPreferences
    private void ecrire(ArrayList<Note> notes) {
        JSONArray tableau = new JSONArray();

        try {
            for (Note note : notes) {
                tableau.put(note.toJson());
            }
            prefs.edit().putString(CLE_NOTES, tableau.toString()).apply();
        } catch (JSONException e) {
            // on ne fait rien, les anciennes données restent
        }
    }
}
