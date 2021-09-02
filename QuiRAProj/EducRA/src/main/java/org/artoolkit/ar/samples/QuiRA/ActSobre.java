package org.artoolkit.ar.samples.QuiRA;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.artoolkit.ar.samples.QuiRA.database.DadosOpenHelper;
import org.artoolkit.ar.samples.QuiRA.dominio.entidades.Objeto;
import org.artoolkit.ar.samples.QuiRA.dominio.repositorio.ObjetoRepositorio;
import org.artoolkit.ar.samples.QuiRA.interfaces.RecyclerViewOnClickListener;

import java.io.Serializable;
import java.util.List;

/**
 * Created by letic on 31/12/2017.
 */

public class ActSobre extends AppCompatActivity {
    private ConstraintLayout layoutContentMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sobre);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão de voltar

        layoutContentMain = (ConstraintLayout) findViewById(R.id.layoutContentListaObjetos);
    }

    //Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_sobre,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Itens do menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, ActInicio.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){ //Botão BACK padrão do android
        startActivity(new Intent(this, ActInicio.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        finish(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

}
