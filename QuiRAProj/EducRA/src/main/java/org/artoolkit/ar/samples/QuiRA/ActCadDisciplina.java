package org.artoolkit.ar.samples.QuiRA;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.artoolkit.ar.samples.QuiRA.arquivo.FileUtils;
import org.artoolkit.ar.samples.QuiRA.database.DadosOpenHelper;
import org.artoolkit.ar.samples.QuiRA.dominio.entidades.Disciplina;
import org.artoolkit.ar.samples.QuiRA.dominio.entidades.Objeto;
import org.artoolkit.ar.samples.QuiRA.dominio.repositorio.DisciplinaRepositorio;
import org.artoolkit.ar.samples.QuiRA.dominio.repositorio.ObjetoRepositorio;

import java.io.Serializable;

/**
 * Created by filip on 16/07/2018.
 */

public class ActCadDisciplina extends AppCompatActivity implements Serializable {

    private EditText editNome;
    private EditText editImagem;
    private ImageButton botaoImagem;
    //variavel para layout
    private ConstraintLayout layoutContentActCadDisciplina;
    //Variáveis para conexão com o banco de dados
    private DadosOpenHelper dadosOpenHelper;
    private SQLiteDatabase conexao;
    //Variável para tratar objeto (inserção ou alteração)
    private DisciplinaRepositorio disciplinaRepositorio;
    private Disciplina disciplina;

    private static final int REQUEST_CODE_IMAGEM = 6386; // onActivityResult request
    private static final String TAG = "ActCadDisciplina";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //criando a xml "act_cad_disciplina" para chamar aqui
        setContentView(R.layout.act_cad_disciplina);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão de volta



        //Campos da tela
        editNome = (EditText) findViewById(R.id.editDisciplina);
        editImagem = (EditText) findViewById(R.id.editImg);
        editImagem = (EditText) findViewById(R.id.editImg);
        botaoImagem = (ImageButton) findViewById(R.id.imgButton);

        layoutContentActCadDisciplina = (ConstraintLayout) findViewById(R.id.layoutContentActCadDisciplina);

        criarConexao(); //conexão com o banco de dados


        botaoImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent target = FileUtils.createGetContentIntent();
                // Create the chooser Intent
                Intent intent = Intent.createChooser(target, getString(R.string.chooser_title));

                startActivityForResult(intent, REQUEST_CODE_IMAGEM);//chama classe para tratar resultado correspondente ao campo Imagem
            }
        });


    }

    //cria a conexão com o banco de dados
    private void criarConexao() {
        try {
            dadosOpenHelper = new DadosOpenHelper(this); //this é a própria activity
            conexao = dadosOpenHelper.getWritableDatabase();
            disciplinaRepositorio = new DisciplinaRepositorio(conexao);
        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_cad_objeto, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Item do menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //Click no ícone para salvar
            case R.id.action_salvar:
                //Chama o método confirmar
                confirmar();
                //Toast.makeText(this,"Botão OK selecionado!", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, ActInicio.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        startActivity(new Intent(this, ActInicio.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        //finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        finish();
        return;
    }

    //Método chamado ao clicar no ícone para incluir/salvar Disciplina
    private void confirmar() {
        disciplina = new Disciplina();
        //Método validaCampos() inicializa Disciplina com os valores dos campos
        if (validaCampos() == false) {
            try {
                    disciplinaRepositorio.inserir(disciplina);
                    Toast.makeText(getApplication(), "Disciplina cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ActCadDisciplina.this,ActInicio.class);
                    startActivity(i);
                    finish();
               // }
            } catch (SQLException ex) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle(R.string.title_erro);
                dlg.setMessage(R.string.message_erro_salvar);
                dlg.setNeutralButton(R.string.action_ok, null);
                dlg.show();
            }
        }
    }

    //Validação dos campos preenchidos
    private boolean validaCampos() {
        boolean res = false;

        //Recebe os dados inseridos nos campos
        String nome = editNome.getText().toString();
        String imagem = editImagem.getText().toString();

        //Instancia o Objeto com os valores dos campos
        disciplina.setNome(nome);
        disciplina.setImagem(imagem);

        //Verifica se há algum campo vazio
        if (res = isCampoVazio(nome)) {
            editNome.requestFocus();
        } else {
            if (res = isCampoVazio(imagem)) {
                editImagem.requestFocus();
            }
        }


        //Exibe mensagem para campo vazio ou inválido
        //Se existem campos vazios / res = true
        if (res) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_aviso);
            dlg.setMessage(R.string.message_campos_invalidos_brancos);
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
        //Se não existem campos vazios / res = false
        //Verifica se as extensões estão corretas
        // Se as extensões não estão corretas entra no if e exibe mensagem orientando correção
        else if (!res){
            //Coleta as extensões dos arquivos
            String extImg = imagem.substring(imagem.lastIndexOf("."));
            System.out.println("EXTENSAO: "+extImg);

            if (!isImg(extImg)) {
                dialogExtensao("Insira um arquivo de imagem no campo Imagem");
                res = true;
            }
        }

        return res; //retorna false se todos os campos estiverem validados
    }

    //Verifica se o conteúdo do campo é vazio
    private boolean isCampoVazio(String valor) {
        //true para vazio
        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }

    //Verifica se a extensão do arquivo é .jop ou .png
    private boolean isImg(String valor) {
        boolean resultado = false;
        if((valor.equals(".jpg")) || (valor.equals(".png")) || (valor.equals(".JPG")) || (valor.equals(".PNG"))){
            resultado = true;
        }
        return resultado; //true para extensão ok
    }

    //Exibe mensagem para correção do arquivo
    private void dialogExtensao (String mensagem){
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle(R.string.title_aviso);
        dlg.setMessage(mensagem);
        dlg.setNeutralButton(R.string.action_ok, null);
        dlg.show();
    }

    //Método que trata o resultado (arquivo selecionado)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_IMAGEM:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            final String path = FileUtils.getPath(this, uri);
                            //Inserindo o caminho selecionado no campo Imagem
                            editImagem.setText(path);
                            Toast.makeText(ActCadDisciplina.this,
                                    "Arquivo selecionado: " + path, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("FileSelectorTestAct", "File select error", e);
                        }
                    }
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
