package com.fypa.projdb.beans;

import com.fypa.projdb.ds.Bucket;
import com.fypa.projdb.ds.Pagina;
import com.fypa.projdb.ds.Tabela;
import com.fypa.projdb.util.FileUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

@Named
@ViewScoped
public class DatabaseBean implements Serializable {

    private Tabela tabela;

    private Pagina pagina;

    private Integer idxPagina;

    private String chaveDeBusca;

    private ArrayList<Bucket> buckets;

    @PostConstruct
    public void init() {
        File arquivoParaLer = FileUtil.getFileFromRsc("words.txt");
        this.tabela = new Tabela(arquivoParaLer, 20);
        this.buckets = Bucket.carregarBucket(tabela.getPaginas(), tabela.getQtdLinhas());
        this.pagina = tabela.getPaginas().get(0);
        this.idxPagina = 0;
    }

    public String buscar() {
        Integer idxPaginaConsulta = Bucket.consultarIdxPagina(chaveDeBusca, buckets);
        this.pagina = tabela.getPaginas().get(idxPaginaConsulta);
        this.idxPagina = idxPaginaConsulta;
        return null;
    }

    public Pagina getPagina() {
        return pagina;
    }

    public void setPagina(Pagina pagina) {
        this.pagina = pagina;
    }

    public String getChaveDeBusca() {
        return chaveDeBusca;
    }

    public void setChaveDeBusca(String chaveDeBusca) {
        this.chaveDeBusca = chaveDeBusca;
    }

    public ArrayList<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(ArrayList<Bucket> buckets) {
        this.buckets = buckets;
    }

    public Integer getIdxPagina() {
        return idxPagina;
    }

    public void setIdxPagina(Integer idxPagina) {
        this.idxPagina = idxPagina;
    }

    public Tabela getTabela() {
        return tabela;
    }

    public void setTabela(Tabela tabela) {
        this.tabela = tabela;
    }
}

