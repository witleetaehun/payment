package com.GalleryAuction.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.GalleryAuction.Adapter.ArtistAuctionDetailAdapter;
import com.geno.bill_folder.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.GalleryAuction.Client.ImageViewItem.getImageBitmap;
import static com.GalleryAuction.Client.TagInfoClient.toHexString;
import static com.GalleryAuction.Item.HttpClientItem.ArtInfo;
import static com.GalleryAuction.Item.HttpClientItem.AuctionDetailInfo;
import static com.GalleryAuction.Item.HttpClientItem.AuctionRemove;

public class ArtistAuctionDetailInfoUi extends Activity implements View.OnClickListener {
    String nowbidding, userid, end, time, auckey, image;
    ListView listView;
    Button btn1, btn2;
    NfcAdapter  nfcAdapter;
    PendingIntent pendingIntent;
    ImageView iv;
    Bitmap bmImg;
    back task;
    String imgUrl = "http://221.156.54.210:8989/NFCTEST/art_images/";

    ArtistAuctionDetailAdapter adapter = new ArtistAuctionDetailAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_artistauction_detail_info);
        listView = (ListView)findViewById(R.id.DetailInfo_listview) ;
        btn1 = (Button)findViewById(R.id.artistauction_detail_btn);
        btn2 = (Button)findViewById(R.id.artistauction_detail_btn_exit);
        iv = (ImageView)findViewById(R.id.artistimage);
        Intent intent = getIntent();
        auckey = intent.getStringExtra("auckey");
        image = intent.getStringExtra("image");
        task = new back();
        task.execute(imgUrl+image);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent0 = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent0, 0);
        try {
            JSONArray ja = new JSONArray(AuctionDetailInfo(auckey));

            for (int i = 0 ; i  < ja.length(); i++){

                JSONObject job = (JSONObject) ja.get(i);
                userid = job.get("user_id").toString();
                nowbidding = job.get("bid_price").toString();
                time = job.get("bid_con_time").toString();
                adapter.addItem(nowbidding, userid, time, getImageBitmap(imgUrl+"bidder.png"));
                Log.d("aaaa" , nowbidding);
                Log.d("aaaa" , userid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.artistauction_detail_btn:
                AlertDialog.Builder alert = new AlertDialog.Builder(ArtistAuctionDetailInfoUi.this);
                alert.setMessage("취소하시겠습니까?").setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    AuctionRemove(auckey);
                        finish();
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertdialog = alert.create();
                alertdialog.show();


                break;
            case R.id.artistauction_detail_btn_exit :
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            Toast.makeText(this, "구매자 ID가 아닙니다.", Toast.LENGTH_SHORT).show();

        }
    }
    private class back extends AsyncTask<String, Integer,Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try{
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);

            }catch(IOException e){
                e.printStackTrace();
            }
            return bmImg;
        }
        protected void onPostExecute(Bitmap img){
            iv.setImageBitmap(bmImg);
        }
    }

}
