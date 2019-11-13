package it.projectalpha.howispend.model;

import java.time.LocalDate;

@SuppressWarnings("unused")
public class Operazione {

    private Integer id;
    private String motivo;
    private double costo;
    private LocalDate dataCreazione;
    private Integer idMese;


    public Operazione(Integer id, String motivo, double costo, LocalDate dataCreazione, Integer idMese) {
        this.id = id;
        this.motivo = motivo;
        this.costo = costo;
        this.dataCreazione = dataCreazione;
        this.idMese = idMese;
    }

    public Operazione(String motivo, double costo, LocalDate dataCreazione, Integer idMese) {
        this.motivo = motivo;
        this.costo = costo;
        this.dataCreazione = dataCreazione;
        this.idMese = idMese;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Integer getIdMese() {
        return idMese;
    }

    public void setIdMese(Integer idMese) {
        this.idMese = idMese;
    }
}
