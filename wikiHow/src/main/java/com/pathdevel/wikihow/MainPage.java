package com.pathdevel.wikihow;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class MainPage extends Activity {
    CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); //for images loading
        setContentView(R.layout.main_page);

        ListView lvMain = (ListView) findViewById(R.id.lvMain); //ListView for displaying articles
        lvMain.addHeaderView(new View(this)); //these are for a little space in the beginning
        lvMain.addFooterView(new View(this)); //and the end of the list

        new LoadFeatured(MainPage.this, lvMain, cardAdapter).execute(); //start loading articles itself

        // when item is selected...
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article a = (Article) adapterView.getItemAtPosition(i);

                // just a quick-check
                if (a != null) {
                    Toast.makeText(MainPage.this, a.url, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
