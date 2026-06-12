package com.example.gestionotes;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

// écran pour créer ou modifier une note
public class EditNoteActivity extends Activity {

    private NoteStorage storage;
    private EditText titleEditText;
    private EditText contentEditText;
    private LinearLayout colorContainer;
    private Button deleteButton;
    private TextView screenTitleText;

    private String noteId = null;
    private String couleurChoisie = "#F2C94C";
    private boolean favorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        storage = new NoteStorage(this);
        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        colorContainer = findViewById(R.id.colorContainer);
        deleteButton = findViewById(R.id.deleteButton);
        screenTitleText = findViewById(R.id.screenTitleText);

        noteId = getIntent().getStringExtra("note_id");
        String couleurRecue = getIntent().getStringExtra("note_color");
        if (couleurRecue != null) {
            couleurChoisie = couleurRecue;
        }

        creerBoutonsCouleur();

        // si on modifie une note existante, on remplit le formulaire
        if (noteId != null) {
            Note note = storage.findNoteById(noteId);
            if (note != null) {
                screenTitleText.setText("Modifier note");
                titleEditText.setText(note.title);
                contentEditText.setText(note.content);
                couleurChoisie = note.color;
                favorite = note.favorite;
                deleteButton.setVisibility(View.VISIBLE);
                rafraichirCouleurs();
            }
        }

        findViewById(R.id.saveButton).setOnClickListener(v -> enregistrer());
        deleteButton.setOnClickListener(v -> supprimer());
    }

    // les ronds de couleur cliquables
    private void creerBoutonsCouleur() {
        colorContainer.removeAllViews();

        for (String couleur : Couleurs.LISTE) {
            TextView bouton = new TextView(this);
            int taille = Utils.dp(this, 44);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(taille, taille);
            params.setMargins(0, 0, Utils.dp(this, 12), 0);
            bouton.setLayoutParams(params);
            bouton.setTag(couleur);
            bouton.setOnClickListener(v -> {
                couleurChoisie = (String) v.getTag();
                rafraichirCouleurs();
            });
            colorContainer.addView(bouton);
        }

        rafraichirCouleurs();
    }

    // met en évidence la couleur sélectionnée
    private void rafraichirCouleurs() {
        for (int i = 0; i < colorContainer.getChildCount(); i++) {
            TextView bouton = (TextView) colorContainer.getChildAt(i);
            String couleur = (String) bouton.getTag();
            boolean selectionne = couleurChoisie.equals(couleur);

            GradientDrawable fond = Utils.fond(couleur, Utils.dp(this, 22));
            if (selectionne) {
                fond.setStroke(Utils.dp(this, 4), Color.BLACK);
            } else {
                fond.setStroke(Utils.dp(this, 1), Color.LTGRAY);
            }
            bouton.setBackground(fond);
        }
    }

    private void enregistrer() {
        String titre = titleEditText.getText().toString().trim();
        String contenu = contentEditText.getText().toString().trim();

        if (titre.isEmpty() || contenu.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir le titre et le contenu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (noteId == null) {
            noteId = UUID.randomUUID().toString();
        }

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE).format(new Date());
        Note note = new Note(noteId, titre, contenu, couleurChoisie, date, favorite);
        storage.saveNote(note);
        finish();
    }

    private void supprimer() {
        if (noteId != null) {
            storage.deleteNote(noteId);
        }
        finish();
    }
}
