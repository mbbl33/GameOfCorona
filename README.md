# Game of Corona

Eine kleine Virus-Simulation im Game-of-Life-Stil.

## Kurzbschreibung:

---
„Game of Corona“ ist eine Abwandlung des
Klassikers [„Game of Life“](https://de.wikipedia.org/wiki/Conways_Spiel_des_Lebens). Der User kann einzelne Zellen
„infizieren“ und benachbarte Zellen können sich anders als beim Original „Game of Life“ anhand eines Zufallswertes
anstecken. Zellen mit Maske können eine geringere Ansteckungswahrscheinlichkeit haben. Die Zellen können nach einer
Infektion sterben oder immun werden. Der User soll die Simulation beeinflussen können, indem er Zellen infiziert, impft,
Masken tragen lässt oder tötet. Darüber hinaus kann über verschiedene Parameter festgelegt werden wie die Geschwindigkeit der Simulation, wie hoch
Infektionswahrscheinlichkeit von Zellen mit erkranktem Nachbarn ist, wie effektiv eine getragene
Maske vor einer Erkrankung schützt, wie tödlich diese ist und mit welcher Wahrscheinlichkeit es zu einer erneuten
Infektion trotz eigentlicher Immunisierung kommen kann. Zusätzlich kann man die „ticks till event“ konfigurieren, von
welchen die Dauer einer Infektion und Immunität abhängen.

*Anmerkung: Ursprünglich war ein Feature geplant, welches die Anzahl der Zellen dynamisch skalierbar gemacht hätte.
Einige der Datenstrukturen und Umsetzungen berücksichtigen dies auch bereits, um eine eventuelle Umsetzung in der
Zukunft zu vereinfachen. Damit die Anzahl der Zellen nicht 480 übersteigt, wurde dies jedoch noch nicht implementiert.*


## Screenshot zur Anwendung:

---

### Legende zu den Zellen-Icons

| Zellen Icon | Zustand | Kurzbeschreibung |
| ------ | ------ | ------ |
| ![](src/main/resources/healthy.png) | gesund | Eine gesunde Zelle; sie kann infiziert, maskiert, geimpft oder getötet werden. |
| ![](src/main/resources/masked.png) | maskiert | Eine maskierte Zelle; sie kann ein geringeres Infektionsrisiko haben, verhält sich ansonsten aber wie eine gesunde Zelle.|
| ![](src/main/resources/immune.png)  | immun | Eine immune Zelle; sie kann nicht mehr erkranken und auch keine Maske tragen, der Spieler kann sie aber dennoch töten. Ihre Immunität verschwindet nach einer gewissen Zeit.|
| ![](src/main/resources/sick.png) | krank | Eine kranke Zelle steckt ihre Nachbarn an und kann entweder an der Infektion sterben oder anschließend eine Immunität entwickeln.|
| ![](src/main/resources/dead.png) | tot | Eine tote Zelle kann nichts mehr machen und nur durch einen kompletten Reset wiederbelebt werden.|

## Bedienungshinweise:

---

### Erklärung der Buttons

Durch einen Klick auf die entsprechenden Button können die folgenden Aktionen durchgeführt werden:

| Button | Beschreibung | Mouseicon |
| ------ | ------ | ------ |
| reset | Setzt das Spiel und den Mausmodus zurück. Die Statistiken werden genullt.| ![](src/main/resources/defaultMouse.png) |
| infect | Die Maus wird in einen "Infektionsmodus" versetzt. Ein Klick auf eine Zelle infiziert diese. | ![](src/main/resources/virusMouse.png) |
| mask | Die Maus wird in einen "Maskenmodus" versetzt. Ein Klick auf eine Zelle stattet diese mit einer Maske aus. | ![](src/main/resources/maskMouse.png) |
| vaccinate | Die Maus wird in einen "Impfmodus" versetzt. Ein Klick auf eine Zelle impft diese. | ![](src/main/resources/vaccinationMouse.png) |
| kill | Die Maus wird in einen "Tötungsmodus" versetzt. Ein Klick auf eine Zelle tötet diese. | ![](src/main/resources/killMouse.png) |

### Erklärung der Slider
Der Wert eines Sliders kann entweder durch Klicken einer Position innerhalb des selbigen oder durch Hovern und Verwendung
des Mausrades verändert werden.

| Titel | Beschreibung | von | bis | Einheit |
| ------ | ------ | ------ | ------ | ------ |
| ms delay between updates | Die Zeit, die zwischen Update-Ticks vergeht. | 0 | 2500 | Millisekunden |
| infection probability | Die Wahrscheinlichkeit, mit welcher sich eine Zelle bei einem infizierten Nachbarn im nächsten Update ansteckt. | 0 | 100 | % |
| infection reduction by mask | Wie effektiv eine Maske vor einer Infektion schützt. | 0 | 100 | % |
| death probability | Die Wahrscheinlichkeit, mit welcher eine Zelle an einer Infektion stirbt. | 0 | 100 | % |
| chance of becoming infectabel| Die Wahrscheinlichkeit, mit welcher sich eine Zelle trotz eigentlicher Immunisierung infizieren kann. Nach Ablauf der garantierten Immunitätsdauer wird jede Runde gewürfelt. | 0 | 100 | % |
| random tickeventrange | Die Tickrange bestimmt, wie lange eine Zelle höchstens krank sein kann. Der Wert multipliziert mit fünf bestimmt die garantierte Immunitätsdauer einer Zelle. Die "ticks till event" sind ein Versuch, die Willkür des Lebens zu erfassen.| 0 | 100 | "game ticks" |

## Übersicht über die Dateien und die Lines of Code:

---

#### Einzeldateien:

| Name | Paket | Teil von | Type | LOC | Anteil in % | Kurzbeschreibung|
| ------ | ------ | ------ | ------ | ------ | ------ | ------ |
| Cell | engine | AL | Klasse | 32 | ≈ 7% | Eine einzelne Zelle der Simulation. |
| CellStatus | engine | AL | Enum | 4 | ≈ 1% | Die möglichen Zustände, die eine Zelle annehmen kann. |
| Engine | engine | AL | Klasse| 172 | ≈ 38% | Der Kern der Anwendung. |
| GameOfCorona | engine | AL | Interface | 37 | ≈ 8% | Die Schnittstelle zwischen AL und PI. |
| Grid | presentation | PI | Klasse | 33 | ≈ 7% | Raster zur Ausrichtung der Zellen. |
| PI | presentation | PI | Klasse | 172 | ≈ 38% | Die Präsentation und Interaktion der Simulation. |
| Main | - | - | Klasse | 8 | ≈ 2% | Die Klasse mit der Main-Methode. |

#### Projektabschnitte:

| Name | Paket | LOC | Anteil in % | Kurzbeschreibung|
| ------ | ------ | ------ | ------ | ------ |
|**engine**| **AL** | **245** | **≈ 54%** | Zusammenfassung des Pakets "engine". |
|**presentation**| **PI** | **206** | **≈ 45%** | Zusammenfassung des Pakets "presentation". |
|**Gesamtes Projekt** | - |**458** | - | Gesamtzeilen des Projekts = AL + PI + Main-Klasse |

## Quellen:

---

#### Genutzte Nachschlagewerke:

- Digital:
  - https://javabeginners.de/index.php
  - https://docs.oracle.com/javase/8/docs/api/
  - die im PiS Moodle Kurs zur Verfügung gestellten Dokumente
- Analog:
  - Kofler M.(2019), "Java der Grundkurs", 3. Auflage, Rheinwerke-Verlag
  - Niemann A. (2007), "Objektorientierte Programmierung in Java", 5. Auflage, bhv

#### Zusätzlich genutzte Bibliothek für Buttons und Slider "controlP5":

- Website: http://www.sojamo.de/libraries/controlP5/
- auf GitHub: https://github.com/sojamo/controlp5

#### Sonstiges:

- Der Algorithmus zum Finden benachbarter Zellen wurde von Seite 233 des Buches "Java der Grundkurs" von Michael
  Kofler inspiriert. Dort erklärt dieser, wie man die Bewegung einer Schachfigur auf Gültigkeit prüfen und mit
  einer ungültigen Bewegungen umgehen kann.


- Aufgrund der sehr bescheidenen Dokumentation der controlP5-Bibliothek, habe ich mich an den Beispieldateien orientiert,
  die im entsprechenden Projekt auf GitHub zur verfügung gestellt werden:
  - für die
    Buttonbar: https://github.com/sojamo/controlp5/blob/master/examples/controllers/ControlP5ButtonBar/ControlP5ButtonBar.pde
  - für die
    Slider: https://github.com/sojamo/controlp5/blob/master/examples/controllers/ControlP5slider/ControlP5slider.pde


- In JUnit habe ich mich unter Verwendung der folgenden Videos eingearbeitet:

  - https://youtu.be/we3zJE3hlWE
  - https://youtu.be/4NDc9QZ8pls
  - https://youtu.be/jtsHJufkDfU

*Alle Links wurden zuletzt am 11.06.2021 geprüft.*

