# Game of Corona

Eine kleine Virus-Simulation in einem Game-of-Life Stil.

## Kurzbschreibung:

---
„Game of Corona“ ist eine Abwandlung des
Klassikers [„Game of Life“](https://de.wikipedia.org/wiki/Conways_Spiel_des_Lebens). Der User kann einzelne Zellen
„infizieren“ und benachbarte Zellen können sich anders als beim Original „Game of Life“ anhand eines Zufallswertes
anstecken. Zellen mit Maske können eine geringere Ansteckungswahrscheinlichkeit haben. Die Zellen können nach einer
Infektion sterben oder immun werden. Der User soll die Simulation beeinflussen können, indem er Zellen infiziert, impft,
Masken tragen lässt oder tötet. Darüber hinaus können die Geschwindigkeit und Wahrscheinlichkeiten wie die
Infektionswahrscheinlichkeit für eine Zelle die mit einer kranken Zelle benachbart ist, wie stark die
Infektionswahrscheinlichkeit abnimmt, wenn eine Zelle eine Maske trägt, die Tödlichkeit des Virus und Chance nach einer
Immunisierung erneut infiziert zu werden, eingestellt werden. Zusätzlich lassen sich die „ticks till event“ einstellen
hiervon wird die Dauer einer Infektion und die Dauer der Immunität beeinflusst.

## Screenshot zur Anwendung:

---

![](readmeImgs/main.png)

### Legende zu den Zellen Icons

| Zellen Icon | Zustand | Kurzbeschreibung |
| ------ | ------ | ------ |
| ![](src/main/resources/healthy.png) | gesund | Eine gesunde Zelle, sie kann infiziert, maskiert, geimpft und getötet werden. |
| ![](src/main/resources/masked.png) | maskiert | Eine maskierte Zelle, sie kann ein geringeres Infektionsrisiko haben, verhält sich ansonsten wie eine gesunde Zelle.|
| ![](src/main/resources/immune.png)  | immun | Eine immune Zelle, kann nicht Krank werden und auch keine Maske tragen, der Spieler kann sie dennoch töten. Sie wird nach einer Zeit wieder Infizierbar.|
| ![](src/main/resources/sick.png) | krank | Eine kranke Zelle steckt ihre nachbarn an und hat die möglichkeit zu sterben oder immun zu werden.|
| ![](src/main/resources/dead.png) | tot | Eine tote Zelle kann nichts mehr machen und kann nur durch ein reset wiederbelebt werden.|

## Bedienungshinweise:

---

### Erklärung für die Buttons

Mit klicken auf die Buttons können die folgenden Aktionen durchgeführt werden:

| Button | Beschreibung | Mouseicon |
| ------ | ------ | ------ |
| reset | Reset setzt alle Zellen gesund, ihre ticksTillEvent auf 0 und den Mausmodus auf default. Die Statistiken werden genullt.| ![](src/main/resources/defaultMouse.png) |
| infect | Die Maus wird in einen "Infektionsmodus" versetzt, jetzt kann durch klicken auf eine Zelle diese infiziert werden. | ![](src/main/resources/virusMouse.png) |
| mask | Die Maus wird in einen "Maskenmodus" versetzt, jetzt kann durch klicken auf eine Zelle diese maskiert werden. | ![](src/main/resources/maskMouse.png) |
| vaccinate | Die Maus wird in einen "Impfmodus" versetzt, jetzt kann durch klicken auf eine Zelle diese geimpft werden. | ![](src/main/resources/vaccinationMouse.png) |
| kill | Die Maus wird in einen "Tötungsmodus" versetzt, jetzt kann durch klicken auf eine Zelle diese getötet werden. | ![](src/main/resources/killMouse.png) |

### Erklärung für die Slider

Die Werte der Slider können wahlweise über klick auf eine Position im Slider oder beim Hovern über dem Slider durch
Benutzen des Mausrades verändert werden.

| Titel | Beschreibung | von | bis | Einheit |
| ------ | ------ | ------ | ------ | ------ |
| ms delay between updates | Die Zeit zwischen den Updates "Ticks" des Boards. | 0 | 2500 | Millisekunden |
| infection probability | Die Wahrscheinlichkeiten für jede Nachbarzelle einer infizierten Zelle beim nächsten Update infiziert zu werden. | 0 | 100 | % |
| infection reduction by mask | Wie stark eine Maske die Infektionswahrscheinlichkeit verringert. | 0 | 100 | % |
| death probability | Die Wahrscheinlichkeiten das eine Zelle nach ein Infektion stirbt. | 0 | 100 | % |
| chance of becoming infectabel| Die Wahrscheinlichkeiten das eine Zell nach einer Immunisierung erneut angesteckt werden kann. Nach ablauf der mindest Immunzeit wird jede runde erneut gewürfelt. | 0 | 100 | % |
| random tickeventrange | Die Tickrange bestimmt wie lange eine Zelle maximal Krank sein kann, der wert x5 bestimmt die maximale mindest Immunzeit der Zelle. Die "ticks till event" sind ein Versuch die Willkür des Lebens zu erfassen.| 0 | 100 | "game ticks" |

## Übersicht über die Dateien und die Lines of Code:

---

####Einzeldateien:

| Name | Paket | Teil von | Type | LOC | Anteil in % | Kurzbeschreibung|
| ------ | ------ | ------ | ------ | ------ | ------ | ------ |
| Cell | engine | AL | Klasse | 32 | ≈ 7% | Eine einzelne Zelle in der Simulation |
| CellStatus | engine | AL | Enum | 4 | ≈ 1% | Die möglichen Zustände der Zellen |
| Engine | engine | AL | Klasse| 172 | ≈ 38% | Der Kern der Anwendung |
| GameOfCorona | engine | AL | Interface | 37 | ≈ 8% | Die Schnittstelle zwischen AL und PI |
| Grid | presentation | PI | Klasse | 33 | ≈ 7% | Raster zum Ausrichten der Zellen |
| PI | presentation | PI | Klasse | 172 | ≈ 38% | Die Präsentation und Interaktion der Simulation |
| Main | - | - | Klasse | 8 | ≈ 2% | Die Klasse mit der Main-Methode |


####Projektabschnitte:

| Name | Paket | LOC | Anteil in % | Kurzbeschreibung|
| ------ | ------ | ------ | ------ | ------ |
|**engine**| **AL** | **245** | **≈ 54%** | Zusammenfassung des Paket "engine" |
|**presentation**| **PI** | **206** | **≈ 45%** | Zusammenfassung des Paket "presentation" |
|**Gesamtes Projekt** | - |**458** | - | Gesamtzeilen des Projekts = AL + PI + Main Klasse|

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

- Der Algorithmus zur Findung der Nachbarzellen wurde von der Seite 233 des Buches "Java der Grundkurs" von Michael
Kofler inspiriert, dort wurde erklärt wie man die Bewegung einer Schachfigur auf Gültigkeit prüft und wie man mit ungültigen Bewegungen umgeht.


- Aufgrund der sehr bescheidenen Dokumentation der controlP5 Bibliothek hab ich mich an den Beispieldateien orientiert die
im entsprechenden Projekt auf GitHub zur verfügung gestellt wurden:
  - für die
  Buttonbar: https://github.com/sojamo/controlp5/blob/master/examples/controllers/ControlP5ButtonBar/ControlP5ButtonBar.pde
  - für die
  Slider: https://github.com/sojamo/controlp5/blob/master/examples/controllers/ControlP5slider/ControlP5slider.pde


- In JUnit habe ich mithilfe der folgenden Videos eingearbeitet:

  - https://youtu.be/we3zJE3hlWE
  - https://youtu.be/4NDc9QZ8pls
  - https://youtu.be/jtsHJufkDfU

*Alle Links wurden zuletzt am 11.06.2021 geprüft.*

