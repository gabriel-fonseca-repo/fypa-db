package com.fypa.projdb.ds;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Getter
@Setter
public class Bucket {

    public static final Integer MAX_TAMANHO_BUCKET = 30;

    private ArrayList<Tupla> registros;

    private ArrayList<Bucket> overflow;

    public static Integer MAX_QTD_BUCKETS;

    public static Integer COLISOES;

    public static Integer OVERFLOW;

    public static Integer QTD_LINHAS;

    public Bucket() {
        this.registros = new ArrayList<>();
        this.overflow = new ArrayList<>();
    }

    public static ArrayList<Bucket> carregarBucket(ArrayList<Pagina> paginas, Integer qtdLinhas) {
        ArrayList<Bucket> buckets = new ArrayList<>();

        QTD_LINHAS = qtdLinhas;

        MAX_QTD_BUCKETS = (int) (double) (QTD_LINHAS / MAX_TAMANHO_BUCKET);

        for (int i = 0; i < MAX_QTD_BUCKETS; i++)
            buckets.add(new Bucket());

        COLISOES = 0;
        OVERFLOW = 0;

        for (Pagina pagina : paginas) {
            for (Tupla tupla : pagina.getDadosPagina()) {

                int hash = Bucket.hash(tupla.getDados());

                Bucket bucket = buckets.get(hash);

                if (bucket.isNotCheio()) {
                    bucket.getRegistros().add(tupla);
                } else {

                    COLISOES += 1;

                    if (bucket.getOverflow().isEmpty()) {
                        bucket.getOverflow().add(new Bucket());
                    }

                    if (bucket.getOverflow().getLast().isNotCheio()) {
                        if (bucket.getOverflow().getLast().getRegistros().isEmpty()) {
                            OVERFLOW += 1;
                        }
                        bucket.getOverflow().getLast().getRegistros().add(tupla);
                    } else {
                        OVERFLOW += 1;
                        Bucket novoOverflow = new Bucket();
                        novoOverflow.getRegistros().add(tupla);
                        bucket.getOverflow().add(novoOverflow);
                    }

                }

            }
        }

        return buckets;
    }

    public static Integer consultarIdxPagina(String dado, ArrayList<Bucket> buckets) {

        Integer hashConsulta = Bucket.hash(dado);

        Bucket bucket = buckets.get(hashConsulta);

        ArrayList<Tupla> registros = bucket.getRegistros();

        Optional<Tupla> strikeConsulta = registros.stream().filter((t) -> t.getDados().equals(dado)).findFirst();
        if (strikeConsulta.isPresent()) {
            return strikeConsulta.get().getIndicePagina();
        } else {

            ArrayList<Bucket> overflow = bucket.getOverflow();
            for (Bucket bucketOvf : overflow) {
                registros = bucketOvf.getRegistros();
                strikeConsulta = registros.stream().filter((t) -> t.getDados().equals(dado)).findFirst();
                if (strikeConsulta.isPresent()) {
                    return strikeConsulta.get().getIndicePagina();
                }
            }

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

    public static BigDecimal calcularPorcentagem(int parte, int total) {
        if (total == 0) {
            throw new IllegalArgumentException("Total não pode ser zero!");
        }

        return BigDecimal.valueOf(((double) parte / total) * 100);
    }

    public boolean isNotCheio() {
        return this.registros.size() < MAX_TAMANHO_BUCKET;
    }

}
