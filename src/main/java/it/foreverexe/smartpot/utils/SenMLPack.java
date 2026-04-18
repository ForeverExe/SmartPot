package it.foreverexe.smartpot.utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class SenMLPack extends ArrayList<SenMLRecord> {

    //Returns a Json-ed format of the SenMLRecord List:
    public static String JsonOutput(SenMLPack pack){
        return new Gson().toJson(pack);
    }
}

