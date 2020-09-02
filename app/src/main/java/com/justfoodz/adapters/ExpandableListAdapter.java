package com.justfoodz.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.justfoodz.R;


public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    ArrayList<String> category_main_icon;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        //this.category_main_icon = ii;
    }

//    public ExpandableListAdapter(Context context, ArrayList<String> cc) {
//        this._context = context;
//        this.category_main_models = cc;
//
//    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        String splitedchild [] = childText.split("\\*");

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.single_extra_item_list, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.tv_item_size);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
        // return this.category_main_models.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
        // return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);




       // String splited [] = headerTitle.split("\\*");


        //  String imgtitle = (String) getGroup(groupPosition);
        GroupHolder groupHolder = new GroupHolder();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }


        groupHolder.lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        // groupHolder.lblListHeader.setTypeface(null, Typeface.BOLD);
        groupHolder.lblListHeader.setText(headerTitle);


    //    groupHolder.icon = (ImageView) convertView.findViewById(R.id.icon);

       // Picasso.get().load(""+splited[1]+splited[2]).into(groupHolder.icon);

//        if (isExpanded) {
//            groupHolder.img.setImageResource(R.drawable.group_up);
//        } else {
//            groupHolder.img.setImageResource(R.drawable.group_down);
//        }

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

    public class GroupHolder
    {
        TextView lblListHeader;
        ImageView img,icon;

    }
}
