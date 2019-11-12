package it.projectalpha.howispend.model;

import java.time.LocalDate;

@SuppressWarnings("unused")
public class Mese implements Comparable<Mese> {

    private Integer id;
    private String mese;
    private Double spesaParziale;
    private Double spesaFinale;
    private Double guadagno;
    private Double introiti;
    private Boolean isAperto;
    private LocalDate dataCreazione;
    private Integer idAnno;


    public Mese(Integer id, String mese, Double spesaParziale, Double spesaFinale, Double guadagno, Double introiti, Boolean isAperto, LocalDate dataCreazione, Integer idAnno) {
        this.id = id;
        this.mese = mese;
        this.spesaParziale = spesaParziale;
        this.spesaFinale = spesaFinale;
        this.guadagno = guadagno;
        this.introiti = introiti;
        this.isAperto = isAperto;
        this.dataCreazione = dataCreazione;
        this.idAnno = idAnno;
    }

    public Mese(String mese, Double spesaParziale, Double spesaFinale, Double guadagno, Double introiti, Boolean isAperto, LocalDate dataCreazione, Integer idAnno) {
        this.mese = mese;
        this.spesaParziale = spesaParziale;
        this.spesaFinale = spesaFinale;
        this.guadagno = guadagno;
        this.introiti = introiti;
        this.isAperto = isAperto;
        this.dataCreazione = dataCreazione;
        this.idAnno = idAnno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMese() {
        return mese;
    }

    public void setMese(String mese) {
        this.mese = mese;
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

    public Double getGuadagno() {
        return guadagno;
    }

    public void setGuadagno(Double guadagno) {
        this.guadagno = guadagno;
    }

    public Double getIntroiti() {
        return introiti;
    }

    public void setIntroiti(Double introiti) {
        this.introiti = introiti;
    }

    public Boolean getAperto() {
        return isAperto;
    }

    public void setAperto(Boolean aperto) {
        isAperto = aperto;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Integer getIdAnno() {
        return idAnno;
    }

    public void setIdAnno(Integer idAnno) {
        this.idAnno = idAnno;
    }

    @Override
    public int compareTo(Mese mese) {
        return mese.getMese().compareTo(this.mese);
    }
}
