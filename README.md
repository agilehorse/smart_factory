# OMOSEM_smart_factory

## Semestrální projekt: Simulace chytré továrny na roboty

### Vytvořili: Bulko, Šourková

Naším cílem bylo vytvořit funkční simulaci superinteligentní továrny na 
superinteligentní roboty; chytrý dům pro nás nebyla žádná výzva. Narozdíl 
od Rossum's Universal Robots mistra Čapka jsme si pro velkovýrobu vybrali
dva konkrétní typy robotů a to galaxií milovaného R2D2 (alias Artoo-Deetoo)
a pozemšťany obávaného Terminátora (alias Arnolda*).

V naší práci jsme využili několika návrhových vzorů a to:
* Singleton
* Observer + Observable
* Visitor + Visitable
* Factory
* State machine

Za inovativní a lidstvu prospěšné na naší práci považujeme zástupy 
R2D2 a Terminátorů, které naše továrna vyprodukuje a kterým zaručujeme 
širokou škálu uplatnění od záchrany po zničení světa. Neméně skvělé
jsou naše výpisy do konzole díky kterým má osoba zodpovědná za chod továrny
přehled nad aktuálním děním v továrně. Sofistikovaný algoritmus výběru 
rozbitého zařízení, které bude jako první opraveno stojí za zmínku stejně 
jako možnost velmi snadno měnit konfiguraci továrny pomocí souboru 
ve formátu JSON. Nakonec ještě přidaný prvek náhody do naší 
jinak deterministické simulace - nemůžeme si být dopředu jistí, ve který 
moment se zařízení v naší továrně rozbije - i přes stejnou životnost se 
dvě zařízení mohou rozbít v jiný okamžik.

*neplést s pozemšťany vysmívaným Arnoldem Rimmerem