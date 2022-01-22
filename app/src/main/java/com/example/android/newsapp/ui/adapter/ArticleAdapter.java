package com.example.android.newsapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.newsapp.R;
import com.example.android.newsapp.entities.Article;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DBM on 07/01/2018.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> mArticlesList;

    public ArticleAdapter(Context context, List<Article> list) {
        this.mArticlesList = list;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {

        Article currentArticle = mArticlesList.get(holder.getAbsoluteAdapterPosition());

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
            if(!thumbnailUrl.equals(holder.itemView.getContext().getString(R.string.no_image_available_thumbnail_field))) {
                Glide.with(holder.itemView.getContext())
                        .load(thumbnailUrl)
                        .apply(new RequestOptions().placeholder(R.drawable.placeholder_img))
                        .into(holder.thumbImageView);
            } else{
                holder.thumbImageView.setImageResource(R.drawable.no_image_available);
            }
        } else {
            holder.thumbImageView.setImageResource(R.drawable.no_image_available);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get article URL
                Article article = (Article) mArticlesList.get(position);
                String url = article.getWebUrl();
                Uri webPage = Uri.parse(url);

                //Show article in browser
                Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                if (intent.resolveActivity(holder.itemView.getContext().getPackageManager()) != null) {
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
    }

    public void setData(List<Article> newlist){
        if(newlist != null){
            this.mArticlesList = newlist;
        } else {
            this.mArticlesList.clear();
        }
    }

    @Override
    public int getItemCount() {
        return mArticlesList.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

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

        private ArticleViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
