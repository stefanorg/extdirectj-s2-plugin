package it.extdirectj.bean;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

/**
 * @Author: Simone Ricciardi
 */
public class ExtDirectJType {

   private JsonElement json;
   private JsonDeserializationContext context;

   public ExtDirectJType(JsonElement json, JsonDeserializationContext context) {
      this.json = json;
      this.context = context;
   }

   public <T> T parseValue(Class<T> type) {
      return (T) this.context.deserialize(this.json, type);
   }
}
