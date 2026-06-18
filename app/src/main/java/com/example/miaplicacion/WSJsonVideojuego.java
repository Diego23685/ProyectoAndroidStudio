package com.example.miaplicacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WSJsonVideojuego extends WSJson{
    private static String IP = "10.0.2.2";
    private static String URL = "http://" + IP + "/android/";

    public static Videojuegos getVideojuegos(int userId) throws IOException, JSONException {
        Videojuegos result = null;

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("user_id", userId);

        JSONObject jsonObj = sendJson(URL + "leer_videojuegos.php", jsonParam);

        if (jsonObj != null) {

            JSONArray clientes = jsonObj.getJSONArray("videojuegos");
            result = new Videojuegos();

            for (int i = 0; i < clientes.length(); i++) {
                JSONObject c = clientes.getJSONObject(i);

                Videojuego temp = new Videojuego();
                temp.setId(c.getInt("id"));
                temp.setTitulo(c.getString("titulo"));
                temp.setPlataforma(c.getString("plataforma"));
                temp.setGenero(c.getString("genero"));
                temp.setAnio(c.getInt("anio"));
                temp.setPrecio(c.getDouble("precio"));

                result.add(temp);
            }
        }
        return result;
    }

    public static boolean insertVideojuego(Videojuego videojuego, int userId) throws IOException, JSONException {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("user_id", userId);
        jsonParam.put("titulo", videojuego.getTitulo());
        jsonParam.put("plataforma", videojuego.getPlataforma());
        jsonParam.put("genero", videojuego.getGenero());
        jsonParam.put("anio", videojuego.getAnio());
        jsonParam.put("precio", videojuego.getPrecio());

        JSONObject jsonResult = sendJson(URL + "/crear_videojuego.php", jsonParam);

        if(jsonResult == null) return false;

        String estado = jsonResult.getString("estado");

        if(estado.compareTo("1")==0) return true;
        else return  false;
    }

    public static boolean editarVideojuego(Videojuego videojuego, int userId) throws IOException, JSONException {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id", videojuego.getId());
        jsonParam.put("user_id", userId);
        jsonParam.put("titulo", videojuego.getTitulo());
        jsonParam.put("plataforma", videojuego.getPlataforma());
        jsonParam.put("genero", videojuego.getGenero());
        jsonParam.put("anio", videojuego.getAnio());
        jsonParam.put("precio", videojuego.getPrecio());

        JSONObject jsonResult = sendJson(URL + "/editar_videojuego.php", jsonParam);

        if(jsonResult == null) return false;

        String estado = jsonResult.getString("estado");

        if(estado.compareTo("1")==0) return true;
        else return  false;
    }

    public static boolean eliminarVideojuego(Videojuego videojuego, int userId) throws IOException, JSONException {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id", videojuego.getId());

        JSONObject jsonResult = sendJson(URL + "/eliminar_videojuego.php", jsonParam);

        if(jsonResult == null) return false;

        String estado = jsonResult.getString("estado");

        if(estado.compareTo("1")==0) return true;
        else return  false;
    }
}
