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

public class UpdateActivity extends AppCompatActivity {


    EditText new_poem,new_poet;
    AppCompatButton update_poem;

    Toolbar toolbar;

    ApiInterface apiInterface;
    int poetryId;
    String poetry_data;
    String poet_name;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        new_poem=findViewById(R.id.new_poetry);
        new_poet=findViewById(R.id.new_poet);
        update_poem=findViewById(R.id.update_poem);

        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Updating");
        progressDialog.setMessage("Poem is updating..");


        poetryId=getIntent().getIntExtra("p_id",0);
        poetry_data=getIntent().getStringExtra("p_data");
        poet_name=getIntent().getStringExtra("p_name");

        new_poem.setText(poetry_data);
        new_poet.setText(poet_name);

        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Retrofit retrofit= ApiClint.getclient();

        apiInterface=retrofit.create(ApiInterface.class);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        update_poem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String poem=new_poem.getText().toString();
                String poet=new_poet.getText().toString();

                if (poem.equals(""))
                {
                    new_poem.setError("Field is empty");
                }
                else
                {
                    if (poet.equals(""))
                    {
                        new_poet.setError("Field is empty");
                    }
                    else
                    {
                        apiInterface.updatepoetry(poem,poetryId+"").enqueue(new Callback<AddPoetryResponse>() {
                            @Override
                            public void onResponse(Call<AddPoetryResponse> call, Response<AddPoetryResponse> response) {

                                if (response!=null)
                                {
                                    if (response.body().getStatus().equals("1"))
                                    {


                                        Toast.makeText(UpdateActivity.this, "Poem Updated", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        Intent intent=new Intent(UpdateActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(UpdateActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(UpdateActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<AddPoetryResponse> call, Throwable t) {
                                Toast.makeText(UpdateActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }


            }
        });





    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(UpdateActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}