SJAKK REKURSJON NOTATER

Hva trengs?

en boolean erDypeste : angir om dette er dypeste nivå av trekk.

imagStilling:

Mottatt i konstruktør:
 - gammelt brett  
 - trekk  
 - ref til forelder
 - antTrekkVidere : Hvor mange generasjoner ETTER dette trekket?


Metode som oppretter ny generasjon:
lagBarn()
 - if(denne er nederst):
    for trekk: muligetrekkListe:
      imagStilling i trekket = new imagStilling(....)

hentDypRating()
 - if(erDypeste){
   returnerer grunnRating.
   else{
      kall på alle barnas hentDypRating() og regn ut gjennomsnitt, returner dette.   
   }
   }



Imag stilling skal KUN kommunisere med forelder, barn og Evaluator.
