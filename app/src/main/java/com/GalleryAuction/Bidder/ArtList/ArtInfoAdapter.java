package com.GalleryAuction.Bidder.ArtList;
import java.util.ArrayList;
import java.util.PriorityQueue;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geno.bill_folder.R;

public class ArtInfoAdapter extends BaseAdapter {
    // 문자열 보관 ArrayList
    private ArrayList<ArtInfoItem> listViewItemList = new ArrayList<ArtInfoItem>() ;
    TextView tv1, tv2, tv3;

    public ArtInfoAdapter() {

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.galleryartinfo_listview_item, parent, false);
        }
        tv1 = (TextView) convertView.findViewById(R.id.artinfoitem1_txt);
        tv2 = (TextView) convertView.findViewById(R.id.artinfoitem2_txt);
        ArtInfoItem artInfoItem = listViewItemList.get(position);
        tv1.setText(artInfoItem.getIcon());
        tv2.setText(artInfoItem.getTitle());
        //경매 X
        if (artInfoItem.getAuckey() == "0") {
            tv1.setTextColor(Color.BLACK);
            tv2.setTextColor(Color.BLACK);
        }
        else if (artInfoItem.getAuckey() == "0" && artInfoItem.getBidkey() != "0"){
            //경매취소 ,마감 (한번이라도 경매했을때 )
            tv1.setTextColor(Color.BLACK);
            tv2.setTextColor(Color.BLACK);
        }
        else if (artInfoItem.getAuckey() == "2" &&artInfoItem.getBidkey() == "0"){
            //경매중일때
            tv1.setTextColor(Color.BLUE);
            tv2.setTextColor(Color.BLUE);
        } else if (artInfoItem.getAuckey() == "1") {
            tv1.setTextColor(Color.GREEN);
            tv2.setTextColor(Color.GREEN);
        } else if (artInfoItem.getAuckey() == "2" && artInfoItem.getBidkey() != "0") {
                //입찰했을때
                tv1.setTextColor(Color.YELLOW);
                tv2.setTextColor(Color.YELLOW);
            }







        return convertView;
    }

    public void addItem(String icon, String title, String auckey, String bidkey) {
        ArtInfoItem item = new ArtInfoItem();

        item.setIcon(icon);
        item.setTitle(title);
        item.setAuckey(auckey);
        item.setBidkey(bidkey);
        listViewItemList.add(item);
    }


    public void removeitem(String icon, String title, String auckey) {
        ArtInfoItem item = new ArtInfoItem();
        item.setIcon(icon);
        item.setTitle(title);
        item.setAuckey(auckey);
        listViewItemList.remove(item);
    }

    public void refreshAdapter(ArrayList<ArtInfoItem> items) {

        this.listViewItemList.clear();
        this.listViewItemList.addAll(items);
        notifyDataSetChanged();

    }
}
