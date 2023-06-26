package com.example.quoteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.quoteapp.Adapter.PoetryAdapter;
import com.example.quoteapp.Api.ApiClint;
import com.example.quoteapp.Api.ApiInterface;
import com.example.quoteapp.Models.PoetryModel;
import com.example.quoteapp.Response.GetPoetryResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PoetryAdapter poetryAdapter;


    ApiInterface apiInterface;

    Toolbar toolbar;

    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        initialize();
        setSupportActionBar(toolbar);
        getdata();
    }

    private void initialize()
    {
     toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.poetry_recycle);
        toolbar=findViewById(R.id.toolbar);

        Retrofit retrofit= ApiClint.getclient();

        apiInterface=retrofit.create(ApiInterface.class);



    }

    private void setadapter(List<PoetryModel>poetryModels)
    {
        poetryAdapter=new PoetryAdapter(MainActivity.this,poetryModels);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(poetryAdapter);
    }

    public void getdata()
    {
        apiInterface.getpoetry().enqueue(new Callback<GetPoetryResponse>() {
            @Override
            public void onResponse(Call<GetPoetryResponse> call, Response<GetPoetryResponse> response) {

            if (response!=null)
            {

                if (response.body().getStatus().equals("1"))
                {
                    setadapter(response.body().getData());
                    progressDialog.dismiss();

                }
                else
                {
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

                 else
                 {
                     Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                 }
             }



            @Override
            public void onFailure(Call<GetPoetryResponse> call, Throwable t) {
                Log.d("error",t.getMessage().toString());
                Toast.makeText(MainActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.add_poetry)
        {
            Intent intent=new Intent(MainActivity.this,AddPoetryActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();

    }
}