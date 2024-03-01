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
import java.util.List;
import java.util.stream.Collectors;

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

    private BigDecimal porcentagemOverflow;

    private ArrayList<Tupla> registrosParaExibir;

    private Integer tableScan = 10;

    private Integer custoDaUltimaOperacao;

    private String ultimaOperacao;

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
        this.porcentagemOverflow = Bucket.calcularPorcentagem(Bucket.OVERFLOW, Bucket.MAX_QTD_BUCKETS);
        tableScan();
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
        tableScan();
        this.pagina = null;
        this.idxPagina = "Busque uma chave!";
        return null;
    }

    public String carregarTodosOsDados() {
        this.registrosParaExibir = tabela.getRegistros();
        return null;
    }

    public void validarTamanhoPagina() {
        if (tamanhoPagina == null) {
            PFUtil.abrirDialog("definirTamanhoPaginaDialog");
        } else {
            PFUtil.fecharDialog("definirTamanhoPaginaDialog");
        }
    }

    public void tableScan() {
        if (tableScan == null || tableScan <= 0) {
            tableScan = 10;
        }

        List<Tupla> listaRst = tabela.getRegistros().stream().limit(tableScan).toList();
        this.custoDaUltimaOperacao = listaRst.getLast().getIndicePagina();
        this.ultimaOperacao = "Table Scan";
        this.registrosParaExibir = new ArrayList<>(listaRst);
    }

    public void gravarTamanhoPagina() {

        if (tamanhoPagina == null) {
            PFUtil.error("O valor de tamanho da página é obrigatório!");
            PFUtil.abrirDialog("definirTamanhoPaginaDialog");
        }

        if (tamanhoPagina <= 0) {
            PFUtil.error("O valor de tamanho da página deve ser inteiro maior que zero!");
            PFUtil.abrirDialog("definirTamanhoPaginaDialog");
        }

        carregarBanco();
        PFUtil.fecharDialog("definirTamanhoPaginaDialog");
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

    public Integer getTableScan() {
        return tableScan;
    }

    public void setTableScan(Integer tableScan) {
        this.tableScan = tableScan;
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

    public Integer getCustoDaUltimaOperacao() {
        return custoDaUltimaOperacao;
    }

    public void setCustoDaUltimaOperacao(Integer custoDaUltimaOperacao) {
        this.custoDaUltimaOperacao = custoDaUltimaOperacao;
    }

    public String getUltimaOperacao() {
        return ultimaOperacao;
    }

    public void setUltimaOperacao(String ultimaOperacao) {
        this.ultimaOperacao = ultimaOperacao;
    }

    public BigDecimal getPorcentagemOverflow() {
        return porcentagemOverflow;
    }

    public void setPorcentagemOverflow(BigDecimal porcentagemOverflow) {
        this.porcentagemOverflow = porcentagemOverflow;
    }
}

