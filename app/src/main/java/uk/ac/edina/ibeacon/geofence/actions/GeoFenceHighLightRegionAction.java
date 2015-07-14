package uk.ac.edina.ibeacon.geofence.actions;

/**
 * Created by murray on 15/10/14.
 */
public class GeoFenceHighLightRegionAction implements GeoFenceAction {
    /*
    private final MapView mapView;
    private Activity activity;
    Utils utils = Utils.getInstance();

    public GeoFenceHighLightRegionAction(Activity activity, MapView mapView){
        this.activity = activity;
        this.mapView = mapView;
    }
    */
    @Override
    public void onEnter() {
        highLightEdinaMeetingRoom();
    }

    private void highLightEdinaMeetingRoom() {
        /*
        File edinaMeetingroomKml = utils.copyFileFromAssets("meetingroom.kml", activity.getAssets(), activity.getPackageName());

        final KmlDocument kmlDocument = new KmlDocument();
        boolean success = kmlDocument.parseKMLFile(edinaMeetingroomKml);

        if(success) {

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    final FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, null, null, kmlDocument);
                    mapView.getOverlays().add(kmlOverlay);
                    final BoundingBoxE6 bb = kmlDocument.mKmlRoot.getBoundingBox();
                    mapView.getController().setZoom(22);
                    mapView.getController().animateTo(bb.getCenter());


                }
            });
        }
        */
    }

    @Override
    public void onLeave() {
        /*
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mapView.getController().setZoom(18);

            }
        });*/
    }
}
