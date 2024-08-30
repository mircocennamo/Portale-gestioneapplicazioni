package it.interno.gestioneapplicazioni.repository.specification;

import it.interno.gestioneapplicazioni.entity.Applicazione;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ApplicazioneSpecification {

    private ApplicazioneSpecification() {
    }

    public static Specification<Applicazione> appIdLike(String parametro) {
        return (root, query, cb) ->  cb.like(
                cb.upper(root.get("appId")), "%" + parametro.toUpperCase() + "%"
        );
    }

    public static Specification<Applicazione> appNameLike(String parametro) {
        return (root, query, cb) ->  cb.like(
                cb.upper(root.get("appName")), "%" + parametro.toUpperCase() + "%"
        );
    }

    public static Specification<Applicazione> appDescriptionLike(String parametro) {
        return (root, query, cb) ->  cb.like(
                cb.upper(root.get("appDescription")), "%" + parametro.toUpperCase() + "%"
        );
    }

    public static Specification<Applicazione> appScopeLike(String parametro) {
        return (root, query, cb) ->  cb.like(
                cb.upper(root.get("appScope")), "%" + parametro.toUpperCase() + "%"
        );
    }

    public static Specification<Applicazione> dataInizioValiditaLessThan(LocalDate dataRif) {
        return (root, query, cb) ->  cb.lessThanOrEqualTo(
                root.get("appDataIni"), dataRif
        );
    }

    public static Specification<Applicazione> dataFineValiditaGreaterThanOrNull(LocalDate dataRif) {
        return (root, query, cb) -> cb.or(
                cb.isNull(root.get("appDataFin")),
                cb.greaterThanOrEqualTo(root.get("appDataFin"), dataRif)
        );
    }

    public static Specification<Applicazione> dataCancellazioneIsNull(){
        return (root, query, cb) -> cb.isNull(root.get("dataCancellazione"));
    }

    public static Specification<Applicazione> ambitoIs(Integer ambito){
        return (root, query, cb) ->  cb.equal(
                root.get("idOrdineAmbito"), ambito
        );
    }

}
