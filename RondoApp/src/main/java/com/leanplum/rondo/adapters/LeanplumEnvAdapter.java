package com.leanplum.rondo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.leanplum.rondo.R;
import com.leanplum.rondo.models.LeanplumEnv;

import java.util.ArrayList;

public class LeanplumEnvAdapter extends ArrayAdapter<LeanplumEnv> {
    public LeanplumEnvAdapter(Context context, ArrayList<LeanplumEnv> apps) {
        super(context, 0, apps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        LeanplumEnv app = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_leanplum_env, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(app.getApiHostName());
        return convertView;
    }
}

