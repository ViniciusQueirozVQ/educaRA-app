package org.artoolkit.ar.samples.QuiRA.database;

/**
 * Created by letic on 02/12/2017.
 */

public class ScriptDDL {

    public static String getCreateTableDisciplina(){

        StringBuilder sql = new StringBuilder();

        sql.append(" CREATE TABLE IF NOT EXISTS DISCIPLINA( ");
        sql.append(" CODIGO_DISCIPLINA INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        sql.append(" NOME VARCHAR (50) NOT NULL DEFAULT (''), ");
        sql.append(" IMAGEM VARCHAR (100) NOT NULL DEFAULT ('') ); ");

        return sql.toString();
    }


    public static String getCreateTableObjeto(){
        StringBuilder sql = new StringBuilder();

        sql.append(" CREATE TABLE IF NOT     EXISTS OBJETO( ");
        sql.append(" CODIGO INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        sql.append(" COD_DISCIPLINA INTEGER NOT NULL, ");
        sql.append(" NOME VARCHAR (30) NOT NULL DEFAULT (''), ");
        sql.append(" DESCRICAO VARCHAR (40) NOT NULL DEFAULT (''), ");
        sql.append(" PATT VARCHAR (100) NOT NULL DEFAULT (''), ");
        sql.append(" OBJ VARCHAR (100) NOT NULL DEFAULT (''), ");
        sql.append(" IMAGEM VARCHAR (100) NOT NULL DEFAULT (''), ");
        sql.append(" MARCADOR VARCHAR (100) NOT NULL DEFAULT (''), ");
        sql.append(" FOREIGN KEY (COD_DISCIPLINA) REFERENCES DISCIPLINA(CODIGO_DISCIPLINA) ); ");

        return sql.toString();
    }


}
