package it.extdirectj.core;

import com.google.gson.*;

import java.lang.reflect.Type;

import it.extdirectj.bean.ExtDirectJType;

/**
 * @Author: Simone Ricciardi
 */
public class ExtDirectJDeserializer implements JsonDeserializer<ExtDirectJType> {

   public ExtDirectJType deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
      return new ExtDirectJType(json, context);
   }
}
