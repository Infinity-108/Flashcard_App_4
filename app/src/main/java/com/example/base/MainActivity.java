package com.example.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Animation from_left = AnimationUtils.loadAnimation(this,R.anim.left_out);
    //Animation from_right = AnimationUtils.loadAnimation(this,R.anim.right_in);



    //this variable keeps track of the index of the current card that we want to display.
    int currentCardDisplayedIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        TextView Question = findViewById(R.id.question_textview);
        TextView Answer = findViewById(R.id.answer_textview);

        //initializing the flashcardDatabase variable
        flashcardDatabase = new FlashcardDatabase(getApplicationContext());

        allFlashcards = flashcardDatabase.getAllCards();


        //this if statement accounts for if allFlashcards list is empty. In the case that it is,
        //the flashcard in main xml will be displayed.
        if (allFlashcards != null && allFlashcards.size() > 0) {
            Question.setText(allFlashcards.get(0).getQuestion());
            Answer.setText(allFlashcards.get(0).getAnswer());
        }

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent,100);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

               // Intent new_intent = new Intent(MainActivity.this, AddCardActivity.class);
                //startActivity(new_intent);
                //overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });

        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFlashcards.size() == 0)
                    return;

                currentCardDisplayedIndex +=1;

                if(currentCardDisplayedIndex >= allFlashcards.size()) {

                    currentCardDisplayedIndex = 0;
                }

                allFlashcards = flashcardDatabase.getAllCards();
                Flashcard flashcard = allFlashcards.get(currentCardDisplayedIndex);



                Answer.setText(flashcard.getAnswer());
                Question.setText(flashcard.getQuestion());

                Answer.setVisibility(View.INVISIBLE);
                Question.setVisibility(View.VISIBLE);


            }
        });




        Question.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Question.setVisibility(View.INVISIBLE);
                Answer.setVisibility(View.VISIBLE);
            }
        });

        Answer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Answer.setVisibility(View.INVISIBLE);
                Question.setVisibility(View.VISIBLE);
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView Question = findViewById(R.id.question_textview);
        TextView Answer = findViewById(R.id.answer_textview);

        if (requestCode == 100 && data != null) { //needs to match so they know which activity to call?
            String the_question = data.getExtras().getString("the_question"); // 'string1' needs to match key used in Intent
            String the_answer = data.getExtras().getString("the_answer");

            Question.setText(the_question);
            Answer.setText(the_answer);

            //this allows for saving of the data gotten from flashcard.
            flashcardDatabase.insertCard(new Flashcard(the_question, the_answer));

            //this updates our list of flashcards with each flashcard created.
            allFlashcards = flashcardDatabase.getAllCards();
        }



    }
    //declaring the variable outside of a method to allow access in all methods
    FlashcardDatabase flashcardDatabase;

    //this is basically a list variable that will hold all flashcard objects
    List<Flashcard> allFlashcards;

}