package it.interno.gestioneapplicazioni.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends StdDeserializer<LocalDate> {

    private static final long serialVersionUID = -4188060148610032532L;

    @Autowired
    private transient MessageSource messageSource;

    protected LocalDateDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(jsonParser.readValueAs(String.class), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }catch (Exception exception) {
            throw new IOException("Formato campo non valido");
        }

        return localDate;
    }


}
