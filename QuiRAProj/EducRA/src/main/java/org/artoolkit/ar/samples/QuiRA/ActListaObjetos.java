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

public class ActListaObjetos extends AppCompatActivity implements RecyclerViewOnClickListener,Serializable {
    private RecyclerView lstDados;
    private FloatingActionButton fab;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private ConstraintLayout layoutContentMain;
    private ObjetoRepositorio objetoRepositorio;
    private ObjetoAdapter objetoAdapter;

    public int codDisciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lista_objetos);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão de voltar

        lstDados = (RecyclerView) findViewById(R.id.lstDados);
        layoutContentMain = (ConstraintLayout) findViewById(R.id.layoutContentListaObjetos);
        lstDados.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lstDados.setLayoutManager(linearLayoutManager);

        //Conexão com o banco de dados
        criarConexão();
        objetoRepositorio = new ObjetoRepositorio(conexao);

        //pega o codigo da disciplina selecionada na tela anteior, valor default -1
        codDisciplina = (getIntent().getIntExtra("CODIGO", -1));

        //Busca os Objetos cadastrados
        List<Objeto> dados = objetoRepositorio.buscarTodos(codDisciplina);

        //Colocar dados no formato par ser mostrado no layout
        objetoAdapter = new ObjetoAdapter(this, dados, this, this);

        //Inserir lista de Objetos no layout
        lstDados.setAdapter(objetoAdapter);



    }

    //Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_lista_objetos,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Itens do menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add_lista:
                Intent it = new Intent(ActListaObjetos.this,ActCadObjeto.class);
                //passa o codigo da disciplina para a tela de cadastro de objetos
                it.putExtra("CODIGO", codDisciplina);
                startActivity(it);
                finish();
                break;
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

    //Click em item da lista (Editar Objeto)
    @Override
    public void onClickListener(Object object) {
        Objeto objeto = (Objeto) object;
        Intent i = new Intent(ActListaObjetos.this,ActEdtObjeto.class);
        i.putExtra("CODIGO", codDisciplina);
        i.putExtra("objetoEnviado", objeto); //Envia objeto para a Activity de Edição -> ActEdtObjeto
        startActivity(i);
        finish();

    }

    @Override
    public void onClickImageButtonEdit(Object object) {

    }

    //Click em ícone da Lixeira (Excluir Objeto)
    @Override
    public void onClickImageButtonDelete(Object object) {
        final Objeto objeto = (Objeto) object;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_aviso);
        //Ação para click em SIM
        builder.setMessage("Deseja excluir este objeto?").setPositiveButton("Sim", new DialogInterface.OnClickListener(){
            public void onClick (DialogInterface dialog, int id){
                //Exclui objeto do Banco de Dados
                objetoRepositorio.excluir(objeto);
                //Exibe mensagemde confirmação da exclusão
                Toast.makeText(getApplication(), "Objeto excluído com sucesso!", Toast.LENGTH_SHORT).show();
                //Retorna para Activity ActListaObjetos
                Intent i = new Intent(getApplicationContext(),ActListaObjetos.class);
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
}
