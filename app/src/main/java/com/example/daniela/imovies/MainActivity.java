package com.example.daniela.imovies;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.daniela.imovies.entity.MyList;
import com.example.daniela.imovies.misc.ListSerieAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import com.example.daniela.imovies.entity.MyList;
import com.example.daniela.imovies.misc.ListSerieAdapter;


public class MainActivity extends AppCompatActivity
      implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ImageView imgUserPhoto;
    private TextView txtUserName;
    private TextView txtUserEmail;
    private LinearLayout navHeaderMain;
    private NavigationView navigationView;
    private View nvHeader;
    private ArrayList<Integer> itemsSelected = new ArrayList<Integer>();
    private Toolbar toolbar;
    private boolean itemsEnabled = false;

    private ArrayList<MyList> series = new ArrayList<MyList>();
    private ListView livSeries;
    private ListSerieAdapter aLivSeries;

    private static final int NEW_SERIE = 69;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, NewSerieActivity.class), 1);
            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        infoUser();

        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference refSeries = fb.getReference("users/" + user.getUid() + "/serie");
        updateSeries(fb);


        refSeries.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MyList list = dataSnapshot.getValue(MyList.class);
                if(!list.getDone() ){
                    series.add(list);
                    aLivSeries = new ListSerieAdapter(MainActivity.this, R.layout.series_list_item, series);
                    livSeries.setAdapter(aLivSeries);
                    ajustaTamanho(series.size(), livSeries);


                    Context ctx = MainActivity.this;
                    Intent notificationIntent = new Intent(ctx, MainActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(ctx,
                            555, notificationIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT);

                    NotificationManager nm = (NotificationManager) ctx
                            .getSystemService(Context.NOTIFICATION_SERVICE);

                    Resources res = ctx.getResources();
                    Notification.Builder builder = new Notification.Builder(ctx);

                    builder.setContentIntent(contentIntent)
                            .setSmallIcon(R.drawable.ic_done)
                            .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_done))
                            .setAutoCancel(true)
                            .setContentTitle("Hello " + user.getDisplayName())
                            .setContentText("You have " + series.size() + " series to watch");
                    Notification n = builder.build();

                    nm.notify(001, n);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                MyList list = dataSnapshot.getValue(MyList.class);
                aLivSeries.remove(list);
                series.remove(list);
                aLivSeries.notifyDataSetChanged();
                reloadActivity();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        livSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!itemsSelected.contains(position)){
                    parent.getChildAt(position).setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                    TextView txtDescription = (TextView) parent.getChildAt(position).findViewById(R.id.txtDescription);
                    txtDescription.setTextColor(Color.WHITE);

                    itemsSelected.add(position);
                }else{
                    cleanListSelection(parent, position);
                    int i = itemsSelected.indexOf(position);
                    itemsSelected.remove(i);
                }

                if(itemsSelected.size() > 0){
                    itemsEnabled = true;
                    onCreateOptionsMenu(toolbar.getMenu());
                }else{
                    itemsEnabled = false;
                    onCreateOptionsMenu(toolbar.getMenu());
                }
            }
        });

    }

    public void reloadActivity(){
        finish();
        startActivity(new Intent(getIntent()));
    }


        public void updateSeries(final FirebaseDatabase fb){
            DatabaseReference ref = fb.getReference("users/" + user.getUid() + "/serie");

            ref.orderByChild("date").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    MyList list = dataSnapshot.getValue(MyList.class);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }


    public void addDone(MyList list){

    }

    public void cleanListSelection(AdapterView<?> parent, int position){
        parent.getChildAt(position).setBackgroundColor(Color.WHITE);
        TextView txtDescription = (TextView) parent.getChildAt(position).findViewById(R.id.txtDescription);
        txtDescription.setTextColor(Color.GRAY);
        TextView txtDate = (TextView) parent.getChildAt(position).findViewById(R.id.txtDate);
        if(txtDate != null){
            txtDate.setTextColor(Color.GRAY);
        }
    }

    public void ajustaTamanho(int c, ListView lista){
        int tam = c * 56;
        float scale = getResources().getDisplayMetrics().density;

        ViewGroup.LayoutParams params = lista.getLayoutParams();
        params.height = (int) (tam * scale);
        lista.setLayoutParams(params);
        lista.requestLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_actions, menu);

        MenuItem mitDoneSerie = menu.findItem(R.id.mitDoneSerie);
        MenuItem mitDeleteSerie = menu.findItem(R.id.mitDeleteSerie);

        mitDoneSerie.setEnabled(itemsEnabled);
        mitDoneSerie.setVisible(itemsEnabled);
        mitDeleteSerie.setEnabled(itemsEnabled);
        mitDeleteSerie.setVisible(itemsEnabled);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.mitDeleteSerie:
                deleteSeries();
                break;
            case R.id.mitDoneSerie:
                deleteSeries();
               // addDone();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteSeries(){
        for(int i = 0; i < itemsSelected.size(); i++){
            final TextView txtDescriptionSerie = (TextView) livSeries.getChildAt(i).findViewById(R.id.txtDescription);
            FirebaseDatabase fb = FirebaseDatabase.getInstance();
            DatabaseReference ref = fb.getReference("users/" + user.getUid() + "/serie");

            //String sDate = MyDate.getMyDate(new Date());

           // if(sDate != null){

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap : dataSnapshot.getChildren()){
                            if(snap.child("description").getValue().toString().equals(txtDescriptionSerie.getText().toString())){
                                snap.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
           // }else{
                //Toast.makeText(this, getString(R.string.anErrorOcurred), Toast.LENGTH_SHORT).show();
            //}
        }

    }

    public void doneSeries(){
        for(int i = 0; i < itemsSelected.size(); i++){
            final TextView txtDescriptionSerie = (TextView) livSeries.getChildAt(i).findViewById(R.id.txtDescription);
            FirebaseDatabase fb = FirebaseDatabase.getInstance();
            DatabaseReference ref = fb.getReference("users/" + user.getUid() + "/serie");

           // String sDate = MyDate.getMyDate(new Date());
           // if(sDate != null){

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap : dataSnapshot.getChildren()){
                            if(snap.child("description").getValue().toString().equals(txtDescriptionSerie.getText().toString())){
                                MyList list = snap.getValue(MyList.class);
                                list.setDone(true);
                                snap.getRef().setValue(list);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
           // }else{
            //    Toast.makeText(this, getString(R.string.anErrorOcurred), Toast.LENGTH_SHORT).show();
            //}
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.close)
                    .setMessage(R.string.closeMsg)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //if (id == R.id.navConfig) {
            //startActivity(new Intent(MainActivity.this, ConfigActivity.class));

        //} else
        if (id == R.id.navSignout) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.signout)
                    .setMessage(R.string.signoutConfirm)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            signOut();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut(){
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }


    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        nvHeader = navigationView.getHeaderView(0);
        imgUserPhoto = (ImageView) nvHeader.findViewById(R.id.imgUserPhoto);
        txtUserName = (TextView) nvHeader.findViewById(R.id.txtUserName);
        txtUserEmail = (TextView) nvHeader.findViewById(R.id.txtUserEmail);

        livSeries = (ListView) findViewById(R.id.livSeries);

    }


    public void infoUser(){
        Picasso.with(this).load(user.getPhotoUrl()).into(imgUserPhoto);
        txtUserName.setText(user.getDisplayName());
        txtUserEmail.setText(user.getEmail());
    }


}
