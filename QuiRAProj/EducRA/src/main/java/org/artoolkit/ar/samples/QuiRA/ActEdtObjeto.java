package org.artoolkit.ar.samples.QuiRA;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.Toast;

import org.artoolkit.ar.samples.QuiRA.arquivo.FileUtils;
import org.artoolkit.ar.samples.QuiRA.database.DadosOpenHelper;
import org.artoolkit.ar.samples.QuiRA.dominio.entidades.Objeto;
import org.artoolkit.ar.samples.QuiRA.dominio.repositorio.ObjetoRepositorio;

import java.io.File;
import java.io.Serializable;

import static org.artoolkit.ar.samples.QuiRA.R.id.imgVMarcador;

//import org.artoolkit.ar.samples.QuiRA.arquivo.MainActivity;

/**
 * Created by letic on 17/12/2017.
 */

public class ActEdtObjeto extends AppCompatActivity implements Serializable{
    //Variáveis para tratarem os campos da tela
    private EditText edtNome;
    private EditText edtDescricao;
    private EditText edtPatt;
    private EditText edtObj;
    private EditText edtImagem;
    private ImageView imgVImagem;
    private ImageView imgVMarcador;
    private EditText edtMarcador;
    //Variáveis para tratarem os botões de pesquisa de arquivo
    private ImageButton btnPatt;
    private ImageButton btnObj;
    private ImageButton btnImagem;
    private ImageButton btnMarcador;
    //Variáveis para conexão com o banco de dados
    private DadosOpenHelper dadosOpenHelper;
    private SQLiteDatabase conexao;
    //Variável para layout
    private ConstraintLayout layoutContentActEdtObjeto;
    //Variável para tratar objeto (inserção ou alteração)
    private ObjetoRepositorio objetoRepositorio;
    private Objeto objeto;
    //Variável para tratar objeto (alteração)
    private Objeto altObjeto;
    private int idalt;
    private boolean alt = false;
    //Variáveis para tratar do acesso ao sistema de arquivos
    private static final int REQUEST_CODE_PATT = 6384; // onActivityResult request
    private static final int REQUEST_CODE_OBJ = 6385; // onActivityResult request
    private static final int REQUEST_CODE_IMAGEM = 6386; // onActivityResult request
    private static final int REQUEST_CODE_MARCADOR = 6387; // onActivityResult request
    private static final String TAG = "ActEdtObjeto";
    public int codDisciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edt_objeto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão de voltar

        //Caso em que objeto é recebido para alteração
        Intent i = getIntent();
        altObjeto = (Objeto) i.getSerializableExtra("objetoEnviado");

        //Tratamento de botões que carregam arquivos
        btnPatt = (ImageButton) findViewById(R.id.btnPatt);
        btnPatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the file chooser dialog
                showChooser(1); // 1 indica que botão de Arquivo .patt foi clicado
            }
        });
        btnObj = (ImageButton) findViewById(R.id.btnObj);
        btnObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the file chooser dialog
                showChooser(2); // 2 indica que botão de Arquivo .obj foi clicado
            }
        });
        btnImagem = (ImageButton) findViewById(R.id.btnImagem);
        btnImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the file chooser dialog
                showChooser(3); // 3 indica que botão de Imagem foi clicado
            }
        });
        btnMarcador = (ImageButton) findViewById(R.id.btnMarcador);
        btnMarcador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the file chooser dialog
                showChooser(4); // 4 indica que botão de Marcador foi clicado
            }
        });

        //Campos da tela
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtPatt = (EditText) findViewById(R.id.edtPatt);
        edtObj = (EditText) findViewById(R.id.edtObj);
        edtImagem = (EditText) findViewById(R.id.edtImagem);
        imgVMarcador = (ImageView) findViewById(R.id.imgVMarcador);
        imgVImagem = (ImageView) findViewById(R.id.imgVImagem);
        edtMarcador = (EditText) findViewById(R.id.edtMarcador);
        edtPatt.setEnabled(false);
        edtObj.setEnabled(false);
        edtImagem.setEnabled(false);
        edtMarcador.setEnabled(false);

        //recebe o codigo da disciplina da tela de lista de objetos
        codDisciplina = (getIntent().getIntExtra("CODIGO", -1));

        //Se foi recebido Objeto para edição, carregar os campos da tela com os dados do Objeto
        if(altObjeto != null){
            edtNome.setText(altObjeto.getNome());
            edtDescricao.setText(altObjeto.getDescricao());
            edtPatt.setText(altObjeto.getPatt());
            edtObj.setText(altObjeto.getObj());
            edtImagem.setText(altObjeto.getImagem());
            edtMarcador.setText(altObjeto.getMarcador());

            String imagem = edtImagem.getText().toString();
            System.out.println("IMAGEM: "+imagem);
            File imgFile2 = new  File(imagem);
            if(imgFile2.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());
                imgVImagem.setImageBitmap(myBitmap);
            }

            String marcador = edtMarcador.getText().toString();
            System.out.println("MARCADOR: "+marcador);
            File imgFile = new  File(marcador);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgVMarcador.setImageBitmap(myBitmap);
            }

            idalt = altObjeto.getCodigo();
            alt = true; //Indica que a operação é de alteração e não de inclusão
        }

        layoutContentActEdtObjeto = (ConstraintLayout) findViewById(R.id.layoutContentActEdtObjeto);

        criarConexao(); //conexão com o banco de dados

    }

    //Cria conexão como banco de dados
    private void criarConexao() {
        try {
            dadosOpenHelper = new DadosOpenHelper(this); //this é a própria activity
            conexao = dadosOpenHelper.getWritableDatabase();
            //Snackbar.make(layoutContentActCadObjeto, R.string.message_conexao_criada_com_sucesso, Snackbar.LENGTH_SHORT).setAction(R.string.action_ok,null).show();
            objetoRepositorio = new ObjetoRepositorio(conexao);
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
        inflater.inflate(R.menu.menu_act_edt_objeto, menu);
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
                //if (alt) {
                    //startActivity(new Intent(this, ActListaObjetos.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                    Intent i = new Intent(this, ActListaObjetos.class);
                    i.putExtra("CODIGO", codDisciplina);
                    System.out.println("teste de cod = "+codDisciplina);
                    startActivity(i); //O efeito ao ser pressionado do botão (no caso abre a activity)
                    finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                    break;
                /*} else{
                    startActivity(new Intent(this, ActInicio.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                    finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                    break;
                }*/
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        Intent i = new Intent(this, ActListaObjetos.class);
        i.putExtra("CODIGO", codDisciplina);
        startActivity(i); //O efeito ao ser pressionado do botão (no caso abre a activity)
        finish(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

    //Método chamado ao clicar no ícone para incluir/salvar Objeto
    private void confirmar() {
        objeto = new Objeto();
        //Método validaCampos() inicializa Objeto com os valores dos campos
        if (validaCampos() == false) {
            try {
                if(alt) {
                    //se for alteração
                    objeto.setCodigo(idalt); //incluindo o código do Objeto alterado
                    objetoRepositorio.alterar(objeto);
                    Toast.makeText(getApplication(), "Objeto alterado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ActEdtObjeto.this,ActListaObjetos.class);
                    i.putExtra("CODIGO", codDisciplina);
                    startActivity(i);
                    finish();
                }else{
                    //se for inserção
                    objetoRepositorio.inserir(objeto);
                    Intent i = new Intent(ActEdtObjeto.this,ActListaObjetos.class);
                    i.putExtra("CODIGO", codDisciplina);
                    startActivity(i);
                    finish();
                }
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
        String nome = edtNome.getText().toString();
        String descricao = edtDescricao.getText().toString();
        String patt = edtPatt.getText().toString();
        String obj = edtObj.getText().toString();
        String imagem = edtImagem.getText().toString();
        String marcador = edtMarcador.getText().toString();

        //Coleta as extensões dos arquivos
        String extPatt = patt.substring(patt.lastIndexOf("."));
        String extObj = obj.substring(obj.lastIndexOf("."));
        String extImg = imagem.substring(imagem.lastIndexOf("."));
        String extMarc = marcador.substring(marcador.lastIndexOf("."));

        //Instancia o Objeto com os valores dos campos
        objeto.setNome(nome);
        objeto.setDescricao(descricao);
        objeto.setPatt(patt);
        objeto.setObj(obj);
        objeto.setImagem(imagem);
        objeto.setMarcador(marcador);
        objeto.setCodDisciplina(codDisciplina);

        //Verifica se há algum campo vazio
        if (res = isCampoVazio(nome)) {
            edtNome.requestFocus();
        } else {
            if (res = isCampoVazio(descricao)) {
                edtDescricao.requestFocus();
            } else {
                if (res = isCampoVazio(patt)) {
                    edtPatt.requestFocus();
                } else {
                    if (res = isCampoVazio(obj)) {
                        edtObj.requestFocus();
                    } else {
                        if (res = isCampoVazio(imagem)) {
                            edtImagem.requestFocus();
                        } else {
                            if (res = isCampoVazio(marcador)) {
                                edtMarcador.requestFocus();
                            }
                        }
                    }
                }
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
            if (!isPatt(extPatt)) { //!true = está no formato - não entra no if / !false = não está no formato - entra no if
                dialogExtensao("Insira um arquivo no formato .patt no campo Arquivo .patt");
                res = true;
            }
            if (!isObj(extObj)) {
                dialogExtensao("Insira um arquivo no formato .obj no campo Arquivo .obj");
                res = true;
            }
            if (!isImg(extImg)) {
                dialogExtensao("Insira um arquivo de imagem no campo Imagem");
                res = true;
            }
            if (!isImg(extMarc)) {
                dialogExtensao("Insira um arquivo de imagem no campo Marcador");
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

    //Verifica se a extensão do arquivo é .patt
    private boolean isPatt(String valor) {
        boolean resultado = valor.equals(".patt");
        return resultado;//true para extensão ok
    }
    //Verifica se a extensão do arquivo é .obj
    private boolean isObj(String valor) {
        boolean resultado = valor.equals(".obj");
        return resultado; //true para extensão ok
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


    //Método que chama o Sistema de Arquivos
    private void showChooser(int btn) {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(target, getString(R.string.chooser_title));
        try {
            if(btn==1) { // 1 indica que botão de Arquivo .patt foi clicado
                startActivityForResult(intent, REQUEST_CODE_PATT);//chama classe para tratar resultado correspondente ao campo Arquivo .patt
            }
            if(btn==2){ // 2 indica que botão de Arquivo .obj foi clicado
                startActivityForResult(intent, REQUEST_CODE_OBJ);//chama classe para tratar resultado correspondente ao campo Arquivo .obj
            }
            if(btn==3){ // 3 indica que botão de Imagem foi clicado
                startActivityForResult(intent, REQUEST_CODE_IMAGEM);//chama classe para tratar resultado correspondente ao campo Imagem
            }
            if(btn==4){ // 4 indica que botão de Marcador foi clicado
                startActivityForResult(intent, REQUEST_CODE_MARCADOR);//chama classe para tratar resultado correspondente ao campo Marcador
            }
        } catch (ActivityNotFoundException e) {
            /*AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(R.string.message_erro_arquivo);
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();*/
        }
    }

    //Método que trata o resultado (arquivo selecionado)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PATT:
                // Se a seleção ocorreu com sucesso
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Retorno do caminho do arquivo selecionado
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Passando caminho para String
                            final String path = FileUtils.getPath(this, uri);
                            //Inserindo o caminho selecionado no campo Arquivo .Patt
                            edtPatt.setText(path);
                            Toast.makeText(ActEdtObjeto.this,
                                    "Arquivo selecionado: " + path, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("FileSelectorTestAct", "File select error", e);
                        }
                    }
                }
                break;
            case REQUEST_CODE_OBJ:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            final String path = FileUtils.getPath(this, uri);
                            //Inserindo o caminho selecionado no campo Arquivo .Obj
                            edtObj.setText(path);
                            Toast.makeText(ActEdtObjeto.this,
                                    "Arquivo selecionado: " + path, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("FileSelectorTestAct", "File select error", e);
                        }
                    }
                }
                break;
            case REQUEST_CODE_IMAGEM:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            final String path = FileUtils.getPath(this, uri);
                            //Inserindo o caminho selecionado no campo Imagem
                            edtImagem.setText(path);
                            Toast.makeText(ActEdtObjeto.this,
                                    "Arquivo selecionado: " + path, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("FileSelectorTestAct", "File select error", e);
                        }
                    }
                }
                break;
            case REQUEST_CODE_MARCADOR:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            final String path = FileUtils.getPath(this, uri);
                            //Inserindo o caminho selecionado no campo Marcador
                            edtMarcador.setText(path);
                            Toast.makeText(ActEdtObjeto.this,
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

