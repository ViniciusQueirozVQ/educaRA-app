package org.artoolkit.ar.samples.QuiRA.dominio.entidades;

import java.io.Serializable;

/**
 * Created by letic on 03/12/2017.
 */

public class Objeto implements Serializable {
    //Atributos da classe Objeto (representa a molécula)
    private int codigo; //código do registro
    private String nome; //nome da molécula
    private String descricao; //descrição
    private String patt; //caminho do arquivo .patt
    private String obj; //caminho do arquivo .obj
    private String imagem; //caminho da imagem da molécula em 3D
    private String marcador; //caminho da imagem do marcador
    private int codDisciplina;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPatt() {
        return patt;
    }

    public void setPatt(String patt) {
        this.patt = patt;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getMarcador() {
        return marcador;
    }

    public void setMarcador(String marcador) {
        this.marcador = marcador;
    }

    public int getCodDisciplina() {
        return codDisciplina;
    }

    public void setCodDisciplina(int codDisciplina) {
        this.codDisciplina = codDisciplina;
    }
}


