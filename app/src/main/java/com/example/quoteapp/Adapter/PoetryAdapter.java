package com.example.quoteapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quoteapp.Api.ApiClint;
import com.example.quoteapp.Api.ApiInterface;
import com.example.quoteapp.Models.PoetryModel;
import com.example.quoteapp.R;
import com.example.quoteapp.Response.DeleteResponse;
import com.example.quoteapp.UpdateActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PoetryAdapter extends RecyclerView.Adapter<PoetryAdapter.ViewHolder> {
    Context context;
    List<PoetryModel>poetryModels;

    ApiInterface apiInterface;

    public PoetryAdapter(Context context, List<PoetryModel> poetryModels) {
        this.context = context;
        this.poetryModels = poetryModels;

        Retrofit retrofit= ApiClint.getclient();
        apiInterface=retrofit.create(ApiInterface.class);

    }

    @NonNull
    @Override
    public PoetryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.poetry_list_recycle,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoetryAdapter.ViewHolder holder, int position) {

        holder.poetName.setText(poetryModels.get(position).getPoet_name());
        holder.poetry.setText(poetryModels.get(position).getPoetry_data());
        holder.date_time.setText(poetryModels.get(position).getDate_time());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle("Are you want to delete ?");
                builder.setIcon(R.drawable.baseline_delete_24);
                builder.setMessage("Are you sure.");

                builder.setCancelable(false);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletepoetry(poetryModels.get(position).getId()+"",position);
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
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, poetryModels.get(position).getId()+"", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(context, UpdateActivity.class);
                intent.putExtra("p_id",poetryModels.get(position).getId());
                intent.putExtra("p_data",poetryModels.get(position).getPoetry_data());
                intent.putExtra("p_name",poetryModels.get(position).getPoet_name());
                context.startActivity(intent);

                ((Activity)v.getContext()).finish();


            }
        });

    }

    @Override
    public int getItemCount() {
        return poetryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView poetName,poetry,date_time;
        AppCompatButton update,delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            poetName=itemView.findViewById(R.id.poet_name);
            poetry=itemView.findViewById(R.id.poetry_data);

            date_time=itemView.findViewById(R.id.datetime);

            update=itemView.findViewById(R.id.btnupdate);

            delete=itemView.findViewById(R.id.btndelete);

        }
    }
    private void deletepoetry(String id,int position)
    {
        apiInterface.deletepoetry(id).enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
            try
            {
             if (response!=null)
             {
                 poetryModels.remove(position);
                 notifyDataSetChanged();
                 Toast.makeText(context, "Quot deleted Successfully.", Toast.LENGTH_SHORT).show();
             }
            }
            catch (Exception e)
            {
                Toast.makeText(context, "Somethig went wrong .", Toast.LENGTH_SHORT).show();

            }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
