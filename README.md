** Kurzbschreibung:**

Die Idee:

„Game of Corona“ ist eine Abwandlung des Klassikers „Game of Life“. Der User kann einzelne Zellen „infizieren“ und benachbarte Zellen können sich anders als beim Original „Game of Life“ anhand eines Zufallswertes anstecken. Zellen mit Maske können eine geringere Ansteckungswahrscheinlichkeit haben. Die Zellen können nach einer Infektion sterben oder immun werden. Der User soll die Simulation beeinflussen können, indem er Zellen infiziert, impft, Masken tragen lässt oder tötet. Darüber hinaus können die Geschwindigkeit und Wahrscheinlichkeiten wie die Infektionswahrscheinlichkeit für eine Zelle, die mit einer kranken Zelle benachbart ist, wie stark die Infektionswahrscheinlichkeit abnimmt, wenn eine Zelle eine Maske trägt, die Tödlichkeit des Virus und Chance nach einer Immunisierung erneut infiziert zu werden eingestellt werden. Zusätzlich lassen sich die „ticks till event“ einstellen hiervon wird die Dauer einer Infektion und die Dauer der Immunisierung beeinflusst.

![](readmeImgs/main.png)


| Zellen Icon | Zustand | Kurzbeschreibung |
| ------ | ------ | ------ |
| ![](src/main/resources/healthy.png) | gesund | eine gesunde Zelle, sie kann infiziert, maskiert, geimpft und getötet werden. |
| ![](src/main/resources/masked.png) | maskiert | eine maskierte Zelle, sie hat ein geringeres Infektionsrisko, verhält sich ansonsten wie eine gesunde Zelle.|
| ![](src/main/resources/immune.png)  | immun | eine immune Zelle, kann nicht Krank werden und auch keine Maske tragen, Spieler kann sie dennoch töten.|
| ![](src/main/resources/sick.png) | krank | eine kranke Zelle steckt ihre nachbarn an, hat die möglichkeit zu sterben oder immun zuwerden.|
| ![](src/main/resources/dead.png) | tot | eine tote Zelle kann nichts mehr machen und kann nur durch ein reset wiederbelebt werden.|
