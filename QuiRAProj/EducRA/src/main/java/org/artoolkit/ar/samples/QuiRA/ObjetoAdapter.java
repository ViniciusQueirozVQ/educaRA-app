package org.artoolkit.ar.samples.QuiRA;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import org.artoolkit.ar.samples.QuiRA.dominio.entidades.Objeto;
import org.artoolkit.ar.samples.QuiRA.interfaces.RecyclerViewOnClickListener;

import java.io.File;
import java.util.List;

/**
 * Created by letic on 31/12/2017.
 */

public class ObjetoAdapter extends RecyclerView.Adapter<ObjetoAdapter.ViewHolderObjeto> {
    private List<Objeto> dados;
    private RecyclerViewOnClickListener clickItem;
    private RecyclerViewOnClickListener clickDelete;
    Context ctx;

    public ObjetoAdapter(Context ctx, List<Objeto> dados, RecyclerViewOnClickListener clickItem, RecyclerViewOnClickListener clickDelete){
        this.dados = dados;
        this.clickItem = clickItem;
        this.clickDelete = clickDelete;
        this.ctx = ctx;
    }

    @Override
    public ObjetoAdapter.ViewHolderObjeto onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext()); //inflar o layout
        View view  = layoutInflater.inflate(R.layout.linha_objeto, parent, false);
        ViewHolderObjeto holderObjeto = new ViewHolderObjeto(view);
        return holderObjeto;
    }

    @Override
    public void onBindViewHolder(ObjetoAdapter.ViewHolderObjeto holder, int position) {//position=posição da linha
        if((dados != null) && (dados.size() > 0)){
            Objeto objeto = dados.get(position);
            holder.txtNome.setText(objeto.getNome());
            String imagem = objeto.getImagem();
            File imgFile = new  File(imagem);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imgVObjeto.setImageBitmap(myBitmap);
            }
         }
    }

    @Override
    public int getItemCount() {

        return dados.size();
    }

    //Representa um item da lista
    public class ViewHolderObjeto extends RecyclerView.ViewHolder{
        public ImageView imgVObjeto;
        public TextView txtNome;
        public ImageButton imgBtnDelete;

        public ViewHolderObjeto(View itemView){
            super(itemView);
            imgVObjeto = (ImageView) itemView.findViewById(R.id.imgVObjeto);
            txtNome = (TextView) itemView.findViewById(R.id.txtNome);
            imgBtnDelete = (ImageButton) itemView.findViewById(R.id.imgBtnDelete);
            //Click em Item da lista
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickItem.onClickListener(dados.get(getLayoutPosition()));
                }
            });
            //Click em Lixeira do Item da lista
            imgBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDelete.onClickImageButtonDelete(dados.get(getLayoutPosition()));
                }
            });
        }
    }
}
