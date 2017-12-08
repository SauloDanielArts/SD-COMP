package simuladorpd;

/**
 *
 * @author Saulo Daniel
 */
public class SimuladorPD {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //painel principal  
        MainJFrame jf = new MainJFrame();
        jf.setVisible(true);

//        //computador simulado
//        COMP comp = new COMP();
//
//        //adicioando dados de programa na memoria
//        comp.setDADO_MEMORIA(0, "INIT #4");
//        comp.setDADO_MEMORIA(1, "1");
//        comp.setDADO_MEMORIA(2, "2");
//        comp.setDADO_MEMORIA(3, "8");
//        comp.setDADO_MEMORIA(4, "PUSH 1");
//        comp.setDADO_MEMORIA(5, "ADD");
//        comp.setDADO_MEMORIA(6, "MOV A C");
//        comp.setDADO_MEMORIA(7, "EQUAL 4 #1 #4");
//        comp.setDADO_MEMORIA(8, "POP A");
//        comp.setDADO_MEMORIA(9, "END");
//
//        //ler memoria e executa comandos 
//        do {
//            comp.updateState();
//
//            if (comp.checkErro() == true) {
//                System.out.println("Falha na execuçao do comando: " + comp.getPC());
//                break;
//            }
//
//            //exibe alguns registradores        
//            System.out.println("P0: " + comp.getDADO_PILHA(0));
//            System.out.println("P1: " + comp.getDADO_PILHA(1));
//            System.out.println(" A: " + comp.getA());
//            System.out.println("\n");
//
//        } while (comp.getEND() == false);

    }

}
