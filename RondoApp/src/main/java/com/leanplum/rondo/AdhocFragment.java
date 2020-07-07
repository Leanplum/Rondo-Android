package com.leanplum.rondo;

import android.text.TextUtils;
import androidx.fragment.app.Fragment;;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.leanplum.Leanplum;

import java.util.HashMap;
import java.util.Map;

public class AdhocFragment extends Fragment {

    AdhocPersistence persistence;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_adhoc, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        persistence = new AdhocPersistence(getContext());

        initButtons();
        loadPersistedData();
    }

    private void loadPersistedData() {
        ((EditText)getView().findViewById(R.id.trackName)).setText(persistence.loadSavedEvent());
        ((EditText)getView().findViewById(R.id.stateName)).setText(persistence.loadSavedState());
    }

    private void sendTrackEvent() {
        String eventName = ((EditText)getView().findViewById(R.id.trackName))
            .getText().toString();
        String paramKey = ((EditText)getView().findViewById(R.id.paramKey))
                .getText().toString().trim();
        String paramValue = ((EditText)getView().findViewById(R.id.paramValue))
                .getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        params.put(paramKey, paramValue);
        if (paramKey != null && paramValue != null) {
            Leanplum.track(eventName.trim(), params);
        } else {
            Leanplum.track(eventName.trim());
        }
        // TODO: figure out how to alert event response/status

        persistence.saveEvent(eventName);
    }

    private void sendState() {
        String stateName = ((EditText)getView().findViewById(R.id.stateName))
                .getText().toString().trim();
        String paramKey = ((EditText)getView().findViewById(R.id.stateParamKey))
                .getText().toString().trim();
        String paramValue = ((EditText)getView().findViewById(R.id.stateParamValue))
                .getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(paramKey) && !TextUtils.isEmpty(paramValue)) {
            params.put(paramKey, paramValue);
            Leanplum.advanceTo(stateName, params);
        } else {
            Leanplum.advanceTo(stateName);
        }
        // TODO: figure out how to alert state response/status

        persistence.saveState(stateName);
    }

    private void sendUserAttr() {
        String attrKey = ((EditText)getView().findViewById(R.id.attrKey))
                .getText().toString();
        String attrValue = ((EditText)getView().findViewById(R.id.attrValue))
                .getText().toString();
        Map attrib = new HashMap();
        attrib.put(attrKey.trim(), attrValue.trim());
        Leanplum.setUserAttributes(attrib);
        // TODO: figure out how to alert state response/status

    }

  private void setUserId() {
    String userId = ((EditText)getView().findViewById(R.id.userIdKey)).getText().toString();
    Leanplum.setUserId(userId.trim());
  }

  private void forceContentUpdate() {
    Leanplum.forceContentUpdate();
  }

    private void setDeviceLocation() {
        Float latitude = Float.parseFloat(
                ((EditText)getView().findViewById(R.id.locLatitude)).getText().toString());
        Float longitude = Float.parseFloat(
                ((EditText)getView().findViewById(R.id.locLongitude)).getText().toString());

        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        Leanplum.setDeviceLocation(location);
        // TODO: figure out how to alert state response/status

    }

    private void setDeviceLocation(Intent data) {
//        Place place = PlacePicker.getPlace(this, data);
//        Location location = new Location(LocationManager.GPS_PROVIDER);
//        location.setLatitude(place.getLatLng().latitude);
//        location.setLongitude(place.getLatLng().longitude);
//        Leanplum.setDeviceLocation(location);
    }

    private void initButtons() {
        getView().findViewById(R.id.buttonTrack)
            .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendTrackEvent();
                }
            });

        getView().findViewById(R.id.buttonState)
            .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendState();
                }
            });

        getView().findViewById(R.id.buttonUserAttr)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendUserAttr();
                    }
                });

        getView().findViewById(R.id.buttonDeviceLocation)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDeviceLocation();
                    }
                });

        getView().findViewById(R.id.buttonUserId)
            .setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                setUserId();
              }
            });

        getView().findViewById(R.id.buttonForceContentUpdate)
            .setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                forceContentUpdate();
              }
            });
/*
        findViewById(R.id.buttonPlacePicker)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPlacePicker();
                    }
                });
*/
    }
}
