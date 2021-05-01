public class Test {
    Cell c = new Cell().setStatus(CellStatus.MASKED);
    int[] arr;
    public void test(){
        arr = new int[2];
        for(int i = 0; i < 100; i++){
            arr[c.willGetInfected(50, 25) ? 0 : 1]++;
        }
    }
    public void print(){
        test();
        for(int data : arr){
            System.out.println(data);
        }
    }
}
//new Test().print()