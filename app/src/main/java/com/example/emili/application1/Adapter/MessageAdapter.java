package com.example.emili.application1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emili.application1.Donnee.Message;
import com.example.emili.application1.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by emili on 24/07/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    int taille;
    LayoutInflater inflater;
    List<Message> messageList;

    public MessageAdapter(Context context, List<Message> messageList){

        inflater = LayoutInflater.from(context);
        this.context = context;
        this.messageList = messageList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.message, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder holderl = (MyHolder) holder;
        Message current = messageList.get(position);
        holderl.message.setText(current.getText());
        holderl.nom.setText(current.getNom());

    }

    @Override
    public int getItemCount() {
        taille = messageList.size();
        return taille;
    }

    public void clear() {

        messageList.clear();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView message;
        TextView nom;

        public MyHolder(View itemView) {
            super(itemView);

            message = (TextView) itemView.findViewById(R.id.message_text);
            nom = (TextView) itemView.findViewById(R.id.auteur);
        }
    }


    public void ajouterTous(List<Message> messageList){

        this.messageList = messageList;
    }
}
