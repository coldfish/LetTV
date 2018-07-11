package io.coldfish.lettv.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class TVShow implements Parcelable {
    public static final Parcelable.Creator<TVShow> CREATOR = new Parcelable.Creator<TVShow>() {
        @Override
        public TVShow createFromParcel(Parcel source) {
            return new TVShow(source);
        }

        @Override
        public TVShow[] newArray(int size) {
            return new TVShow[size];
        }
    };
    private int id;
    private String name;
    private String original_name;
    private String overview;
    private String first_air_date;
    private String poster_path;
    private String backdrop_path;
    private double popularity;
    private double vote_average;
    private int vote_count;
    private String original_language;
    private String[] origin_country;
    private int[] genre_ids;

    protected TVShow(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.original_name = in.readString();
        this.overview = in.readString();
        this.first_air_date = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.popularity = in.readDouble();
        this.vote_average = in.readDouble();
        this.vote_count = in.readInt();
        this.original_language = in.readString();
        this.origin_country = in.createStringArray();
        this.genre_ids = in.createIntArray();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String[] getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(String[] origin_country) {
        this.origin_country = origin_country;
    }

    public int[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TVShow{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", original_name='").append(original_name).append('\'');
        sb.append(", overview='").append(overview).append('\'');
        sb.append(", first_air_date='").append(first_air_date).append('\'');
        sb.append(", poster_path='").append(poster_path).append('\'');
        sb.append(", backdrop_path='").append(backdrop_path).append('\'');
        sb.append(", popularity=").append(popularity);
        sb.append(", vote_average=").append(vote_average);
        sb.append(", vote_count=").append(vote_count);
        sb.append(", original_language='").append(original_language).append('\'');
        sb.append(", origin_country=").append(Arrays.toString(origin_country));
        sb.append(", genre_ids=").append(Arrays.toString(genre_ids));
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.original_name);
        dest.writeString(this.overview);
        dest.writeString(this.first_air_date);
        dest.writeString(this.poster_path);
        dest.writeString(this.backdrop_path);
        dest.writeDouble(this.popularity);
        dest.writeDouble(this.vote_average);
        dest.writeInt(this.vote_count);
        dest.writeString(this.original_language);
        dest.writeStringArray(this.origin_country);
        dest.writeIntArray(this.genre_ids);
    }
}
