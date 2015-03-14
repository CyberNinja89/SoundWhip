package com.renegade.audio.testapp2electricboogaloo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {
int voteCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        voteCount = 0;
    }



public void addVote(){
    voteCount++;
}






}
