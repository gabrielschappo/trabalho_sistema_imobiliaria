package br.edu.univille.dao;

import java.math.BigDecimal;

public class Imovel {
    private long id;
    private String endereco;
    private String tipo;
    private int quartos;
    private int banheiros;
    private BigDecimal valorAluguelBase;
    private boolean disponivel;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuartos() {
        return quartos;
    }

    public void setQuartos(int quartos) {
        this.quartos = quartos;
    }

    public int getBanheiros() {
        return banheiros;
    }

    public void setBanheiros(int banheiros) {
        this.banheiros = banheiros;
    }

    public BigDecimal getValorAluguelBase() {
        return valorAluguelBase;
    }

    public void setValorAluguelBase(BigDecimal valorAluguelBase) {
        this.valorAluguelBase = valorAluguelBase;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
