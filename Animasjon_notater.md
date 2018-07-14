ANIMASJON NOTATER

Framdrift styres av manuelle trekk OG evt animasjon -OnFinished.


BEGGE MANUELLE:

Manuelt trekk
  1) godkjennes,
  2) registreres,
  3)Animeres. Disable input under animasjon.

Manuelt - Auto
  Manuelt:
    1) godkjennes,
    2) registreres,
    3) Utslag/rokade/bondeForvandling Animeres. Disable input under animasjon.
  Auto:
    Når manuelt trekk gjennomført ELLER dersom animasjon: Animasjon onFinished:
      1) Stilling: auto-trekk
      2) Disable input, Animer trekk. Avhengig av trekk-type:
        OnFinished(
          Hvis enkelt trekk uten utslag: enable input.
          Hvis utslag: Animer utslag, enable input
          Hvis rokade: Animer tårnflytt, enable input
          Hvis bondeForvandling: animer dette, enable input.
          )
Fra auto til auto: Siste Animasjon kaller på autoTrekk!!


Variabel nesteHandling:
  0) neste spiller
  1) Utslag
  2) lang rokade
  3) kort rokade
  4) bondeforvandling
  5) bondeforvandling med utslag motstander


  PR 13.JULI: IGANG, MEN FLYTT ANSVAR OVER I ON FINISHED!!
