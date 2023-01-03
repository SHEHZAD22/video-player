package com.shehzad.gifsvideo.adapter;

import static android.content.ContentValues.TAG;
import static android.content.Context.DOWNLOAD_SERVICE;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shehzad.gifsvideo.LookBasedActivity;
import com.shehzad.gifsvideo.R;
import com.shehzad.gifsvideo.VidActivity;
import com.shehzad.gifsvideo.model.GifsModel;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    BottomSheetDialog bottomSheetDialog;
    Context context;
    List<GifsModel> gifsList;

    public MyAdapter(Context context, List<GifsModel> gifsList) {
        this.context = context;
        this.gifsList = gifsList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<GifsModel> filteredList) {
        gifsList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        GifsModel model = gifsList.get(position);
        holder.title.setText(model.getTitle());
        holder.likes.setText("Likes:" + model.getLikes());
        holder.tags.setText("Tags: " + model.getTags());

        if (model.getImage() != null) {
            Glide.with(context).load(model.getImage()).into(holder.image);
        }

        holder.more.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
            View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.video_bottom_sheet_layout, v.findViewById(R.id.bottom_sheet));

            //share intent
            bottomSheetView.findViewById(R.id.bs_share).setOnClickListener(view2 -> {
                String link = model.getVideourl();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, link);
                context.startActivity(Intent.createChooser(shareIntent, "Share video link via"));
                bottomSheetDialog.dismiss();
            });

            //download section
            bottomSheetView.findViewById(R.id.bs_download).setOnClickListener(view4 -> {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Download");
                String url = model.getVideourl();
                String title = model.getTitle();
                alertDialog.setMessage(R.string.downloadRef);

                alertDialog.setPositiveButton("OK", ((dialog, i) -> {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    String halfTitle = getString(title);
                    request.setTitle(halfTitle + ".mp4");
                    request.setDescription("Downloading Please wait....");
                    String cookie = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie",cookie);
                    request.setAllowedOverMetered(true);
                    request.setVisibleInDownloadsUi(false);

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, halfTitle+".mp4");
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    downloadManager.enqueue(request);

                    Toast.makeText(context, "Downloading Started.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }));

                alertDialog.setNegativeButton("Don't Save", (dialog, i) -> dialog.dismiss());
                alertDialog.show();
                bottomSheetDialog.dismiss();
            });

            //look base section
            bottomSheetView.findViewById(R.id.bs_lookBase).setOnClickListener(view5 -> {
                Intent lookIntent = new Intent(context, LookBasedActivity.class);
                lookIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                lookIntent.putExtra("url", model.getVideourl());
                context.startActivity(lookIntent);
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();

        });


        holder.itemView.setOnClickListener(holderView -> {
            Intent intent = new Intent(context, VidActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("url", model.getVideourl());
            context.startActivity(intent);
        });


    }

    private String getString( String s) {
        final int mid = s.length() / 2;
        String parts[] = {s.substring(0,mid), s.substring(mid)};
        Log.d(TAG, "getString: " + parts[0]);
        return parts[0];
    }

    @Override
    public int getItemCount() {
        if (gifsList != null) {
            return gifsList.size();
        }
        return 0;
    }

    //this method is for searchView
    public void filter(String query, ArrayList<GifsModel> list) {
        //creating new list to filter our data
        ArrayList<GifsModel> filteredList = new ArrayList<>();

        //comparing the qury with the Model data
        for (GifsModel model : list) {
            if (
                    model.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            model.getTags().toLowerCase().contains(query.toLowerCase()) ||
                            model.getModel().toLowerCase().contains(query.toLowerCase()) ||
                            model.getChannel().toLowerCase().contains(query.toLowerCase())) {

                filteredList.add(model);
            }
        }
        if (filteredList.isEmpty())
            Toast.makeText(context, "No Data Found...", Toast.LENGTH_SHORT).show();
        else filterList(filteredList);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tags, likes, title;
        ImageView image, more;

        public MyViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.textViewTitle);
            tags = view.findViewById(R.id.tags);
            likes = view.findViewById(R.id.likes);
            image = view.findViewById(R.id.image);
            more = view.findViewById(R.id.more);
        }
    }
}


