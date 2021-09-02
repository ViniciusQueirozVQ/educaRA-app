package org.artoolkit.ar.samples.QuiRA.dominio.entidades;

import java.io.Serializable;

/**
 * Created by filip on 11/07/2018.
 */

public class Disciplina implements Serializable{

    private int codigo_disciplina;
    public String nome;
    public String imagem;

    public int getCodigo_disciplina() {
        return codigo_disciplina;
    }

    public void setCodigo_disciplina(int codigo_disciplina) {
        this.codigo_disciplina = codigo_disciplina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
