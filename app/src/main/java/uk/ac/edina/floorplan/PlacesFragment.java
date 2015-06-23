package uk.ac.edina.floorplan;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by murrayking on 19/06/2015.
 */
public class PlacesFragment extends ListFragment {



    public static final String ROUTE_CHOSEN_KEY = "ROUTE_CHOSEN_KEY";




    // True or False depending on if we are in horizontal or duel pane mode
    boolean dualPane;

    // Currently selected item in the ListView
    SingleRow selectedRow;


    MyAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // 1. Access the TextView defined in layout XML
        // and then set its text
        adapter = new MyAdapter(this.getActivity());

        setListAdapter(adapter);




        // Check if the FrameLayout with the id details exists
        View detailsFrame = getActivity().findViewById(R.id.details);

        // Set mDuelPane based on whether you are in the horizontal layout
        // Check if the detailsFrame exists and if it is visible
        dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        // If the screen is rotated onSaveInstanceState() below will store the // hero most recently selected. Get the value attached to curChoice and // store it in mCurCheckPosition
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            selectedRow = (SingleRow)savedInstanceState.getSerializable(PlacesFragment.ROUTE_CHOSEN_KEY);

        }
        //first usage and no selection made
        if(selectedRow == null){
            selectedRow =(SingleRow)adapter.getItem(0);
        }

        if (dualPane) {
            // CHOICE_MODE_SINGLE allows one item in the ListView to be selected at a time
            // CHOICE_MODE_MULTIPLE allows multiple
            // CHOICE_MODE_NONE is the default and the item won't be highlighted in this case'
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // Send the item selected to showDetails so the right hero info is shown

            goToMap(selectedRow);
        }

    }

    private void goToMap(SingleRow row){
        // create an Intent to take you over to a new DetailActivity

        if (dualPane) {

            // Make the currently selected item highlighted
            // getListView().setItemChecked(, true);

            // Create an object that represents the current FrameLayout that we will
            // put the hero data in


            // When a DetailsFragment is created by calling newInstance the index for the data
            // it is supposed to show is passed to it. If that index hasn't been assigned we must
            // assign it in the if block


            // Make the details fragment and give it the currently selected hero index
            MainActivity details = MainActivity.newInstance(row);

            // Start Fragment transactions
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            // Replace any other Fragment with our new Details Fragment with the right data
            ft.replace(R.id.details, details);

            // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();


        } else {

            Intent detailIntent = new Intent(this.getActivity(), DetailActivity.class);

            detailIntent.putExtra(ROUTE_CHOSEN_KEY, row);

            this.getActivity().startActivity(detailIntent);
        }
    }

    // Called every time the screen orientation changes or Android kills an Activity
    // to conserve resources
    // We save the last item selected in the list here and attach it to the key curChoice
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PlacesFragment.ROUTE_CHOSEN_KEY, selectedRow);
    }




    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        selectedRow =(SingleRow)adapter.getItem(position);
        goToMap(selectedRow);
    }

    static class ImagePixelLocation implements Serializable{
        private int x;
        private int y;

        public ImagePixelLocation(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    static class SingleRow implements Serializable {

        private String title;
        private int imageId;
        private String description;
        private ImagePixelLocation point;

        public int getImageId() {
            return imageId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public ImagePixelLocation getPoint(){ return  point;}

        SingleRow(String title, int imageId, String description, String locations) {
            this.title = title;
            this.imageId = imageId;
            this.description = description;
            String[] p = locations.split(",");
            int x = Integer.valueOf(p[0]);
            int y = Integer.valueOf(p[1]);
            point = new ImagePixelLocation(x,y);
        }


    }

    class  MyAdapter extends BaseAdapter {

        private final Context context;
        ArrayList<SingleRow> rows = new ArrayList<>();

        MyAdapter(Context context) {
            this.context = context;
            Resources resources = context.getResources();
            String[] titles = resources.getStringArray(R.array.titles);
            String[] descriptions = resources.getStringArray(R.array.descriptions);

            String[] locations = resources.getStringArray(R.array.locations);
            TypedArray icons = resources.obtainTypedArray(R.array.route_list_icons);


            for (int i = 0; i < titles.length; i++) {
                rows.add(new SingleRow(titles[i], icons.getResourceId(i, -1), descriptions[i], locations[i]));
            }

        }

        ;

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
            TextView description = (TextView) row.findViewById(R.id.textDescription);
            ImageView imageView = (ImageView) row.findViewById(R.id.trailIcon);
            SingleRow singleRow = rows.get(i);

            title.setText(singleRow.getTitle());
            description.setText(singleRow.getDescription());
            imageView.setImageResource(singleRow.getImageId());
            return row;
        }
    }

}
