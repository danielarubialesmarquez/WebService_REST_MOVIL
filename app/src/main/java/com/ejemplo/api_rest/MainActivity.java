package com.ejemplo.api_rest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//Importamos las librerias para mostrar Json en ListView

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ListView lv_clientes_list; //Se crea variable de tipo lista para tener acceso a ListView del Layout
    private ArrayAdapter adapter;  //Areglo que guarda los registros que se mostraran en el ListView

    /* Cadena de texto que contiene la direccion URL a la que se realizan las conexiones con la api y
       hash de la aplicacion web */
    private String getAllClientesURL = "http://192.168.1.69:8080/api_clientes?user_hash=12345&action=get";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Strict = Establece conexion externa y requiere permiso de mantenerla abierta */
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        initComponents(); //Se agrega el metodo de variables

        webServiceRest(getAllClientesURL); // Metodo que muestra todos los registros que contiene la tabla
    }

    /**
     * Inicializa las variables del inicio mediante el id de los componentes del Layout
     */
    private void initComponents(){
        lv_clientes_list = (ListView)findViewById(R.id.lv_clientes_list);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        lv_clientes_list.setAdapter(adapter);
    }

    /**
     *Para realizar la petición de conexión y el almacenamiento de la información
     *que regrese el webservice, los datos recibidos se almacenan en forma de lineas, por lo que
     * posteriormente se requiere un parseo para separar cada uno de los campos
     * @param requestURL
     */
    private void webServiceRest(String requestURL){
        try{
            URL url = new URL(requestURL);

            /*Variable que crea la conexion*/
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            /* Se utiliza InputStreamReader y la conexion creada para almacenar los datos que regresa el webservice */
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line=""; //Variable que guarda los datos que se guardan del webservice
            String webServiceResult=""; //almacena las lineas de datos

            /*Ciclo que cuenta las lineas de la BD y las guarda en webserviceResult*/
            while((line=bufferedReader.readLine())!=null){
                webServiceResult += line;
            }

            bufferedReader.close();

            parseInformation(webServiceResult);


        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Metodo que recibe una cadena String y la almacena en un array de tipo JSONArray, lo que
     * permite separar los campos y valores de la cadena.
     * @param jsonResult
     */
    private void parseInformation(String jsonResult){
        JSONArray jsonArray = null; //variable de tipo Json Array

        //Variables que guardan el valor de los atributos de la BD
        String id_clientes;
        String nombre;
        String ape_pat;
        String ape_mat;
        String telefono;
        String email;

        try{
            jsonArray = new JSONArray(jsonResult);
        }catch (JSONException ex){
            ex.printStackTrace();
        }

        //Ciclo que cuenta los valores de la BD mediante JSON y lo guarda el las variables ya declaradas
        for(int i=0; i<jsonArray.length();i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                id_clientes = jsonObject.getString("id_clientes");
                nombre = jsonObject.getString("nombre");
                ape_pat = jsonObject.getString("ape_pat");
                ape_mat = jsonObject.getString("ape_mat");
                telefono = jsonObject.getString("telefono");
                email = jsonObject.getString("email");

                /*Une todos las variables con sus valores reales de la BD*/
                adapter.add(id_clientes+"._ " + nombre + " " + ape_pat + " " + ape_mat+ "\n \t\t\t" + telefono + "\n \t\t\t" + email + "\n");

            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }


    }
}
