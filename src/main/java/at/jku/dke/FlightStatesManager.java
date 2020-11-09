package at.jku.dke;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import be.ugent.rml.Executor;
import be.ugent.rml.Utils;
import be.ugent.rml.functions.FunctionLoader;
import be.ugent.rml.functions.lib.IDLabFunctions;
import be.ugent.rml.records.RecordsFactory;
import be.ugent.rml.store.QuadStore;
import be.ugent.rml.store.QuadStoreFactory;
import be.ugent.rml.store.RDF4JStore;

public class FlightStatesManager {
    private static final String REST_ENDPOINT_URL = "https://opensky-network.org/api/states/all"
            + "?lamin=46.1646&lomin=9.6459&lamax=48.9802&lomax=16.9628";
    private static final File STATES_FILE = new File ("states.json");
    private static final File FUNCTION_FILE = new File ("custom_functions.json");

    private QuadStore rmlStore;

    private void loadStates() throws IOException {
        if (STATES_FILE.exists()) {
            STATES_FILE.delete();
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             FileOutputStream outputStream = new FileOutputStream(STATES_FILE)) {
            HttpGet httpGet = new HttpGet(REST_ENDPOINT_URL);

            CloseableHttpResponse responseBody = httpClient.execute(httpGet);

            try (InputStream inputStream = responseBody.getEntity().getContent()) {
                inputStream.transferTo(outputStream);
            }
        }
    }

    public static void main(String[] args){
        System.out.println("Ausgabe aus der main()-Methode");
        FlightStatesManager fsm = new FlightStatesManager();

        try {
            fsm.loadStates();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
