package com.example.translateapptwo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class IntroPage extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_page);

        configureButtonOne();
        configureButtonTwo();

    }

    //to connect to English to Spanish activity using the Screen button and intents
    private void configureButtonOne() {
        Button button_en_sp = (Button) findViewById(R.id.button_en_sp);
        button_en_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroPage.this, EnglishToSpanish.class));
            }

        });
    }

    //to pass to Spanish to English activity using the Screen button and intents
    private void configureButtonTwo() {
        Button button_sp_en = (Button) findViewById(R.id.button_sp_en);
        button_sp_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroPage.this, SpanishToEnglish.class));
            }

        });
    }

    //EXIT BUTTON ACTIONS:
    public void ExitButton(View view) {
        Button button_exit = findViewById(R.id.button_exit);

        AlertDialog.Builder ScreenMessage = new AlertDialog.Builder(this);
        ScreenMessage.setMessage(" Quieres Salir?? \n \n Do you want to exit??");
        ScreenMessage.setPositiveButton("si \n \n yes",

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
        ScreenMessage.setNegativeButton(" \n No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(IntroPage.this,"Disfruta el Traductor \n Enjoy the Translator",Toast.LENGTH_LONG).show();
                // finish();
            }
        });

        AlertDialog Alert = ScreenMessage.create();
        Alert.show();
    }
}