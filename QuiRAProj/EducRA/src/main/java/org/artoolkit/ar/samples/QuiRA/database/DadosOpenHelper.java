package org.artoolkit.ar.samples.QuiRA.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by letic on 02/12/2017.
 */

public class DadosOpenHelper extends SQLiteOpenHelper {
    //Classe para gerenciar a criação de banco de dados e o gerenciamento de versões.
    public DadosOpenHelper(Context context) {

        super(context, "DADOS", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(ScriptDDL.getCreateTableDisciplina());
        db.execSQL(ScriptDDL.getCreateTableObjeto());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
