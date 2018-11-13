package com.example.sridharjajoo.newsapp.core.Headline;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sridharjajoo.newsapp.R;
import com.example.sridharjajoo.newsapp.Utils.Utils;
import com.example.sridharjajoo.newsapp.data.AppDatabase;
import com.example.sridharjajoo.newsapp.data.Headline.Articles;
import com.example.sridharjajoo.newsapp.data.Headline.HeadlineService;
import com.example.sridharjajoo.newsapp.di.Injectable;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.List;
import java.util.Objects;
import java.util.PrimitiveIterator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeadlineFragment extends Fragment implements Injectable {

    @Inject
    HeadlineService headlineService;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.recycler_headline)
    RecyclerView recyclerHeadline;

    @BindView(R.id.custom_search)
    SearchView searchView;

    private List<Articles> articlesList;
    private HeadlineAdapter headlineAdapter;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_headline, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onStart() {
        super.onStart();
        final AppDatabase db = AppDatabase.getAppDatabase(getActivity());
        if (db.newsDao().newsArticles().isEmpty()) {
            headlineService.getHeadline("in")
                    .doOnSubscribe(disposable -> progressBar.setVisibility(View.VISIBLE))
                    .doFinally(() -> progressBar.setVisibility(View.GONE))
                    .subscribe(status -> {
                        this.articlesList = status.articles;
                        addArticlesToDb(articlesList);
                        setRecyclerView(articlesList);
                        Utils.hideKeyboard(view);
                    });
        } else {
            List<Articles> articles = db.newsDao().newsArticles();
            setRecyclerView(articles);
            Utils.hideKeyboard(view);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void addArticlesToDb(List<Articles> articles) {
        final AppDatabase db = AppDatabase.getAppDatabase(getActivity());
        for (int i = 0 ;i < articles.size(); i++) {
            Articles article = articles.get(i);
            db.newsDao().insertAt(article);
            Log.i("HeadlineFragment.class", "addArticlesToDb: " + db.newsDao().newsArticles().get(i) + "\n");
        }
    }

    private void setRecyclerView(List<Articles> articlesList) {
        recyclerHeadline.setLayoutManager(new LinearLayoutManager(getContext()));
        headlineAdapter = new HeadlineAdapter(articlesList, getActivity());
        recyclerHeadline.setAdapter(headlineAdapter);
        loadArticles(articlesList);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.divider)));
        recyclerHeadline.addItemDecoration(itemDecorator);
    }

    private void loadArticles(List<Articles> articlesList) {
        headlineAdapter.setArticlesList(articlesList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}