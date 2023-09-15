package trabalho1_POO;

public class Principal {
    public static void main (String args[]) {
        GPS g1 = new GPS();
        Robot40 r1 = new Robot40(1, g1);
        r1.alternating();
        
        while(true) {
            Move m1 = r1.MOVE();
            if(m1 == Move.STOP)
               break;
            g1.move(m1);
        }
        r1.print();
        System.out.print("\nFim\n");
    }

}