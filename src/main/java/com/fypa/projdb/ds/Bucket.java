package com.fypa.projdb.ds;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Optional;

@Getter
@Setter
public class Bucket {

    private ArrayList<Tupla> registros;

    public static final Integer MAX_TAMANHO_BUCKET = 30;

    public static Integer MAX_QTD_BUCKETS;

    public Bucket() {
        this.registros = new ArrayList<>();
    }

    public static ArrayList<Bucket> carregarBucket(ArrayList<Pagina> paginas, Integer qtdLinhas) {
        ArrayList<Bucket> buckets = new ArrayList<>();

        MAX_QTD_BUCKETS = (int) (double) (qtdLinhas / MAX_TAMANHO_BUCKET);

        for (int i = 0; i < MAX_QTD_BUCKETS; i++)
            buckets.add(new Bucket());

        int colisoes = 0;

        for (Pagina pagina : paginas) {
            for (Tupla tupla : pagina.getDadosPagina()) {

                int hash = Bucket.hash(tupla.getDados());

                if (buckets.get(hash).isNotCheio()) {
                    buckets.get(hash).getRegistros().add(tupla);
                } else {
                    colisoes += 1;
                }

            }
        }

        System.out.println("Porcentagem de colisão no preenchimento do Bucket: " + calcularPorcentagem(colisoes, qtdLinhas));
        return buckets;
    }

    public static Integer consultarIdxPagina(String dado, ArrayList<Bucket> buckets) {

        Integer hashConsulta = Bucket.hash(dado);

        ArrayList<Tupla> registros = buckets.get(hashConsulta).getRegistros();

        Optional<Tupla> strikeConsulta = registros.stream().filter((t) -> t.getDados().equals(dado)).findFirst();
        if (strikeConsulta.isPresent()) {
            return strikeConsulta.get().getIndicePagina();
        } else {
            throw new RuntimeException("Dado '" + dado + "' não encontrado no bucket!");
        }

    }

    public static Integer hash(String dado) {
        int hash = 0;
        for (int i = 0; i < dado.length(); i++) {
            // 92821 | 31
            hash = (92821 * hash + dado.charAt(i)) % MAX_QTD_BUCKETS;
        }
        return Math.abs(hash);
    }

    public static double calcularPorcentagem(int parte, int total) {
        if (total == 0) {
            throw new IllegalArgumentException("Total não pode ser zero!");
        }
        return ((double) parte / total) * 100;
    }

    public boolean isNotCheio() {
        return this.registros.size() < MAX_TAMANHO_BUCKET;
    }

}
