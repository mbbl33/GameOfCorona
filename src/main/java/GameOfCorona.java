import java.util.ArrayList;

public interface GameOfCorona {
    //das board auf dem simulation stattfindet
    ArrayList<Cell> getBoard();

    //die verschiedenen Aktionen die der User mit den Zellen vornehmen kann
    GameOfCorona infectCell(int pos);
    GameOfCorona maskCell(int pos);
    GameOfCorona vaccinateCell(int pos);

    //methoden die bestimmung infizierbarer nachbarn
    Cell getCellNeighbour(int pos, Direction direction);
    ArrayList<Integer> getInfectabelNeighbours(int pos) ;
    boolean isInfectable(Cell cell);

    //prozentuale infektionswahrscheinlichkeit abrufen und setzen
    int getProbaOfInfection();
    void setProbaOfInfection(int probaOfInfection);

    //prozentuale infektionswahrscheinlichkeitsmodifikation durch maske abrufen und setzen
    int getMaskModifikator();
    void setMaskModifikator(int maskModifikator);

    //prozentuale todeswahrscheinlichkeit abrufen und setzen
    int getProbaOfDead();
    void setProbaOfDead(int probaOfDead);

    //errechnet anhand eines uebergebenen prozentwert ob ein ereigins eintritt
    boolean willEventHappen(float probability);
    //errechnet anhand eines uebergebenen prozentwert ob ein ereigins eintritt wird um modifikator reduziert
    boolean willEventHappen(float probability, float modifikator);

    //ein durchlauf der simulation
    void update();

    //zuruecksetzen der simulation (alle zellen sind gesund und infizierbar)
    void reset();

}
