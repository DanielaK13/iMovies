package com.example.daniela.imovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/*
public class NewSerieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_serie);
    }
}
*/


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.daniela.imovies.comm.SeriesGet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.daniela.imovies.entity.MyList;

public class NewSerieActivity extends AppCompatActivity{

    private FloatingActionButton fab;
    private TextView txtNewWhat;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private EditText edtNewWhat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SeriesGet();
        setContentView(R.layout.activity_new_serie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initViews();

        fab.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {



                MyList listserie = new MyList(edtNewWhat.getText().toString(), false);

                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                FirebaseDatabase fb = FirebaseDatabase.getInstance();
                DatabaseReference ref = fb.getReference("users/" + user.getUid() + "/serie/");
                ref.push().setValue(listserie);

                finish();
                //}else{
                    //Toast.makeText(NewSerieActivity.this, getString(R.string.anErrorOcurred), Toast.LENGTH_SHORT).show();
                //}
            }
        });

    }

    public void enableFab(){
        fab.setEnabled(true);
        fab.getBackground().setAlpha(100);
    }

    public void disableFab(){
        fab.setEnabled(false);
        fab.getBackground().setAlpha(80);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, getString(R.string.newSerieCancelled), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(this, getString(R.string.newSerieCancelled), Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        return true;
    }

    public void initViews(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        txtNewWhat = (TextView) findViewById(R.id.txtNewWhat);
        edtNewWhat = (EditText) findViewById(R.id.edtNewWhat);
    }

}

