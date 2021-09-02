package org.artoolkit.ar.samples.QuiRA.interfaces;

import android.view.View;

import org.artoolkit.ar.samples.QuiRA.dominio.entidades.Objeto;

/**
 * Created by letic on 20/01/2018.
 */

public interface RecyclerViewOnClickListener {
    void onClickListener(Object object);
    void onClickImageButtonDelete(Object object);
    void onClickImageButtonEdit(Object object);

}
