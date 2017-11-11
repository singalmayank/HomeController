package com.iothome.app.homecontroller.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.iothome.app.homecontroller.R;
import com.iothome.app.homecontroller.model.Thing;

import java.util.HashMap;
import java.util.List;


public class ThingsExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _roomHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Thing>> _listDataChild;

    public ThingsExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Thing>> listChildData) {
        this._context = context;
        this._roomHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._roomHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Thing thing = (Thing) getChild(groupPosition, childPosition);

        return Utilities.createThingView(convertView, thing, this._context);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._roomHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._roomHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._roomHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.things_list_header, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.room);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}