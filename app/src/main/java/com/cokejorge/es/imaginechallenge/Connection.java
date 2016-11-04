package com.cokejorge.es.imaginechallenge;

import android.os.StrictMode;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

/**
 * Created by cokelas on 4/11/16.
 */

public class Connection {

    //TODO: LANZA EL SERVIDOR E INSERTA LA URL AQUI (terminado en "/"):
    public static String url = "http://192.168.2.226:8080/";

    public static RestTemplate rest;
    public static URI uri;

    /**
     * Iniciamos conexion con el servidor
     * @param uriBase
     */
    public static void init(URI uriBase){
        //Enabling ThreadPolicy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Creamos ObjectMapper y Agnadimos Jackson para Parsear JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jackson2HalModule());
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
        converter.setObjectMapper(mapper);
        rest = new RestTemplate(Collections.<HttpMessageConverter<?>> singletonList(converter));
        uri = uriBase;
    }

    public static void postUser(Localizable u) throws RestClientException {
        uri = rest.postForLocation(url+"people",u,Localizable.class);
        Log.e("TAG","Post OK");
    }

    public static void deleteUser(){
        try{
            rest.delete(uri);
        }catch (RestClientException ex){
            Log.e("DEVELOP", "Exception al postUser: " + ex);
        }
    }

    public static void deleteBall(double lat, double lon){
        rest.delete(url+"deleteBall?lat="+lat+"&lon="+lon, Localizable[].class);
    }

    public static void updateUser(Localizable u) throws RestClientException{
        try{
            rest.put(uri,u);
        }
        catch (IllegalArgumentException ex){
            Log.e("DEVELOP", "Exception IllegalArgument al postUser: " + ex);
        }
    }

    public static Localizable[] getUsers(double lat, double lon, double radio){
        try{
            return rest.getForObject(url+"location?lat="+lat+"&lon="+lon+"&r="+radio/1000, Localizable[].class);
            //return new Localizable[0];
        }catch (RestClientException ex){
            Log.e("DEVELOP", "Exception al getUsers: " + ex);
        }
        return new Localizable[0];
    }

    public static Ball[] getBalls(double lat, double lon, double radio){
        try{
            return rest.getForObject(url+"ball?lat="+lat+"&lon="+lon+"&r="+radio/1000, Ball[].class);
            //return new Localizable[0];
        }catch (RestClientException ex){
            Log.e("DEVELOP", "Exception al getBalls: " + ex);
        }
        return new Ball[0];
    }

}
