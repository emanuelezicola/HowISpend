package it.projectalpha.howispend.core.filters;

import it.projectalpha.howispend.utilities.Session;

public class CheckAuthFilter {



    private Session session;
    private static CheckAuthFilter instance;

    private CheckAuthFilter() {
        session = Session.getInstance();
    }

    public static CheckAuthFilter getInstance() {
        if(instance == null) {
            instance = new CheckAuthFilter();
        }
        return instance;
    }


    public boolean filter() {
        return session.getLoggedUser() == null;
    }






}
