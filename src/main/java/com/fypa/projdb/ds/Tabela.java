package com.fypa.projdb.ds;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Getter
@Setter
public class Tabela {

    private final ArrayList<Pagina> paginas;

    private final ArrayList<Tupla> registros;

    private final Integer qtdLinhas;

    public Tabela(File arquivoParaLer, Integer tamanhoPagina) {
        this.paginas = carregarPaginas(arquivoParaLer, tamanhoPagina);
        this.registros = carregarRegistros(paginas);
        this.qtdLinhas = lerQtdLinhas(arquivoParaLer);
    }

    private ArrayList<Tupla> carregarRegistros(ArrayList<Pagina> paginas) {
        List<Tupla> registrosLcl = paginas.stream().map(Pagina::getDadosPagina).flatMap(Collection::stream).toList();
        return new ArrayList<>(registrosLcl);
    }

    public ArrayList<Pagina> carregarPaginas(File arquivoParaLer, Integer tamanhoPagina) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(arquivoParaLer));
            String dados = "";

            Pagina pagina = new Pagina(tamanhoPagina);
            Integer paginaIdx = 0;
            ArrayList<Pagina> paginas = new ArrayList<>();

            while (dados != null) {
                while (pagina.isNotCheia()) {

                    if (dados.equals("hoicking")) {
                        System.out.println("Dado 'hoicking' na página " + paginaIdx + ".");
                    }

                    Tupla registro = new Tupla(paginaIdx, dados);
                    pagina.adicionarRegistro(registro);
                    dados = br.readLine();
                    if (dados == null)
                        break;
                }
                paginas.add(pagina);
                pagina = new Pagina(tamanhoPagina);
                paginaIdx++;
            }

            br.close();
            return paginas;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar carregar as páginas do arquivo! Erro: " + e.getMessage());
        }
    }

    private Integer lerQtdLinhas(File arquivoParaLer) {
        int linhas = 0;
        try (Scanner sc = new Scanner(arquivoParaLer)) {
            while (sc.hasNextLine()) {
                sc.nextLine();
                linhas++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar ler a quantidade de linhas do arquivo! Erro: " + e.getMessage());
        }
        return linhas;
    }

}
