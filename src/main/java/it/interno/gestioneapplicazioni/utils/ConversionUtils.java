package it.interno.gestioneapplicazioni.utils;

import it.interno.gestioneapplicazioni.entity.QualificaAssegnabilita;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public interface ConversionUtils {

    static LocalDate timestampToLocalDate(Timestamp timestamp){
        return timestamp.toLocalDateTime().toLocalDate();
    }

    static Timestamp localDateToTimestamp(LocalDate localDate){
        return Timestamp.valueOf(localDate.atStartOfDay());
    }

    static List<Integer> qualificheAssegnateToInteger(List<QualificaAssegnabilita> qualificheAssegnate){

        if(qualificheAssegnate == null)
            return new ArrayList<>();

        return qualificheAssegnate.stream()
                .map(QualificaAssegnabilita::getId)
                .toList();
    }

    static List<QualificaAssegnabilita> idQualificheAssegnateToEntity(List<Integer> idQualificheAssegnate){

        if(idQualificheAssegnate == null)
            return new ArrayList<>();

        return idQualificheAssegnate.stream()
                .map(QualificaAssegnabilita::new)
                .toList();
    }

    static Timestamp getCurrentTimestamp(){
        ZoneId fusoOrario = ZoneId.of("Europe/Rome");
        return Timestamp.valueOf(LocalDateTime.now(fusoOrario));
    }

    static String conversioneCaratteriSpeciali(String parola){
        parola = parola.replace('.', '-');
        parola = parola.replace(',', '-');
        parola = parola.replace('/', '-');
        parola = parola.replace('\\', '-');
        parola = parola.replace(' ', '_');
        return parola;
    }
}
