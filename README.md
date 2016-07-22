# Szóláncjáték
Osztott Rendszerek beadandó (Java)
#
Két játékos felváltva szavakat küldenek egymásnak úgy, hogy a kapott szó utolsó betűjével kezdődő szót kell válaszul küldeni. Amelyik játékos nem tud ilyen szót, az vesztett. Vannak tiltott szavak is, ha a játékos ilyen szót akarna küldeni, akkor a rendszer megkéri hogy próbálkozzon újra. Az elküldött szavak is bekerülnek a tiltott szavak közé.

##GepiKliens
Két parancssori paramétert vár. Az első a jétékos neve, a második a szókincs, amivel a gepi játékos rendelkezik.A szókincs egy szöveges fájl neve, amelyben az ismert szavak vannak felsorolva, minden sorban egy szó. A gépi kliens  ezekből szavakból mindíg az első olyan szót küldi el, ami megfelel a szabályoknak (jó kezdőbetű és nem tiltott szó).  

##InteraktivKliens
Először bekéri a játékos nevét, majd ha még nincs ellenfél, akkor addig vár, ameddig más nem csatlakozik. A játékos vagy a start szót kapja (tetszőleges kezdőbetűjű szót küldhet), vagy az ellenfél első szavát.

##SzolancJatek
Egy gépi és egy interaktív klienst indít, így lehetővé teszi hogy a felhasználó a gép ellen játszhasson. A gépi kliens szókincsét a "szokincs1.txt" fájl tartalmazza.

##SzolancSzimulacio
Négy gépi játékost indít, a Server biztosítja az ellenfelek egymáshoz rendelését.

#
#
#
Az InteraktivKliens illetve a SzolancJatek automatikusan elindít egy TiltottDeployt és egy Servert. GepiKliens vagy InteraktivKliens használata esetén biztosítani kell, hogy először elindítjuk a TiltottDeploy-t, majd a Servert. A TiltottDeploy egy parancssori paramétert vár, egy számot, ami megadja hogy mennyi tiltott szóhalmazból választhat, ha ennél egyszerre több pár játszik, akkor előről kezdi a kiosztást (6 játékos és 2 tiltott szóhalmaz esetén az első páros a "tiltott1.txt"-t használja, a második páros a "tiltott2.txt"-t, a harmadik páros ismét a "tiltott1.txt"-t).
#
#
Ha fél percen keresztül egy játékos sem csatlakozik, és nincs folyamatban játék, akkor a Server leáll. A TiltottDeploy viszont nem áll le magától, azt nekünk kell leállítani minden esetben.
