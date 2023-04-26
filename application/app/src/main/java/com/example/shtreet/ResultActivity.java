package com.example.shtreet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultActivity extends AppCompatActivity {

    TextView textViewResult, historyTextView, errorTextView;
    Button copyButton;
    List<String> roadTypes = new ArrayList<>();
    VoieAPI voieAPI;


    /**
     * Fill the list with the provided road types
     */
    private void fillRoadTypesList() {
        roadTypes.add("Rue");
        roadTypes.add("Allée");
        roadTypes.add("Cité");
        roadTypes.add("Cours");
        roadTypes.add("Avenue");
        roadTypes.add("Boulevard");
        roadTypes.add("Impasse");
        roadTypes.add("Chemin");
        roadTypes.add("Place");
        roadTypes.add("Esplanade");
        roadTypes.add("Impasse");
        roadTypes.add("Passage");
        roadTypes.add("Voie");
        roadTypes.add("Port");
        roadTypes.add("Square");
        roadTypes.add("Place");
        roadTypes.add("Promenade");
        roadTypes.add("Cité");
        roadTypes.add("Quai");
        roadTypes.add("Cour");
        roadTypes.add("Galerie");
        roadTypes.add("Villa");
        roadTypes.add("Sentier");
        roadTypes.add("Belvédère");
        roadTypes.add("Passerelle");


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        fillRoadTypesList();
        //Get the views and buttons
        textViewResult = findViewById(R.id.recognizedTextView);
        historyTextView = findViewById(R.id.history);
        errorTextView = findViewById(R.id.errorTextView);
        copyButton = findViewById(R.id.copyButton);

        //Copy button
        copyButton.setOnClickListener(view -> {
            //Get the content of the text view
            String copy = historyTextView.getText().toString();
            // Get the clipboard manager
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // Create a ClipData to hold the text to copy
            ClipData clipData = ClipData.newPlainText("text", copy);
            // Add the ClipData to the clipboard
            clipboardManager.setPrimaryClip(clipData);
            // Show a message to indicate that the copy has been done
            Toast.makeText(getApplicationContext(), "Copié dans le presse papier", Toast.LENGTH_SHORT).show();
        });

        String uriString = getIntent().getStringExtra("uri");
        Uri uri = Uri.parse(uriString);
        extractTextFromUri(getApplicationContext(), uri);

        //Create the communication with the API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VoieAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        voieAPI = retrofit.create(VoieAPI.class);
    }

    /**
     * Extract the text from the photo.
     *
     * @param context the context
     * @param _uri    the photo
     */
    public void extractTextFromUri(Context context, Uri _uri) {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        try {
            InputImage image = InputImage.fromFilePath(context, _uri);
            Task<Text> result =
                    recognizer.process(image)
                            // Task completed successfully
                            .addOnSuccessListener(t -> {
                                String analyzedStreetName = analyzeExtractedText(t);
                                getHistory(analyzedStreetName);
                            })
                            .addOnFailureListener(e -> {
                                // Task failed with an exception
                                Toast.makeText(getApplicationContext(), "Une erreur s'est produite", Toast.LENGTH_LONG).show();
                            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Analyse the extracted text (essentially check if at least one line starts with a provided road type).
     *
     * @param result the text
     * @return analysed text if the check is good, an error if not.
     */
    private String analyzeExtractedText(Text result) {
        StringBuilder analyzedText = new StringBuilder();
        /*Extract text
        Each TextBlock represents a rectangular block of text containing zero, one or more Line objects.
        Each Line object represents a line of text, which contains zero or more Element objects.
        Each Element object represents a word or word-like entity, which contains zero or more Symbol objects.
        Each Symbol object represents a character, a number or a verbal entity.
        */
        for (Text.TextBlock block : result.getTextBlocks()) {
            String blockText = block.getText();
            for (Text.Line line : block.getLines()) {
                String lineText = block.getText();
                //Check if the extracted line starts with a provided road type.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (roadTypes.stream().anyMatch(s -> lineText.startsWith(s) || lineText.startsWith(s.toUpperCase()))) {
                        analyzedText.append(result.getText());
                        Pattern replace = Pattern.compile("\n");
                        Matcher matcher = replace.matcher(analyzedText.toString());
                        String s = matcher.replaceAll(" ");
                        textViewResult.setText("Votre texte extrait : " + s);
                        return s;
                    }
                }
                for (Text.Element element : line.getElements()) {
                    String elementText = element.getText();
                    for (Text.Symbol symbol : element.getSymbols()) {
                        String symbolText = symbol.getText();
                    }
                }
            }
        }
        errorTextView.setText(getErrorMessage(result.getText()));
        return null;
    }

    /**
     * @return the error message if the something went wrong.
     */
    public String getErrorMessage(String result) {
        StringBuilder message = new StringBuilder();
        message.append("Une erreur s'est produite ! Cela peut être dû à plusieurs choses : \n");
        message.append("\n");
        message.append("1. Le texte reconnu n'est pas une rue, pour rappel voici les types de rues pris en compte : \n ");
        message.append("\n");
        int count = 0;
        for (String r : roadTypes) {
            if (count < 4) {
                message.append(" ").append(r);
                count++;
            } else {
                message.append(" ").append(r).append(" \n");
                count = 0;
            }
        }
        message.append("\n2. Cette erreur peut également se produire si la rue n'est pas une rue de Paris. \n");
        message.append("\n");
        message.append("3. Nous n'arrivons pas à reconnaitre le texte de votre photo. \n");
        message.append("Votre texte extrait : \n");
        message.append(result);
        return message.toString();
    }

    /**
     * Call the api to get all the necessary information for a given street name
     *
     * @param result name of the street
     */
    private void getHistory(String result) {
        //Call the api
        Call<Voie> call = voieAPI.getStreetWithName(result);
        call.enqueue(new Callback<Voie>() {
            @Override
            public void onResponse(Call<Voie> call, Response<Voie> response) {
                //If no response print the error message
                if (!response.isSuccessful()) {
                    textViewResult.setVisibility(View.INVISIBLE);
                    copyButton.setVisibility(View.INVISIBLE);
                    if (errorTextView.getText().equals("")) {
                        errorTextView.setText(getErrorMessage(result));
                    }
                    //Toast.makeText(getApplicationContext(), "Une erreur s'est produite", Toast.LENGTH_LONG).show();
                    return;
                }
                errorTextView.setVisibility(View.INVISIBLE);
                Voie voie = response.body();

                //Print the history and description of the street
                StringBuilder history = new StringBuilder();
                history.append("Histoire : ");
                if (voie.getHistory() == null) {
                    history.append("Cette rue n'a pas d'histoire renseigné. \n");
                } else {
                    history.append(voie.getHistory());
                    history.append("\n");
                }

                history.append("\nDescription : ");
                if (voie.getStreetDescription() == null) {
                    history.append("Cette rue n'a pas de description renseigné. \n");
                } else {
                    history.append(voie.getStreetDescription());
                    history.append("\n");
                }
                historyTextView.setText(history);
            }

            @Override
            public void onFailure(Call<Voie> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Une erreur s'est produite", Toast.LENGTH_LONG).show();
            }
        });
    }
}