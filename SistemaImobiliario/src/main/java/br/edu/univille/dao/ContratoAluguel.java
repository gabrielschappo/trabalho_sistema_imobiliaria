package br.edu.univille.dao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContratoAluguel {
    private long id;
    private long idCliente;
    private long idImovel;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private BigDecimal valorAluguelPactuado;
    private boolean ativo;

    private Cliente cliente;
    private Imovel imovel;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public BigDecimal getValorAluguelPactuado() {
        return valorAluguelPactuado;
    }

    public void setValorAluguelPactuado(BigDecimal valorAluguelPactuado) {
        this.valorAluguelPactuado = valorAluguelPactuado;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public long getIdImovel() {
        return idImovel;
    }

    public void setIdImovel(long idImovel) {
        this.idImovel = idImovel;
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }
}
