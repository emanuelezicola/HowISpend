package it.projectalpha.howispend.model;


import java.time.LocalDate;

public class Anno {

    private Integer id;
    private String anno;
    private LocalDate dataCreazione;
    private Double spesaParziale;
    private Double spesaFinale;
    private Double risparmioFinale;
    private Boolean isAperto;

    public Anno(Integer id, String anno, LocalDate dataCreazione,
                Double spesaParziale, Double spesaFinale, Double risparmioFinale,
                Boolean isAperto) {
        this.id = id;
        this.anno = anno;
        this.dataCreazione = dataCreazione;
        this.spesaParziale = spesaParziale;
        this.spesaFinale = spesaFinale;
        this.risparmioFinale = risparmioFinale;
        this.isAperto = isAperto;
    }

    public Anno(String anno, LocalDate dataCreazione,
                Double spesaParziale, Double spesaFinale, Double risparmioFinale,
                Boolean isAperto) {
        this.anno = anno;
        this.dataCreazione = dataCreazione;
        this.spesaParziale = spesaParziale;
        this.spesaFinale = spesaFinale;
        this.risparmioFinale = risparmioFinale;
        this.isAperto = isAperto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Double getSpesaParziale() {
        return spesaParziale;
    }

    public void setSpesaParziale(Double spesaParziale) {
        this.spesaParziale = spesaParziale;
    }

    public Double getSpesaFinale() {
        return spesaFinale;
    }

    public void setSpesaFinale(Double spesaFinale) {
        this.spesaFinale = spesaFinale;
    }

    public Double getRisparmioFinale() {
        return risparmioFinale;
    }

    public void setRisparmioFinale(Double risparmioFinale) {
        this.risparmioFinale = risparmioFinale;
    }

    public Boolean getAperto() {
        return isAperto;
    }

    public void setAperto(Boolean aperto) {
        isAperto = aperto;
    }


}
