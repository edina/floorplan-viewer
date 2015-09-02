package uk.ac.edina.floorplan;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by murrayking on 19/06/2015.
 */
public class AreasListActivity extends FloorPlanBaseActivity {



    public static final String AREA_KEY = "AREA_KEY";


    // Currently selected item in the ListView
    Area selectedRow;


    MyAdapter adapter;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        listView = (ListView) findViewById(R.id.areasList);

        // 1. Access the TextView defined in layout XML
        // and then set its text
        adapter = new MyAdapter(this);

        listView.setAdapter(adapter);



        if (savedInstanceState != null) {
            // Restore last state for checked position.
            selectedRow = (Area)savedInstanceState.getSerializable(AreasListActivity.AREA_KEY);

        }
        //first usage and no selection made
        if(selectedRow == null){
            selectedRow =(Area)adapter.getItem(0);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final Area area = (Area) parent.getItemAtPosition(position);
                Intent detailIntent = new Intent(AreasListActivity.this, FloorPlanViewActivity.class);

                detailIntent.putExtra(AREA_KEY, area);

                startActivity(detailIntent);
            }

        });

    }

    @Override
    public boolean acceptBeaconNotifications() {
        return false;
    }

    @Override
    public boolean removeNavigationHistory() {
        return false;
    }

    // Called every time the screen orientation changes or Android kills an Activity
    // to conserve resources
    // We save the last item selected in the list here and attach it to the key curChoice
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(AreasListActivity.AREA_KEY, selectedRow);
    }



    class  MyAdapter extends BaseAdapter {

        private final Context context;
        List<Area> rows;

        MyAdapter(Context context) {
            this.context = context;
            Resources resources = context.getResources();
            rows = FloorPlanAreas.getAreas(resources);

        };

        @Override
        public int getCount() {
            return rows.size();
        }

        @Override
        public Object getItem(int i) {
            return rows.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.places_row_layout, viewGroup, false);
            TextView title = (TextView) row.findViewById(R.id.textTitle);

            AssetManager am = context.getApplicationContext().getAssets();

            Typeface typeface = Typeface.createFromAsset(am,
                    String.format(Locale.ENGLISH, "fonts/%s", "SourceSansPro-Regular.ttf"));

            title.setTypeface(typeface);

            //TextView description = (TextView) row.findViewById(R.id.textDescription);
            ImageView imageView = (ImageView) row.findViewById(R.id.trailIcon);
            Area area = rows.get(i);

            title.setText(area.getTitle());
            //description.setText(area.getDescription());
            imageView.setImageResource(area.getImageId());
            return row;
        }
    }

}
