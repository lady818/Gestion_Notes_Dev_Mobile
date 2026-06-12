package com.example.gestionotes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

// écran principal : liste des notes, recherche, favoris
public class MainActivity extends Activity {

    private NoteStorage storage;
    private LinearLayout notesContainer;
    private LinearLayout colorPalette;
    private EditText searchEditText;
    private TextView emptyText;
    private TextView favoriteButton;
    private ScrollView notesScrollView;

    private boolean filtreFavoris = false;
    private String recherche = "";

    // pour gérer le double clic sur une note
    private final Handler handler = new Handler(Looper.getMainLooper());
    private long dernierClic = 0;
    private String derniereNoteId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storage = new NoteStorage(this);
        notesContainer = findViewById(R.id.notesContainer);
        colorPalette = findViewById(R.id.colorPalette);
        searchEditText = findViewById(R.id.searchEditText);
        emptyText = findViewById(R.id.emptyText);
        favoriteButton = findViewById(R.id.favoriteButton);
        notesScrollView = findViewById(R.id.notesScrollView);

        afficherPalette();

        // bouton + : montre ou cache les couleurs
        findViewById(R.id.addButton).setOnClickListener(v -> {
            boolean visible = colorPalette.getVisibility() == View.VISIBLE;
            colorPalette.setVisibility(visible ? View.GONE : View.VISIBLE);
        });

        // bouton favoris : affiche seulement les notes en favori
        favoriteButton.setOnClickListener(v -> {
            filtreFavoris = !filtreFavoris;
            mettreAJourBoutonFavoris();
            afficherNotes();
        });

        // recherche en temps réel sur le titre
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recherche = s.toString().trim().toLowerCase();
                afficherNotes();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // on recharge quand on revient de l'écran d'édition
        afficherNotes();
    }

    // crée les ronds de couleur pour une nouvelle note
    private void afficherPalette() {
        colorPalette.removeAllViews();

        for (String couleur : Couleurs.LISTE) {
            TextView rond = new TextView(this);
            int taille = Utils.dp(this, 48);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(taille, taille);
            params.setMargins(0, 0, 0, Utils.dp(this, 10));
            rond.setLayoutParams(params);
            rond.setBackground(Utils.fond(couleur, Utils.dp(this, 24)));
            rond.setOnClickListener(v -> ouvrirEdition(null, couleur));
            colorPalette.addView(rond);
        }
    }

    // affiche les notes (avec filtres recherche + favoris)
    private void afficherNotes() {
        notesContainer.removeAllViews();
        ArrayList<Note> notes = storage.getAllNotes();
        int compteur = 0;

        for (Note note : notes) {
            if (filtreFavoris && !note.favorite) continue;
            if (!recherche.isEmpty() && !note.title.toLowerCase().contains(recherche)) continue;

            notesContainer.addView(creerCarte(note));
            compteur++;
        }

        boolean vide = compteur == 0;
        emptyText.setVisibility(vide ? View.VISIBLE : View.GONE);
        notesScrollView.setVisibility(vide ? View.GONE : View.VISIBLE);
    }

    // une carte de note dans la liste
    private View creerCarte(Note note) {
        LinearLayout carte = new LinearLayout(this);
        carte.setOrientation(LinearLayout.VERTICAL);
        carte.setPadding(Utils.dp(this, 16), Utils.dp(this, 14), Utils.dp(this, 16), Utils.dp(this, 14));
        carte.setBackground(Utils.fond(note.color, Utils.dp(this, 8)));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, Utils.dp(this, 12));
        carte.setLayoutParams(params);

        // ligne du haut : titre + étoile favori
        LinearLayout entete = new LinearLayout(this);
        entete.setOrientation(LinearLayout.HORIZONTAL);
        entete.setGravity(Gravity.CENTER_VERTICAL);

        TextView titre = new TextView(this);
        titre.setText(note.title);
        titre.setTextColor(Color.BLACK);
        titre.setTextSize(24);
        titre.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        titre.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView etoile = new TextView(this);
        etoile.setText(note.favorite ? "★" : "☆");
        etoile.setTextColor(Color.BLACK);
        etoile.setTextSize(28);

        TextView date = new TextView(this);
        date.setText(note.date);
        date.setTextColor(Color.DKGRAY);
        date.setTextSize(12);
        date.setPadding(0, Utils.dp(this, 8), 0, 0);

        TextView contenu = new TextView(this);
        contenu.setText(note.content);
        contenu.setTextColor(Color.BLACK);
        contenu.setTextSize(14);
        contenu.setMaxLines(3);
        contenu.setPadding(0, Utils.dp(this, 8), 0, 0);

        entete.addView(titre);
        entete.addView(etoile);
        carte.addView(entete);
        carte.addView(date);
        carte.addView(contenu);

        carte.setOnClickListener(v -> gererClic(note));
        return carte;
    }

    // simple clic = modifier, double clic rapide = favori
    private void gererClic(Note note) {
        long maintenant = System.currentTimeMillis();

        if (note.id.equals(derniereNoteId) && maintenant - dernierClic < 350) {
            handler.removeCallbacksAndMessages(null);
            note.favorite = !note.favorite;
            storage.saveNote(note);
            afficherNotes();
            derniereNoteId = "";
            return;
        }

        dernierClic = maintenant;
        derniereNoteId = note.id;

        // on attend un peu pour voir si c'est un double clic
        handler.postDelayed(() -> {
            if (note.id.equals(derniereNoteId)) {
                ouvrirEdition(note.id, note.color);
            }
        }, 360);
    }

    private void ouvrirEdition(String noteId, String couleur) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("note_id", noteId);
        intent.putExtra("note_color", couleur);
        startActivity(intent);
        colorPalette.setVisibility(View.GONE);
    }

    private void mettreAJourBoutonFavoris() {
        favoriteButton.setText(filtreFavoris ? "Tous" : "Favoris");
        favoriteButton.setTextColor(filtreFavoris ? Color.WHITE : Color.BLACK);
        String couleur = filtreFavoris ? "#000000" : "#FFFFFF";
        favoriteButton.setBackground(Utils.fond(couleur, Utils.dp(this, 10)));
    }
}
