package org.projects.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "com.example.StateChange";

    ArrayAdapter<Product> adapter;
    ListView listView;
    ArrayList<Product> bag = new ArrayList<Product>();


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    Product lastDeletedProduct;
    int lastDeletedPosition;
    public void saveCopy()
    {
        lastDeletedPosition = listView.getCheckedItemPosition();
        lastDeletedProduct = bag.get(lastDeletedPosition);
    }

    public void showDialog() {
        //showing our dialog.
        ClearAllDialog dialog = new ClearAllDialog() {
            @Override
            protected void positiveClick() {
                //Here we override the methods and can now
                //do something
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Shopping list cleared", Toast.LENGTH_LONG);
                bag.clear();
                getMyAdapter().notifyDataSetChanged();
                toast.show();
            }

            @Override
            protected void negativeClick() {
                //Here we override the method and can now do something
                Toast toast = Toast.makeText(getApplicationContext(),
                        "negative button clicked", Toast.LENGTH_SHORT);
                toast.show();
            }
        };
        dialog.show(getFragmentManager(), "MyFragment");
    }

    public ArrayAdapter getMyAdapter() {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            bag = savedInstanceState.getParcelableArrayList("MyList");

        }
        Log.i(TAG, "onCreate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        Toast.makeText(
                this,
                "Welcome back: " + email, Toast.LENGTH_SHORT).show();
        //getting our listiew - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);
        //here we create a new adapter linking the bag and the
        //listview
        adapter = new ArrayAdapter<Product>(this, android.R.layout.simple_list_item_checked, bag);

        //setting the adapter on the listview
        listView.setAdapter(adapter);
        //here we set the choice mode - meaning in this case we can
        //only select one item at a time.
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



        Button addButton = (Button) findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText bagInput = (EditText) findViewById(R.id.inputBag);
                String bagText = bagInput.getText().toString();


                EditText bagQuantity = (EditText) findViewById(R.id.inputQuantity);
                String bagNumber = bagQuantity.getText().toString();
                Spinner spinners = (Spinner) findViewById(R.id.spinner1);
                String volume = String.valueOf(spinners.getSelectedItem());
                int bagQu = Integer.parseInt(bagNumber);

                //bag.add(String.valueOf(bagQu) + ' ' + volume + ' ' + bagText);
                bag.add(new Product(bagQu, volume, bagText));
                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();
            }
        });

        final Button clrButton = (Button) findViewById(R.id.clrButton);

        clrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });

        Button dltButton = (Button) findViewById(R.id.dltButton);

        dltButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                saveCopy();
                for (int i = adapter.getCount() - 1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        // This item is checked and can be removed
                        bag.remove(adapter.getItem(i));
                    }
                }
                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();

                final View parent = listView;
                Snackbar snackbar = Snackbar
                        .make(parent, "Item Deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bag.add(lastDeletedPosition,lastDeletedProduct);
                                getMyAdapter().notifyDataSetChanged();
                                Snackbar snackbar = Snackbar.make(parent, "Item restored!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        });

                snackbar.show();
                getMyAdapter().notifyDataSetChanged();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1) //exited our preference screen
        {
            Toast toast =
                    Toast.makeText(getApplicationContext(), "back from preferences", Toast.LENGTH_LONG);
            toast.setText("back from our preferences");
            toast.show();
            //here you could put code to do something.......
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setPreferences() {
        //Here we create a new activity and we instruct the
        //Android system to start it
        Intent intent = new Intent(this, SettingsActivity.class);
        //startActivity(intent); //this we can use if we DONT CARE ABOUT RESULT

        //we can use this, if we need to know when the user exists our preference screens
        startActivityForResult(intent, 1);
    }

    public void getPreferences() {

        //We read the shared preferences from the

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //We we set that we want to use the xml file
        //under the menu directory in the resources and
        // that we want to use the specific file called "main.xml"
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Toast.makeText(this, "Application icon clicked!",
                        Toast.LENGTH_SHORT).show();
                return true; //return true, means we have handled the event
            case R.id.item_settings:
                setPreferences();
                Toast.makeText(this, "About item clicked!", Toast.LENGTH_SHORT)
                        .show();
                return true;
            case R.id.clear_all:
                showDialog();
                Toast.makeText(this, "Help item clicked!", Toast.LENGTH_SHORT)
                        .show();
                return true;

            case R.id.share_list:
                String finalList = convertListToString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, finalList);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

        }

        return false; //we did not handle the event
    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.projects.shoppinglist/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout rl = (LinearLayout)findViewById(R.id.container);
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);

        int bgc = Color.parseColor(prefs.getString("background_color", "#FFFFFF"));
        rl.setBackgroundColor(bgc);



    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // savedInstanceState.putStringArrayList("SaveList", bag);
        savedInstanceState.putParcelableArrayList("MyList", bag);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.projects.shoppinglist/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
