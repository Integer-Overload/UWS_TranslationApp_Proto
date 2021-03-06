package com.example.translateapptwo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;



public class EnglishToSpanish  extends AppCompatActivity {

    private Button say_it;
    private TextToSpeech tts;
    private TextInputEditText textInputEditText; // this one not working
    private TextView translatedTv;
    private String originalText;
    private String translatedText;
    private boolean connected;
    Translate translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.english_spanish);
            say_it = (Button) findViewById((R.id.button_say)); //button


           //this is  send the text from the TextView of already translated text into Text to Speech (TTS):
            translatedTv = (TextView) findViewById(R.id.translatedTv); //text box
            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {

                            //HERE TO CHANGE THE TTS INTO SPANISH
                            Locale spanish = new Locale("es", "ES");
                            int ttsLang = tts.setLanguage(spanish);

                            if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                                    || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("TTS", "The Language is not supported!");
                            } else {
                                Log.i("TTS", "Language Supported.");
                            }
                            Log.i("TTS", "Initialization success.");
                        } else {
                            Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                        }
            }
        });



        //WHAT HAPPENS TO THE BUTTON SAY
        say_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //WHAT HAPPENS IF BUTTON TTS IS CLICKED AND WORKS:

                    String data = translatedTv.getText().toString(); // gets the text from the input box and makes a string
                    Log.i("TTS BUTTON", "BUTTON CLICKED" + data); // shows button functioning in system
                    int speechUpdate = tts.speak(data, TextToSpeech.QUEUE_FLUSH, null); // this allows tts to speak

                    //WHAT HAPPENS IF BUTTON CLICKED DOES NOT WORK

                    if (speechUpdate == TextToSpeech.ERROR) {
                        Log.e("TTS BUTTON", "Error, Text-to-Speed did not work");

                    }
                }
            });

            textInputEditText = findViewById(R.id.textInputEditText);
            translatedTv = findViewById(R.id.translatedTv);
            Button button_en_sp = findViewById(R.id.button_en_sp);


        button_en_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (InternetConnectionCheck()) {

                        //Checks for internet connection, and gets translate API service, then starts translation:
                        ObtainTranslationService();
                        TranslateText();

                    } else {

                        //if internet connection fails, display warning message:
                        translatedTv.setText(getResources().getString(R.string.no_connection));
                    }

                }
            });

    }



    @Override
    public void onDestroy() {
            super.onDestroy();
            if (tts != null) {
                tts.stop();
                tts.shutdown();
            }
    }


    //TOAST FOR SAY IT BUTTON   - used for testing when no google API was implemented
    public void VoiceComingUp(View view) {
            Toast say_it;
            say_it = Toast.makeText(this, R.string.toast_message_one, Toast.LENGTH_SHORT);
            say_it.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            say_it.show();
    }



    //TOAST FOR TRANSLATE BUTTON  - used for testing when no google API was implemented

    public void TranslationToast(View view) {
            Toast trans_toast;
            trans_toast = Toast.makeText(this, R.string.toast_message_two, Toast.LENGTH_SHORT);
            trans_toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            trans_toast.show();
    }


    //VERIFY TRANSLATION CREDENTIALS:
   public void ObtainTranslationService() {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try (InputStream is = getResources().openRawResource(R.raw.credentials)) {

                //Get credentials:
                final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

                //Set credentials and get translate service:
                TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
                translate = translateOptions.getService();

            } catch (IOException ioe) {
                ioe.printStackTrace();

        }
    }


    //TRANSLATE TEXT
    public void TranslateText() {

            //Get input text to be translated:
            originalText = textInputEditText.getText().toString();
            Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage("es"), Translate.TranslateOption.model("base"));
            translatedText = translation.getTranslatedText();

            //Translated text and original text are set to TextViews:
            translatedTv.setText(translatedText);

    }


    //TO CHECK/VALIDATE INTERNET CONNECTION:
    public boolean InternetConnectionCheck() {

            //Check internet connection:
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            //Means that we are connected to a network (mobile or wi-fi)
            connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

            return connected;
    }




    //EXIT BUTTON ACTIONS:
    public void ExitButton(View view) {
        Button button_exit = findViewById(R.id.button_exit);

        AlertDialog.Builder ScreenMessage = new AlertDialog.Builder(this);
        ScreenMessage.setMessage("Do you want to exit??");
        ScreenMessage.setPositiveButton("yes",

                //WHAT HAPPENS WHEN USER CLICKS YES TO EXIT APP:
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });


        //WHAT HAPPENS WHEN USER CLICKS NO TO EXIT APP:
        ScreenMessage.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EnglishToSpanish.this,"Enjoy the Translator",Toast.LENGTH_LONG).show();
                // finish();
            }
        });

        AlertDialog Alert = ScreenMessage.create();
        Alert.show();
    }

}