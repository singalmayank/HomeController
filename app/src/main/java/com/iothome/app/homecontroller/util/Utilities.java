package com.iothome.app.homecontroller.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.iothome.app.homecontroller.R;
import com.iothome.app.homecontroller.model.Thing;

/*
Utility methods go here
 */
public class Utilities {
    public static View createThingView(View convertView, Thing thing, Context context){
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.things_list_item, null);
        }

        TextView txtListChild = convertView.findViewById(R.id.room_thing);
        txtListChild.setText(thing.getThingDisplayName());

        ToggleButton tb = convertView.findViewById(R.id.room_thing_state);
        if (Constants.ON.equals(thing.getCurrentState())) {
            tb.setChecked(true);
        } else {
            tb.setChecked(false);
        }
        tb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(v.getContext(), "Switch state updated for : ." + v.getId(),
                        Toast.LENGTH_SHORT).show();            }
        });
        return convertView;
    }
}
