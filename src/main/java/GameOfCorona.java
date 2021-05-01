public interface GameOfCorona {
    //Cell[] getCells(); //ggf andere Datenstruktur um anzahl dynamisch zu ändern
    CellStatus getCellStatus(int pos); // Enum für die verschiedenen Zustaende
    void infectCell(int pos);
    void maskCell(int pos);
    void vaccinateCell(int pos);
    int[] getSickCells(Cell[] cells);
    void infectNeighbours(Cell[] cells, int pos);
    boolean isInfectable(Cell cell);
    double plausibilityOfInfection(Cell cell);
}
