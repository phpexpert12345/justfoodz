package com.justfoodz.activities;

import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;

import java.util.ArrayList;

public class Referedfrnlist extends AppCompatActivity {

    RecyclerView rcfrndlist;
    ImageView iv_back;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referedfrnlist);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        rcfrndlist = (RecyclerView) findViewById(R.id.rcfrndlist);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FrndList frndList = new FrndList(this,ReferEarnActivity.aa_user_name,ReferEarnActivity.aa_user_phone,ReferEarnActivity.aa_user_email);
        linearLayoutManager = new LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false);
        rcfrndlist.setLayoutManager(linearLayoutManager);
        rcfrndlist.setAdapter(frndList);
    }

    public class FrndList extends RecyclerView.Adapter<FrndList.ViewHolder>
    {
        Context context;
        ArrayList<String> name,number,email;

        protected FrndList(Context c,ArrayList<String> na,ArrayList<String> n,ArrayList<String> e)
        {
            this.context = c;
            this.name = na;
            this.number = n;
            this.email = e;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.frndlistdesign, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.txtname.setText(""+name.get(position));
            holder.txtnumber.setText(""+number.get(position));
            holder.txtemail.setText(""+email.get(position));
        }

        @Override
        public int getItemCount() {
            return name.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtname,txtnumber,txtemail;
            public ViewHolder(View itemView) {
                super(itemView);
                txtname = (TextView) itemView.findViewById(R.id.txtname);
                txtnumber = (TextView) itemView.findViewById(R.id.txtnumber);
                txtemail = (TextView) itemView.findViewById(R.id.txtemail);
            }
        }
    }
}
