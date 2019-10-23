package it.projectalpha.howispend.utilities;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Base64;

import it.projectalpha.howispend.model.Utente;

@SuppressWarnings("all")
public class Constants {

    private static Constants instance;

    //UTENTE
    private final String URL_LOGIN = "https://howispend.altervista.org/utente/login.php";
    private final String URL_NUOVO_UTENTE = "https://howispend.altervista.org/utente/nuovoUtente.php";

    //MESE
    private final String URL_ALL_MESI = "https://howispend.altervista.org/mese/fetchAllMesi.php";
    private final String URL_MESE_BY_ID = "https://howispend.altervista.org/mese/fetchMeseById.php";
    private final String URL_RIMANENZE_MESE = "https://howispend.altervista.org/mese/rimanenzeMese.php";
    private final String URL_UPDATE_NOME_MESE = "https://howispend.altervista.org/mese/modificaMese.php";
    private final String URL_NUOVO_MESE = "https://howispend.altervista.org/mese/nuovoMese.php";
    private final String URL_ELIMINA_MESE = "https://howispend.altervista.org/mese/eliminaMese.php";
    private final String URL_CHIUDI_MESE = "https://howispend.altervista.org/mese/chiudiMese.php";
    private final String URL_MESI_BY_ANNO = "https://howispend.altervista.org/mese/fetchMesiByAnno.php";


    //OPERAZIONE
    private final String URL_AGGIORNA_OPERAZIONE = "https://howispend.altervista.org/operazione/aggiornaOperazione.php";
    private final String URL_OPERAZIONI_BY_MESE = "https://howispend.altervista.org/operazione/fetchOperazioniByMese.php";
    private final String URL_NUOVA_OPERAZIONE = "https://howispend.altervista.org/operazione/nuovaOperazione.php";
    private final String URL_ELIMINA_OPERAZIONE = "https://howispend.altervista.org/operazione/eliminaOperazione.php";


    //ANNO
    private final String URL_NUOVO_ANNO = "https://howispend.altervista.org/anno/nuovoAnno.php";
    private final String URL_ALL_ANNI = "https://howispend.altervista.org/anno/fetchAllAnni.php";
    private final String URL_ANNO_BY_ID = "https://howispend.altervista.org/anno/fetchAnnoById.php";





    private final String REGEX_EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";


    private Constants() {}


    public static Constants getInstance() {
        if(instance==null){
            instance = new Constants();
        }
        return instance;
    }


    public String getURL_LOGIN() {
        return URL_LOGIN;
    }


    public String getURL_NUOVO_UTENTE() {
        return URL_NUOVO_UTENTE;
    }


    public String getURL_ALL_MESI() { return URL_ALL_MESI;}

    public String getURL_OPERAZIONI_BY_MESE() {
        return URL_OPERAZIONI_BY_MESE;
    }

    public String getURL_NUOVA_OPERAZIONE() {
        return URL_NUOVA_OPERAZIONE;
    }

    public String getURL_UPDATE_NOME_MESE() {
        return URL_UPDATE_NOME_MESE;
    }

    public String getURL_RIMANENZE_MESE() {
        return URL_RIMANENZE_MESE;
    }

    public String getURL_AGGIORNA_OPERAZIONE() {
        return URL_AGGIORNA_OPERAZIONE;
    }

    public String getURL_MESE_BY_ID() {
        return URL_MESE_BY_ID;
    }

    public String getURL_ELIMINA_OPERAZIONE() {
        return URL_ELIMINA_OPERAZIONE;
    }

    public String getURL_NUOVO_MESE() {
        return URL_NUOVO_MESE;
    }

    public String getURL_ELIMINA_MESE() {
        return URL_ELIMINA_MESE;
    }

    public String getURL_CHIUDI_MESE() {
        return URL_CHIUDI_MESE;
    }

    public String getURL_NUOVO_ANNO() {
        return URL_NUOVO_ANNO;
    }

    public String getURL_ALL_ANNI() {
        return URL_ALL_ANNI;
    }

    public String getURL_ANNO_BY_ID() {
        return URL_ANNO_BY_ID;
    }

    public String getURL_MESI_BY_ANNO() {
        return URL_MESI_BY_ANNO;
    }


    public Utente createUserFromJson(JSONObject jsonObject) throws ParseException, JSONException {
        int id = jsonObject.getInt("id");
        String nome = jsonObject.getString("nome");
        String cognome = jsonObject.getString("cognome");
        String email = jsonObject.getString("email");
        String password = new String(Base64.getDecoder().decode(jsonObject.getString("password")));
        LocalDate dataCreazione = LocalDate.parse(jsonObject.getString("data_creazione"));

        return new Utente(id, nome, cognome, email, password, dataCreazione);
    }

    /*public Mese createMeseFromJson(JSONObject jsonObject) throws ParseException, JSONException {

        int id = jsonObject.getInt("id");
        String mese = jsonObject.getString("mese");
        double spesaParziale = jsonObject.getDouble("spesa_parziale");
        double spesaFinale = jsonObject.getDouble("spesa_finale");
        double guadagno = jsonObject.getDouble("guadagno");
        double introiti = jsonObject.getDouble("introiti");
        boolean isAperto = jsonObject.getString("is_aperto").equals("1");
        Date dataCreazione = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(jsonObject.getString("data_creazione"));

        return new Mese(id, mese, spesaParziale, spesaFinale, guadagno, introiti, isAperto, dataCreazione);

    }*/

    /*public Operazione createOperazioneFromJson(JSONObject jsonObject, Mese mese) throws ParseException, JSONException{
        Integer id = jsonObject.getInt("id");
        String motivo = jsonObject.getString("motivo");
        Double costo = jsonObject.getDouble("costo");
        Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(jsonObject.getString("data_creazione"));

        return new Operazione(id, motivo, costo, mese, date);
    }*/

    /*public Anno createAnnoFromJson(JSONObject jsonObject) throws JSONException, ParseException {
        Integer id = jsonObject.getInt("id");
        String anno = jsonObject.getString("anno");
        Date dataCreazione = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(jsonObject.getString("data_creazione"));
        double spesaParziale = jsonObject.getDouble("spesa_parziale");
        double spesaFinale = jsonObject.getDouble("spesa_finale");
        double risparmioFinale = jsonObject.getDouble("risparmio_finale");
        boolean isAperto = jsonObject.getString("is_aperto").equals("1");

        return new Anno(id, anno, dataCreazione, spesaParziale, spesaFinale, risparmioFinale, isAperto);
    }*/


    /*public List<Mese> mesiAperti(Utente utente) {
        return utente.getMesi().stream().filter(Mese::isAperto).collect(Collectors.toList());
    }

    public List<Mese> mesiChiusi(Utente utente) {
        return utente.getMesi().stream().filter(x -> !x.isAperto()).collect(Collectors.toList());
    }

    public List<Anno> anniAperti(Utente utente) {
        return utente.getAnni().stream().filter(Anno::isAperto).collect(Collectors.toList());
    }

    public List<Anno> anniChiusi(Utente utente) {
        return utente.getAnni().stream().filter(x -> !x.isAperto()).collect(Collectors.toList());
    }*/


    public boolean matchMailRegex(String mail) {
        return mail.matches(REGEX_EMAIL);
    }

}
