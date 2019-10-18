package it.projectalpha.howispend.utilities;

import it.projectalpha.howispend.model.Utente;

public class Session {

    private Utente loggedUser;

    private static Session instance;

    private Session() {}

    public static Session getInstance() {
        if(instance == null) {
            instance = new Session();
        }
        return instance;
    }


    public Utente getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Utente loggedUser) {
        this.loggedUser = loggedUser;
    }
}
