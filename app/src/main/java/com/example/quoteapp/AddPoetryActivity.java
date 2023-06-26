package com.example.quoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quoteapp.Api.ApiClint;
import com.example.quoteapp.Api.ApiInterface;
import com.example.quoteapp.Response.AddPoetryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddPoetryActivity extends AppCompatActivity {

    Toolbar toolbar;
    ApiInterface apiInterface;
    EditText poetry,poem_name;

    AppCompatButton addPoetry;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poetry);

        poetry=findViewById(R.id.poetry);
        poem_name=findViewById(R.id.poet);

        addPoetry=findViewById(R.id.add_poem);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog=new ProgressDialog(this);

        progressDialog.setTitle("Adding..");
        progressDialog.setMessage("Adding Poem..");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddPoetryActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        Retrofit retrofit= ApiClint.getclient();

        apiInterface=retrofit.create(ApiInterface.class);


        addPoetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String poem=poetry.getText().toString();
                String poet_name=poem_name.getText().toString();

                if (poem.equals(""))
                {
                    poetry.setError("Field is empty");
                }
                else
                {

                    if (poet_name.equals(""))
                    {
                        poem_name.setError("Field is empty");
                    }
                    else
                    {
                        apiInterface.addpoetry(poem,poet_name).enqueue(new Callback<AddPoetryResponse>() {
                            @Override
                            public void onResponse(Call<AddPoetryResponse> call, Response<AddPoetryResponse> response) {

                                if (response!=null)
                                {
                                    if (response.body().getStatus().equals("1"))
                                    {
                                        Toast.makeText(AddPoetryActivity.this, "Poem Added Successfully.", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                       Intent intent=new Intent(AddPoetryActivity.this,MainActivity.class);
                                       startActivity(intent);
                                       finish();
                                    }
                                }
                                else {
                                    Toast.makeText(AddPoetryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<AddPoetryResponse> call, Throwable t) {
                                Toast.makeText(AddPoetryActivity.this, "something went wrong !", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }


                }


            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AddPoetryActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}