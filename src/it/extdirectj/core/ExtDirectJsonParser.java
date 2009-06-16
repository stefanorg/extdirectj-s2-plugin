package it.extdirectj.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

import it.extdirectj.bean.ExtDirectJType;

/**
 * @Author: Simone Ricciardi
 */
public class ExtDirectJsonParser {

   private Gson gson;

   public ExtDirectJsonParser() {
      this.gson = new GsonBuilder()
            .registerTypeAdapter(ExtDirectJType.class, new ExtDirectJDeserializer())
            .setDateFormat(DateFormat.LONG)
            .create();
   }

   public String serialize(Object object) {
      return this.gson.toJson(object);
   }

   public <T> T deserialize(String json, Class<T> type) {
      return this.gson.fromJson(json, type);
   }
}
