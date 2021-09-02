package org.artoolkit.ar.samples.QuiRA;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import org.artoolkit.ar.samples.QuiRA.dominio.entidades.Disciplina;
import org.artoolkit.ar.samples.QuiRA.interfaces.RecyclerViewOnClickListener;

import java.io.File;
import java.util.List;

/**
 * Created by filip on 02/08/2018.
 */

public class DisciplinaAdapter extends RecyclerView.Adapter<DisciplinaAdapter.ViewHolderObjeto> {
    private List<Disciplina> dados;
    private RecyclerViewOnClickListener clickItem;
    private RecyclerViewOnClickListener clickDelete;
    private RecyclerViewOnClickListener clickEdit;
    Context ctx;

    public DisciplinaAdapter(Context ctx, List<Disciplina> dados, RecyclerViewOnClickListener clickItem, RecyclerViewOnClickListener clickDelete, RecyclerViewOnClickListener clickEdit){
        this.dados = dados;
        this.clickItem = clickItem;
        this.clickDelete = clickDelete;
        this.clickEdit = clickEdit;
        this.ctx = ctx;
    }

    @Override
    public DisciplinaAdapter.ViewHolderObjeto onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext()); //inflar o layout
        View view  = layoutInflater.inflate(R.layout.linha_disciplina, parent, false);
        ViewHolderObjeto holderObjeto = new ViewHolderObjeto(view);
        return holderObjeto;
    }

    @Override
    public void onBindViewHolder(DisciplinaAdapter.ViewHolderObjeto holder, int position) {//position=posição da linha
        if((dados != null) && (dados.size() > 0)){
            Disciplina disciplina = dados.get(position);
            holder.txtNome.setText(disciplina.getNome());
            String imagem = disciplina.getImagem();
            File imgFile = new  File(imagem);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imgVDisciplina.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    public int getItemCount() {

        return dados.size();
    }

    //Representa um item da lista
    public class ViewHolderObjeto extends RecyclerView.ViewHolder{
        public ImageView imgVDisciplina;
        public TextView txtNome;
        public ImageButton imgBtnDelete;
        public ImageButton imgBtnEdit;

        public ViewHolderObjeto(View itemView){
            super(itemView);
            imgVDisciplina = (ImageView) itemView.findViewById(R.id.imageView_imagem);
            txtNome = (TextView) itemView.findViewById(R.id.textView_nome);
            imgBtnDelete = (ImageButton) itemView.findViewById(R.id.botaoDelete);
            imgBtnEdit = (ImageButton) itemView.findViewById(R.id.botaoEdit);

            //Click em Item da lista
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickItem.onClickListener(dados.get(getLayoutPosition()));
                }
            });

            //Click no Edit do Item da lista
            imgBtnEdit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    clickEdit.onClickImageButtonEdit(dados.get(getLayoutPosition()));
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
