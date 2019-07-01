package com.example.hometask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.sip.SipAudioCall;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private String[] arraySurname;
    private String[] arrayName;
    private String[] arrayPatronymic;
    private String[] arrayPosition;
    private String[] arrayDate;
    private int[] arrayId;
    private Listener listener;

    interface Listener{
        void onClick(int i);
    }

    public Adapter(String[] arraySurname, String[] arrayName, String[] arrayPatronymic, String[] arrayPosition, String[] arrayDate, int[] arrayId) {
        this.arraySurname = arraySurname;
        this.arrayName = arrayName;
        this.arrayPatronymic = arrayPatronymic;
        this.arrayPosition = arrayPosition;
        this.arrayDate = arrayDate;
        this.arrayId = arrayId;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_client, null);
        return new  ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final CardView cardView = viewHolder.cardView;
        ImageView image = (ImageView) cardView.findViewById(R.id.image);
        image.setImageBitmap(Image("/data/data/com.example.hometask/app_imageDir", String.valueOf(arrayId[i])));
        image.setContentDescription(arrayName[i]);
        TextView fio = (TextView) cardView.findViewById(R.id.fio);
        fio.setText(arraySurname[i] + " " + arrayName[i] + " " + arrayPatronymic[i]);
        final TextView position = (TextView) cardView.findViewById(R.id.position);
        position.setText(arrayPosition[i]);
        TextView date = (TextView) cardView.findViewById(R.id.date);
        date.setText(arrayDate[i]);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onClick(arrayId[i]);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayName.length;
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }

    private Bitmap Image(String directory, String name){
        File file = new File(directory, name + ".png");
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        return bitmap;
    }
}
