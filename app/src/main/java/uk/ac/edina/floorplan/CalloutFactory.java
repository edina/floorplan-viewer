package uk.ac.edina.floorplan;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by murrayking on 29/07/2015.
 */
public class CalloutFactory {


    public View createCallout(final Activity activity, final Area area) {


        // create a simple callout
        View callout = LayoutInflater.from(activity).inflate(R.layout.callout_layout, null);

        TextView calloutTitle = (TextView)callout.findViewById(R.id.calloutTitle);
        AssetManager am = activity.getApplicationContext().getAssets();

        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.ENGLISH, "fonts/%s", "SourceSansPro-Regular.ttf"));

        calloutTitle.setTypeface(typeface);
        calloutTitle.setText(area.getTitle());
        ImageView calloutDescription = (ImageView)callout.findViewById(R.id.calloutIcon);
        calloutDescription.setImageResource(area.getImageId());

        Button detailsButton = (Button)callout.findViewById(R.id.areaDetails);
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewDetails = new Intent(activity, AreaDetailVideoViewActivity.class);

                viewDetails.putExtra(AreasListActivity.AREA_KEY, area);

                activity.startActivity(viewDetails);

            }
        });
        return callout;
    }
}

