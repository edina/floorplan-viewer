package uk.ac.edina.floorplan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailCardViewActivity extends FloorPlanBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_card_view);

        Intent previousIntent = getIntent();

        Area area = (Area)previousIntent.getSerializableExtra(AreasListActivity.AREA_KEY);

        TextView areaDetailsTitle = (TextView) findViewById(R.id.areaDetailVideoTitle);
        areaDetailsTitle.setText(area.getTitle());
    }

    @Override
    public boolean acceptBeaconNotifications() {
        return false;
    }

    @Override
    public boolean removeNavigationHistory() {
        return false;
    }

}
