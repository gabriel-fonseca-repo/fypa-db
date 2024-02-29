package com.fypa.projdb.beans;

import com.fypa.projdb.ds.Bucket;
import com.fypa.projdb.ds.Pagina;
import com.fypa.projdb.ds.Tabela;
import com.fypa.projdb.ds.Tupla;
import com.fypa.projdb.util.FileUtil;
import com.fypa.projdb.util.PFUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

@Named
@SessionScoped
public class DatabaseBean implements Serializable {

    private Tabela tabela;

    private Pagina pagina;

    private String idxPagina = "Busque uma chave!";

    private String chaveDeBusca;

    private ArrayList<Bucket> buckets;

    private Integer tamanhoPagina;

    private File arquivoParaLer;

    private BigDecimal porcentagemColisoes;

    private ArrayList<Tupla> registrosParaExibir;

    private Integer qtdRegistrosPorPagina = 10;

    @PostConstruct
    public void init() {
        this.arquivoParaLer = null;
        System.gc();

        this.arquivoParaLer = FileUtil.getFileFromRsc("words.txt");
    }

    private void carregarBanco() {
        this.tabela = null;
        System.gc();

        this.tabela = new Tabela(arquivoParaLer, tamanhoPagina);
        this.buckets = Bucket.carregarBucket(tabela.getPaginas(), tabela.getQtdLinhas());
        this.porcentagemColisoes = Bucket.calcularPorcentagem(Bucket.COLISOES, Bucket.QTD_LINHAS);
        this.registrosParaExibir = tabela.getRegistros();
    }

    public String buscar() {
        try {
            Integer idxPaginaConsulta = Bucket.consultarIdxPagina(chaveDeBusca, buckets);
            this.pagina = tabela.getPaginas().get(idxPaginaConsulta);
            this.registrosParaExibir = this.pagina.getDadosPagina();
            this.idxPagina = idxPaginaConsulta.toString();
        } catch (RuntimeException re) {
            PFUtil.error(re.getMessage());
        }
        return null;
    }

    public String resetarTabela() {
        this.registrosParaExibir = tabela.getRegistros();
        this.pagina = null;
        this.idxPagina = "Busque uma chave!";
        return null;
    }

    public void validarTamanhoPagina() {
        if (tamanhoPagina == null) {
            PFUtil.abrirDialog("definirTamanhoPaginaDialog");
        } else {
            PFUtil.fecharDialog("definirTamanhoPaginaDialog");
        }
    }

    public String gravarTamanhoPagina() {

        if (tamanhoPagina == null) {
            PFUtil.error("O valor de tamanho da página é obrigatório!");
            PFUtil.abrirDialog("definirTamanhoPaginaDialog");
            return null;
        }
        if (tamanhoPagina <= 0) {
            PFUtil.error("O valor de tamanho da página deve ser inteiro maior que zero!");
            PFUtil.abrirDialog("definirTamanhoPaginaDialog");
            return null;
        }

        carregarBanco();
        PFUtil.fecharDialog("definirTamanhoPaginaDialog");
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

    public Tabela getTabela() {
        return tabela;
    }

    public void setTabela(Tabela tabela) {
        this.tabela = tabela;
    }

    public Integer getTamanhoPagina() {
        return tamanhoPagina;
    }

    public void setTamanhoPagina(Integer tamanhoPagina) {
        this.tamanhoPagina = tamanhoPagina;
    }

    public File getArquivoParaLer() {
        return arquivoParaLer;
    }

    public void setArquivoParaLer(File arquivoParaLer) {
        this.arquivoParaLer = arquivoParaLer;
    }

    public BigDecimal getPorcentagemColisoes() {
        return porcentagemColisoes;
    }

    public void setPorcentagemColisoes(BigDecimal porcentagemColisoes) {
        this.porcentagemColisoes = porcentagemColisoes;
    }

    public Integer getQtdRegistrosPorPagina() {
        return qtdRegistrosPorPagina;
    }

    public void setQtdRegistrosPorPagina(Integer qtdRegistrosPorPagina) {
        this.qtdRegistrosPorPagina = qtdRegistrosPorPagina;
    }

    public ArrayList<Tupla> getRegistrosParaExibir() {
        return registrosParaExibir;
    }

    public void setRegistrosParaExibir(ArrayList<Tupla> registrosParaExibir) {
        this.registrosParaExibir = registrosParaExibir;
    }

    public String getIdxPagina() {
        return idxPagina;
    }

    public void setIdxPagina(String idxPagina) {
        this.idxPagina = idxPagina;
    }
}

