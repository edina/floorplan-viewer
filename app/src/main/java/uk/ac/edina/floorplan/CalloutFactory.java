package uk.ac.edina.floorplan;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by murrayking on 29/07/2015.
 */
public class CalloutFactory {


    public View createCallout(final Activity activity, final Area area) {


        // create a simple callout
        View callout = LayoutInflater.from(activity).inflate(R.layout.callout_layout, null);

        TextView calloutTitle = (TextView)callout.findViewById(R.id.calloutTitle);
        calloutTitle.setText(area.getTitle());
        TextView calloutDescription = (TextView)callout.findViewById(R.id.calloutDescription);
        calloutDescription.setText(area.getDescription());

        Button detailsButton = (Button)callout.findViewById(R.id.areaDetails);
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewDetails = new Intent(activity, AreaDetailVideoView.class);

                viewDetails.putExtra(PlacesFragment.AREA_KEY, area);

                activity.startActivity(viewDetails);

            }
        });
        return callout;
    }
}

