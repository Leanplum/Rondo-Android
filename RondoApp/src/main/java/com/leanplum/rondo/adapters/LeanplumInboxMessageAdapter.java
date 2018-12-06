package com.leanplum.rondo.adapters;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leanplum.LeanplumInboxMessage;
import com.leanplum.rondo.R;
import com.leanplum.rondo.models.LeanplumApp;

import java.util.ArrayList;

public class LeanplumInboxMessageAdapter extends ArrayAdapter<LeanplumInboxMessage> {
    public LeanplumInboxMessageAdapter(Context context, ArrayList<LeanplumInboxMessage> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final LeanplumInboxMessage message = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_inbox_message, parent, false);
        }
        // Lookup view for data population
        TextView title = convertView.findViewById(R.id.title);
        title.setText(message.getTitle());
        TextView subtitle = convertView.findViewById(R.id.subtitle);
        subtitle.setText(message.getSubtitle());
        ImageView image = convertView.findViewById(R.id.image);
        image.setImageURI(message.getImageUrl());
        Button button = convertView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                message.read();
            }
        });
        return convertView;
    }
}

