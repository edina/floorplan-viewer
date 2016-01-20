package uk.ac.edina.floorplan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailCardViewActivity extends FloorPlanBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_card_view);

        Intent previousIntent = getIntent();

        Area area = (Area)previousIntent.getSerializableExtra(AreasListActivity.AREA_KEY);

        ImageView cardViewImage = (ImageView) findViewById(R.id.cardViewImage);
        cardViewImage.setImageResource(area.getCardViewImageId());
        TextView cardViewTitle = (TextView) findViewById(R.id.cardViewTitle);
        cardViewTitle.setText(area.getTitle());
        TextView cardViewDetail = (TextView) findViewById(R.id.cardViewDetails);
        cardViewDetail.setText(area.getCardViewDetail());


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
