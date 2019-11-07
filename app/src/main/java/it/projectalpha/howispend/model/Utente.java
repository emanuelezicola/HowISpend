package it.projectalpha.howispend.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("unused")
public class Utente implements Serializable {

    private Integer id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private LocalDate dataCreazione;

    public Utente(@NonNull Integer id, @NonNull String nome, @NonNull String cognome, @NonNull String email,
                  @NonNull String password, @NonNull LocalDate dataCreazione) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.dataCreazione = dataCreazione;
    }

    public Utente(@NonNull String nome, @NonNull String cognome, @NonNull String email,
                  @NonNull String password, @NonNull LocalDate dataCreazione) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.dataCreazione = dataCreazione;
    }

    public Utente() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }


    @NonNull
    @Override
    public String toString() {
        return "Utente[" + id + ", " + email + ", " + password + "]";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Utente) {
            Utente utente = (Utente) obj;
            return this.id.equals(utente.id) &&
                    this.email.equals(utente.email) &&
                    this.password.equals(utente.password);
        }
        return false;
    }
}
