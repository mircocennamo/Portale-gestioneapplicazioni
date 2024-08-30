package it.interno.gestioneapplicazioni.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimestampSerializer extends StdSerializer<Timestamp> {


    protected TimestampSerializer() {
        super(Timestamp.class);
    }

    @Override
    public void serialize(Timestamp timestamp, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(timestamp));
    }
}
