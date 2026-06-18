package com.example.miaplicacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WSJsonUsuario extends WSJson {

    private static String IP = "10.0.2.2";
    private static String URL = "http://" + IP + "/android/";

    public static Usuarios getUsuarios() throws IOException, JSONException {
        Usuarios result = null;

        JSONObject jsonObj = getJson(URL + "get_usuarios.php");

        if (jsonObj != null) {

            JSONArray usuarios = jsonObj.getJSONArray("usuraio");
            result = new Usuarios();

            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject c = usuarios.getJSONObject(i);

                Usuario temp = new Usuario();
                temp.setId(c.getInt("id"));
                temp.setNombre(c.getString("nombre"));
                temp.setPasswd(c.getString("passwd"));

                result.add(temp);
            }
        }
        return result;
    }

    public static boolean insertUsuario(Usuario usuario) throws IOException, JSONException {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("nombre", usuario.getNombre());
        jsonParam.put("passwd", usuario.getPasswd());

        JSONObject jsonResult = sendJson(URL + "/insertar_usuario.php", jsonParam);

        if(jsonResult == null) return false;

        String estado = jsonResult.getString("estado");

        if(estado.compareTo("1")==0) return true;
        else return  false;
    }
}

