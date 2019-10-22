package it.projectalpha.howispend.utilities;

import it.projectalpha.howispend.core.registrazione.NuovoUtenteActivity;

public class NuovoUtenteObject {

    private static NuovoUtenteObject instance;

    private String nome;
    private String cognome;


    private NuovoUtenteObject() {}

    public static NuovoUtenteObject getInstance() {
        if(instance == null) {
            instance = new NuovoUtenteObject();
        }
        return instance;
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
}
