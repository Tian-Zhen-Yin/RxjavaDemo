package com.example.administrator.cheesefinder;

import android.app.AppComponentFactory;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BaseSearchActivity extends AppCompatActivity {

    protected CheeseSearchEngine mCheeseSearchEngine;
    //EditText用来输入字符
    protected EditText mQueryEdiText;
    protected Button mSearchButton;
    private CheeseAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheeses);

        RecyclerView list=(RecyclerView)findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(mAdapter=new CheeseAdapter());

        mQueryEdiText=(EditText)findViewById(R.id.query_edit_text);
        mSearchButton=(Button)findViewById(R.id.search_button);
        mProgressBar=(ProgressBar) findViewById(R.id.progress_bar);

        List<String> cheeses=Arrays.asList(getResources().getStringArray(R.array.cheeses));
        mCheeseSearchEngine= new CheeseSearchEngine(cheeses);

    }
    protected void showProgressBar()
    {
        mProgressBar.setVisibility(View.VISIBLE);
    }
    protected void hideProgressBar()
    {
        mProgressBar.setVisibility(View.GONE);
    }
    protected void showResult(List<String> result)
    {
        if(result.isEmpty())
        {
          Toast.makeText(this, R.string.nothing_found, Toast.LENGTH_SHORT).show();
          mAdapter.setCheeses(Collections.<String>emptyList());
        }else
        {
            mAdapter.setCheeses(result);
        }
    }
}
