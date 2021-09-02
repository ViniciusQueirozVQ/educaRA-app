package org.artoolkit.ar.samples.QuiRA.dominio.repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.artoolkit.ar.samples.QuiRA.dominio.entidades.Disciplina;
import org.artoolkit.ar.samples.QuiRA.dominio.entidades.Objeto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filip on 11/07/2018.
 */

public class DisciplinaRepositorio {

    //Classe reponsável pela comunicação do Objeto com o banco de dados
    private SQLiteDatabase conexao;

    public DisciplinaRepositorio(SQLiteDatabase conexao){

        this.conexao = conexao;
    }

    //Cadastra uma nova Disciplina
    public void inserir(Disciplina disciplina){

        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", disciplina.getNome());
        contentValues.put("IMAGEM", disciplina.getImagem());

        conexao.insertOrThrow("DISCIPLINA", null, contentValues);

    }

    //Lista todas as disciplinas cadastradas
    public List<Disciplina> listarDisciplinas(){

        List<Disciplina> disciplinas = new ArrayList<Disciplina>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CODIGO_DISCIPLINA, NOME, IMAGEM ");
        sql.append("FROM DISCIPLINA");
        sql.append("ORDER BY NOME");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        if(resultado.getCount() > 0){
            resultado.moveToFirst();
            do{
                Disciplina d = new Disciplina();
                d.setCodigo_disciplina(resultado.getInt(resultado.getColumnIndexOrThrow("CODIGO_DISCIPLINA")));
                d.setNome(resultado.getString(resultado.getColumnIndexOrThrow("NOME")));
                d.setImagem(resultado.getString(resultado.getColumnIndexOrThrow("IMAGEM")));
                disciplinas.add(d);
            }while (resultado.moveToNext());
        }
        return disciplinas;
    }

    //Retorna todos os objetos cadastrados
    public List<Disciplina> buscarTodos(){
        List<Disciplina> disciplinas = new ArrayList<Disciplina>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CODIGO_DISCIPLINA, NOME, IMAGEM ");
        sql.append("FROM DISCIPLINA ");
        sql.append("ORDER BY NOME");
        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        if(resultado.getCount()>0) {
            resultado.moveToFirst();
            do{
                Disciplina d = new Disciplina();
                d.setCodigo_disciplina(resultado.getInt(resultado.getColumnIndexOrThrow("CODIGO_DISCIPLINA")));
                d.setNome(resultado.getString(resultado.getColumnIndexOrThrow("NOME")));
                d.setImagem(resultado.getString(resultado.getColumnIndexOrThrow("IMAGEM")));
                disciplinas.add(d);

            } while(resultado.moveToNext());
        }
        return disciplinas;
    }

    //Alteração do Objeto
    public void alterar(Disciplina disciplina){
        ContentValues contentValues = new ContentValues();
        contentValues.put("CODIGO_DISCIPLINA", disciplina.getCodigo_disciplina());
        contentValues.put("NOME", disciplina.getNome());
        contentValues.put("IMAGEM", disciplina.getImagem());

        String[] args = {String.valueOf(disciplina.getCodigo_disciplina())};
        conexao.update("DISCIPLINA", contentValues,"CODIGO_DISCIPLINA=?",args);
    }

    //Exclusão da disciplina
    public void excluir(Disciplina disciplina){
        String[] args = {String.valueOf(disciplina.getCodigo_disciplina())};
        conexao.delete("DISCIPLINA", "CODIGO_DISCIPLINA=?",args);
    }

}



