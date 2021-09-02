package org.artoolkit.ar.samples.QuiRA.dominio.repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.artoolkit.ar.samples.QuiRA.dominio.entidades.Objeto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by letic on 03/12/2017.
 */

public class ObjetoRepositorio {
    //Classe reponsável pela comunicação do Objeto com o banco de dados
    private SQLiteDatabase conexao;

    public ObjetoRepositorio(SQLiteDatabase conexao){

        this.conexao = conexao;
    }

    //Inserção do Objeto no banco de dados
    public void inserir(Objeto objeto){
        ContentValues contentValues = new ContentValues();
        contentValues.put("COD_DISCIPLINA", objeto.getCodDisciplina());
        contentValues.put("NOME", objeto.getNome());
        contentValues.put("DESCRICAO", objeto.getDescricao());
        contentValues.put("PATT", objeto.getPatt());
        contentValues.put("OBJ", objeto.getObj());
        contentValues.put("IMAGEM", objeto.getImagem());
        contentValues.put("MARCADOR", objeto.getMarcador());

        conexao.insertOrThrow("OBJETO", null, contentValues);
    }

    //Alteração do Objeto
    public void alterar(Objeto objeto){
        ContentValues contentValues = new ContentValues();
        contentValues.put("COD_DISCIPLINA", objeto.getCodDisciplina());
        contentValues.put("NOME", objeto.getNome());
        contentValues.put("DESCRICAO", objeto.getDescricao());
        contentValues.put("PATT", objeto.getPatt());
        contentValues.put("OBJ", objeto.getObj());
        contentValues.put("IMAGEM", objeto.getImagem());
        contentValues.put("MARCADOR", objeto.getMarcador());

        String[] args = {String.valueOf(objeto.getCodigo())};
        conexao.update("OBJETO", contentValues,"CODIGO=?",args);
    }

    //Exclusão do objeto
    public void excluir(Objeto objeto){
        String[] args = {String.valueOf(objeto.getCodigo())};
        conexao.delete("OBJETO", "CODIGO=?",args);
    }

    //Retorna todos os objetos cadastrados
    public List<Objeto> buscarTodos(int codDisciplina){
        List<Objeto> objetos = new ArrayList<Objeto>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CODIGO, NOME, DESCRICAO, PATT, OBJ, IMAGEM , MARCADOR ");
        sql.append("FROM OBJETO ");
        sql.append("WHERE COD_DISCIPLINA = "+codDisciplina+" ");
        sql.append("ORDER BY NOME");
        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        if(resultado.getCount()>0) {
            resultado.moveToFirst();
            do{
                Objeto obj = new Objeto();
                obj.setCodigo(resultado.getInt(resultado.getColumnIndexOrThrow("CODIGO")));
                obj.setNome(resultado.getString(resultado.getColumnIndexOrThrow("NOME")));
                obj.setDescricao(resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO")));
                obj.setPatt(resultado.getString(resultado.getColumnIndexOrThrow("PATT")));
                obj.setObj(resultado.getString(resultado.getColumnIndexOrThrow("OBJ")));
                obj.setImagem(resultado.getString(resultado.getColumnIndexOrThrow("IMAGEM")));
                obj.setMarcador(resultado.getString(resultado.getColumnIndexOrThrow("MARCADOR")));
                objetos.add(obj);

            } while(resultado.moveToNext());
        }
        return objetos;
    }

    //Retorna todas as URL de arquivos .obj dos Objetos
    //Utilizado na classe ARObjetoActivity
    //Retorna um array de strings para facilitar a passagem para o arquivo ARWrapperObjeto.cpp
    public String [] buscarObjs(){
        List<String> objs = new ArrayList<String>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT OBJ ");
        sql.append("FROM OBJETO");
        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        if(resultado.getCount()>0) {
            resultado.moveToFirst();
            do{
                String obj = resultado.getString(resultado.getColumnIndexOrThrow("OBJ"));
                objs.add(obj);

            } while(resultado.moveToNext());
        }
        String[] objsArray = objs.toArray(new String[0]);
        return objsArray;
    }


    //Retorna todas as URL de arquivos .patt dos Objetos
    //Utilizado na classe ARObjetoActivity
    //Retorna um array de strings para facilitar a passagem para o arquivo ARWrapperObjeto.cpp
    public String [] buscarPatts(){
        List<String> patts = new ArrayList<String>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PATT ");
        sql.append("FROM OBJETO");
        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        if(resultado.getCount()>0) {
            resultado.moveToFirst();
            do{
                //single (indica que é um marcador singular) e 80 ->particularidadoes da biblioteca ARToolkit
                String patt = "single;"+resultado.getString(resultado.getColumnIndexOrThrow("PATT"))+";80";
                patts.add(patt);
            } while(resultado.moveToNext());
        }
        String[] pattsArray = patts.toArray(new String[0]);
        return pattsArray;
    }

}
