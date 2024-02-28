package com.fypa.projdb.ds;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Pagina {

    private ArrayList<Tupla> dadosPagina;

    private Integer tamanhoPagina;

    public Pagina(Integer tamanhoPagina) {
        this.tamanhoPagina = tamanhoPagina;
        this.dadosPagina = new ArrayList<>();
    }

    public void adicionarRegistro(Tupla registro) {
        if (this.isNotCheia()) {
            this.dadosPagina.add(registro);
        } else {
            throw new RuntimeException("Overflow! Caso ocorrido ao tentar inserir o dado: " + registro.getDados() + ".");
        }
    }

    public boolean isNotCheia() {
        return this.dadosPagina.size() < tamanhoPagina;
    }

}
