package org.artoolkit.ar.samples.QuiRA;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.artoolkit.ar.samples.QuiRA.database.DadosOpenHelper;
import org.artoolkit.ar.samples.QuiRA.dominio.entidades.Disciplina;
import org.artoolkit.ar.samples.QuiRA.dominio.repositorio.DisciplinaRepositorio;
import org.artoolkit.ar.samples.QuiRA.interfaces.RecyclerViewOnClickListener;

import java.io.Serializable;
import java.util.List;

public class ActInicio extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,RecyclerViewOnClickListener,Serializable {

    private RecyclerView listaDados;
    private FloatingActionButton fab;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private ConstraintLayout layoutContentMain;
    private DisciplinaRepositorio discRep;
    private DisciplinaAdapter discAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //definição do layout
        setContentView(R.layout.activity_inicio_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        listaDados = (RecyclerView) findViewById(R.id.listaDados);
        layoutContentMain = (ConstraintLayout) findViewById(R.id.layoutContentListaDisciplina);
        listaDados.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listaDados.setLayoutManager(linearLayoutManager);

        //Conexão com o banco de dados
        criarConexão();
        discRep = new DisciplinaRepositorio(conexao);

        //busca todas as disciplinas cadastradas no bd
        List<Disciplina> listaDisciplinas = discRep.buscarTodos();

        //coloca os dados no formato a ser mostrado no layout
        discAdapter = new DisciplinaAdapter(this, listaDisciplinas, this, this, this);

        //inserir lista de disciplinas no layout
        listaDados.setAdapter(discAdapter);

        //Conexão com o banco de dados
        criarConexão();

        //botao de cadastrar nova disciplina
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.createDisciplina);
        //ação do botão de adicionar nova disciplina
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActInicio.this,ActCadDisciplina.class);
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

    //Conexão com o banco de dados
    private void criarConexão(){
        try{
            dadosOpenHelper = new DadosOpenHelper(this); //this é a própria activity
            conexao = dadosOpenHelper.getWritableDatabase();
            //Snackbar.make(layoutContentMain, R.string.message_conexao_criada_com_sucesso, Snackbar.LENGTH_SHORT).setAction(R.string.action_ok,null).show();
        }
        catch(SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Click em item da lista
    @Override
    public void onClickListener(Object object) {
        Disciplina disciplina = (Disciplina) object;

        int codDisciplina = disciplina.getCodigo_disciplina();
        String nomeDisciplina = disciplina.getNome();
        String imgDisciplina = disciplina.getImagem();

        Intent i = new Intent(ActInicio.this,ActInicioModulo.class);
        //Envia os dados da disciplina selecionada para a Activity de Disciplina -> ActInicioModulo
        System.out.println("CODIGO DA DISCIPLINA PASSADO 1 = "+codDisciplina);

        i.putExtra("CODIGO", codDisciplina);
        i.putExtra("NOME", nomeDisciplina);
        i.putExtra("IMAGEM", imgDisciplina);
        startActivity(i);
        finish();

    }

    @Override
    public void onClickImageButtonEdit(Object object) {
        Disciplina d = (Disciplina) object;
        Intent i = new Intent(ActInicio.this, ActEdtDisciplina.class);
        i.putExtra("objetoEnviado", d); //Envia objeto para a Activity de Edição -> ActEdtObjeto
        startActivity(i);
        finish();
    }

    @Override
    public void onClickImageButtonDelete(Object object) {
        final Disciplina disciplina = (Disciplina) object;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_aviso);
        //Ação para click em SIM
        builder.setMessage("Deseja excluir este módulo? Todos os objetos relacionados serão destruídos").setPositiveButton("Sim", new DialogInterface.OnClickListener(){
            public void onClick (DialogInterface dialog, int id){
                //Exclui objeto do Banco de Dados
                discRep.excluir(disciplina);
                //Exibe mensagemde confirmação da exclusão
                Toast.makeText(getApplication(), "Módulo excluído com sucesso!", Toast.LENGTH_SHORT).show();
                //Retorna para Activity ActInicio
                Intent i = new Intent(getApplicationContext(),ActInicio.class);
                startActivity(i);
            }
            //Ação para click em NÃO
        }).setNegativeButton("Não",new DialogInterface.OnClickListener(){
            public void onClick (DialogInterface dialog, int id){
                //Retira Dialog da tela
            }
        });
        //Exibe diaolog
        builder.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        /*if (id == R.id.action_gerenciar) {
            Intent it = new Intent(ActInicio.this,ActListaObjetos.class);
            startActivity(it);
            finish();
        } else */ if (id == R.id.action_sobre) {
            Intent it = new Intent(ActInicio.this,ActSobre.class);
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
