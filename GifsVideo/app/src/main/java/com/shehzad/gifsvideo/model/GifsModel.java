package com.shehzad.gifsvideo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GifsModel implements Parcelable {
    private int id;
    private String image;
    private String title;
    private String videourl;
    private String tags;
    private int likes;
    private String model;
    private int page;
    private String channel;

    public GifsModel(int id, String image, String title, String videourl, String tags, int likes, String model, int page, String channel) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.videourl = videourl;
        this.tags = tags;
        this.likes = likes;
        this.model = model;
        this.page = page;
        this.channel = channel;
    }

    public GifsModel(String videourl, String image, String  title){
        this.videourl = videourl;
        this.image = image;
        this.title = title;
    }

    protected GifsModel(Parcel in) {
        id = in.readInt();
        image = in.readString();
        title = in.readString();
        videourl = in.readString();
        tags = in.readString();
        likes = in.readInt();
        model = in.readString();
        page = in.readInt();
        channel = in.readString();
    }

    public static final Creator<GifsModel> CREATOR = new Creator<GifsModel>() {
        @Override
        public GifsModel createFromParcel(Parcel in) {
            return new GifsModel(in);
        }

        @Override
        public GifsModel[] newArray(int size) {
            return new GifsModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(image);
        parcel.writeString(title);
        parcel.writeString(videourl);
        parcel.writeString(tags);
        parcel.writeInt(likes);
        parcel.writeString(model);
        parcel.writeInt(page);
        parcel.writeString(channel);
    }
}
