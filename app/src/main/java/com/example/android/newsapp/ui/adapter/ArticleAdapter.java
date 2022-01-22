package com.example.android.newsapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.newsapp.R;
import com.example.android.newsapp.entities.Article;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DBM on 07/01/2018.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    static class ViewHolder {
        @BindView(R.id.title_article)
        TextView titleTextView;

        @BindView(R.id.section_name_article)
        TextView sectionNameTextView;

        @BindView(R.id.author_article)
        TextView authorTextView;

        @BindView(R.id.date_published_article)
        TextView releaseDateTextView;

        @BindView(R.id.thumb_image_view)
        ImageView thumbImageView;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    //Constructor of ArticleAdapter
    public ArticleAdapter(Context context, ArrayList<Article> list){
        super(context,0,list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get Article object at respective position
        Article currentArticle = getItem(position);

        ViewHolder holder;

        //Create a new View in case no Views were created before
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        //Get data from the Article object
        String author = currentArticle.getAuthor();
        String sectionName = currentArticle.getSectionName();
        String title = currentArticle.getTitle();
        String releaseDate = currentArticle.getReleaseDate();
        String thumbnailUrl = currentArticle.getThumbnailUrl();

        //Set title
        if(title != null) {
            holder.titleTextView.setText(title);
        } else{
            holder.titleTextView.setText(R.string.unknown_title_message);
        }


        //Set section name
        if(sectionName != null) {
            holder.sectionNameTextView.setText(sectionName + " ");
        } else{
            holder.sectionNameTextView.setText(R.string.unknown_section_text);
        }

        //Set author
        if(author != null) {
            if (!author.isEmpty()) {
                holder.authorTextView.setText(String.format("By %s", author));
            } else {
                holder.authorTextView.setText(R.string.by_unknown_author_text);
            }
        } else{
            holder.authorTextView.setText(R.string.by_unknown_author_text);
        }

        //Set published date
        if(releaseDate != null) {
            holder.releaseDateTextView.setText(releaseDate + " ");
        } else {
            holder.releaseDateTextView.setText(R.string.unknown_release_date_text);
        }

        //Set thumbnail URL
        if(thumbnailUrl != null && !thumbnailUrl.isEmpty()){
            if(!thumbnailUrl.equals(getContext().getString(R.string.no_image_available_thumbnail_field))) {
                Glide.with(getContext())
                        .load(thumbnailUrl)
                        .apply(new RequestOptions().placeholder(R.drawable.placeholder_img))
                        .into(holder.thumbImageView);
            } else{
                holder.thumbImageView.setImageResource(R.drawable.no_image_available);
            }
        } else {
            holder.thumbImageView.setImageResource(R.drawable.no_image_available);
        }

        return convertView;
    }
}
