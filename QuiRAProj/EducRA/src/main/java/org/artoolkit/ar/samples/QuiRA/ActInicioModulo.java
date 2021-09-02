package org.artoolkit.ar.samples.QuiRA;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.artoolkit.ar.samples.QuiRA.ar.ARObjetoActivity;
import org.artoolkit.ar.samples.QuiRA.dominio.entidades.Disciplina;

public class ActInicioModulo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variaveis que irao receber os dados da disciplina da tela anterior
    int codDisciplina;
    String nomeDisciplina;
    String imgDisciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //definição do layout
        setContentView(R.layout.activity_modulo_disciplina_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ImageView iv = (ImageView) findViewById(R.id.imageView4);
        TextView tv = (TextView) findViewById(R.id.textView4);

        //pega os dados da tela anterior e seta as informações na tela
        iv.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra("IMAGEM"))); //pega o caminho da imagem da tela anterior e seta como a imageview da tela atual
        tv.setText(getIntent().getStringExtra("NOME"));

        //pega o codigo da disciplina selecionada na tela anteior, valor default -1
        codDisciplina = (getIntent().getIntExtra("CODIGO", -1));

        //System.out.println("CODIGO DA DISCIPLINA PASSADO = "+codDisciplina);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //ação do botão de câmera
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActInicioModulo.this,ARObjetoActivity.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(this, ActInicio.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        finish();
        return;
       /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        */
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.action_gerenciar) {
            Intent it = new Intent(ActInicioModulo.this,ActListaObjetos.class);
            it.putExtra("CODIGO", codDisciplina);
            startActivity(it);
            finish();
        } else if (id == R.id.action_sobre) {
            Intent it = new Intent(ActInicioModulo.this,ActSobre.class);
            startActivity(it);
            finish();
        } else if (id == R.id.action_manual) {//Abre navegador com o link do manual
            String endereco = "https://drive.google.com/drive/folders/" +
                    "19ZnWziSguht1jyk5V9YzB_iSiYPuO1oD?usp=sharing";
            Uri uri = Uri.parse(endereco);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
