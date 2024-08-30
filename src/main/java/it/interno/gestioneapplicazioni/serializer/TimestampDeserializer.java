package it.interno.gestioneapplicazioni.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimestampDeserializer extends StdDeserializer<Timestamp> {

    @Autowired
    private transient MessageSource messageSource;

    public TimestampDeserializer() {
        super(Timestamp.class);
    }

    @Override
    public Timestamp deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Timestamp timestamp;
        try {
            String valoreStringa = jsonParser.readValueAs(String.class);
            long time = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").parse(valoreStringa).getTime();
            timestamp = new Timestamp(time);
        } catch (Exception exception) {
            throw new IOException("Formato campo non valido");
        }

        return timestamp;
    }
}
