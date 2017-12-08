
package simuladorpd;

/**
 *
 * @author Saulo Daniel
 */
public final class COMP {

    //vetor da pilha: P0,P1
    private final float[] PILHA = new float[2]; //

    //registradores gerais: A,B,C
    private float A, B, C;

    private int PC;// Contador de Programa (Program Counter)
    private String IR;// Registrador de Instrução (Instruction Register)
    private int MAR;// Registrador de Endereço (Memory Address Register)
    private String MBR;// Registrador de Dados ( Memory Buffer Register)

    private final String[] MEMORIA;

    private boolean END = false;
    private boolean ERRO = false;

    COMP() {

        this.MEMORIA = new String[100];
        clear();

    }

    COMP(int MEMORIA_SIZE) {

        this.MEMORIA = new String[MEMORIA_SIZE];
        clear();

    }

    public boolean checkErro() {
        return this.ERRO;
    }

    public int MEMORIA_SIZE() {
        return this.MEMORIA.length;
    }

    public boolean getEND() {
        return this.END;
    }

    public void setEND(boolean end) {
        this.END = end;
    }

    public float getA() {
        return this.A;
    }

    public void setA(float a) {
        this.A = a;
    }

    public float getB() {
        return this.B;
    }

    public void setB(float b) {
        this.B = b;
    }

    public float getC() {
        return this.C;
    }

    public void setC(float c) {
        this.C = c;
    }

    public int getPC() {
        return this.PC;
    }

    public void setPC(int pc) {
        this.PC = pc;
    }

    public void updatePC() {
        this.PC++;
    }

    public void resetPC() {
        this.PC = 0;
    }

    public int getMAR() {
        return this.MAR;
    }

    public void setMAR(int mar) {
        this.MAR = mar;
    }

    public String getIR() {
        return this.IR;
    }

    public void setIR(String IR) {
        this.IR = IR;
    }

    public String getMBR() {
        return this.MBR;
    }

    public void setMBR(String mbr) {
        this.MBR = mbr;
    }

    public void clear() {

        for (int i = 0; i < this.MEMORIA.length; i++) {
            this.MEMORIA[i] = "";
        }

        this.PILHA[0] = 0;
        this.PILHA[1] = 0;

        this.PC = 0;
        this.IR = "";
        this.MAR = 0;
        this.MBR = "";
        this.A = 0;
        this.B = 0;
        this.C = 0;
        this.END = false;
    }

    public void setDADO_MEMORIA(int index, String dado) {
        MEMORIA[index] = dado;
    }

    public String getDADO_MEMORIA(int index) {
        return MEMORIA[index];
    }

    public void setDADO_PILHA(int index, float dado) {
        PILHA[index] = dado;
    }

    public float getDADO_PILHA(int index) {
        return PILHA[index];
    }

    //PUSH: ADICIONAR DADO NA PILHA
    public void PUSH_PILHA(float dado) {
        PILHA[1] = PILHA[0];
        PILHA[0] = dado;
    }

    //POP: REMOVER DADO DA PILHA PARA USO
    public float POP_PILHA() {
        return PILHA[0];
    }

//==============================================================================
//Tipos de instruções:
//
//Caracteres espiral:
//# : sinaliza o acesso a um endereço de memória. 
//Exemplo: 
//#1 -> endereço 1 da memória.
//
//Marcadores de programa:
//INIT : marca o início do programa da posição de memória  onde os comandos estão armazenados. (deve ser sempre utilizado apenas uma vez na posição zero da memória).
//Exemplo: 
//INIT #1 -> inicia a execução do código no endereço 1 da memória.
//END : finaliza a execução do programa.
//
//Movimentação de dados:
//PUSH : adicionar dado na pilha.
//Exemplos: 
//PUSH #1 -> Desloca dado de P0 para P1, registrador P0 recebe o valor contido na posição de memória 1.
//PUSH 12 -> Desloca dado de P0 para P1, registrador P0 recebe o valor 12.
//
//POP : copia dado da pilha para um registrador ou região de memória.
//		Exemplos: 
//			POP #1 -> posição de memória 1 recebe o valor contido em P0.
//			POP A -> registrador A recebe o valor contido em P0.
//ZERO : adiciona zero em um registrador ou região de memória.
//		Exemplos: 
//			ZERO #1 -> posição de memória 1 recebe zero.
//			ZERO A -> registrador A recebe o valor contido em P0.
//MOV : move dado de um registrador ou região de memória para outro registrador ou região e adiciona zero a posição original.
//Exemplos: 
//MOV #1 #2 -> move o conteúdo da posição e memória 1 para a posição e memória 2. Substitui o conteúdo da posição de memória 1 para zero.
//MOV A #1 -> move o conteúdo do registrador A para a posição e memória 2. Substitui o conteúdo do registrador A para zero.
//CP : copia um dado para um registrador ou um local da memória.
//Exemplos: 
//CP #1 #2 -> move o conteúdo da posição e memória 1 para a posição e memória 2. 
//CP A #1 -> move o conteúdo do registrador A para a posição e memória 2.
//
//Salto:
//EQUAL : se igual salte para alguma região de memória.
//		Exemplos: 
//EQUAL A 3 #1 -> se A é igual a 3 pula para a posição de memória 1. 
//NEQUAL : se igual salte para alguma região de memória.
//NEQUAL A 3 #1 -> se A é diferente de 3 pula para a posição de memória 1. 
//
//JUMP : pula para uma posição de memória.
//		Exemplos: 
//JUMP #1 -> pula para a posição de memória 1.
//
//Aritmética (atuam exclusivamente nos valores P0 e P1 da pilha):
//ADD :  soma ( P0 = P1 + P0 ).
//SUB :  subtração ( P0 = P1 - P0 ).
//MULT : multiplicação ( P0 = P1 * P0 ).
//DIV : divisão  ( P0 = P1 / P0 ).
//EXP : exponencial  ( P0 = exp(P0) ) .
//MOD : resto de divisão ( P0 = P1 % P0 ).
//
//==============================================================================
    
    //interpretador dos comandos
    public boolean interprerador(String comando) {
        try {

            String[] array = comando.trim().split(" ");

            if (this.MAR == 0) {
                switch (array.length) {
                    case 2:

                        //- INIT
                        if (array[0].compareToIgnoreCase("INIT") == 0) {
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true) {
                                int index = Integer.parseInt(array[1].substring(1));

                                this.PC = index;
                                this.END = false;
                                this.ERRO = false;

                            }
                        } else {
                            return true;
                        }

                        break;

                    default:
                        return true;
                }
            } else {
                switch (array.length) {

                    case 1:

                        //- ADD
                        if (array[0].compareToIgnoreCase("ADD") == 0) {
                            PUSH_PILHA(PILHA[1] + PILHA[0]);
                        } else //- SUB
                        if (array[0].compareToIgnoreCase("SUB") == 0) {
                            PUSH_PILHA(PILHA[1] - PILHA[0]);
                        } else //- MULT
                        if (array[0].compareToIgnoreCase("MULT") == 0) {
                            PUSH_PILHA(PILHA[1] * PILHA[0]);
                        } else //- DIV
                        if (array[0].compareToIgnoreCase("DIV") == 0) {
                            PUSH_PILHA(PILHA[1] / PILHA[0]);
                        } else //- EXP
                        if (array[0].compareToIgnoreCase("EXP") == 0) {
                            PUSH_PILHA((float) Math.exp(PILHA[0]));
                        } else //- MOD
                        if (array[0].compareToIgnoreCase("MOD") == 0) {
                            PUSH_PILHA(PILHA[1] % PILHA[0]);
                        } else //- END
                        if (array[0].compareToIgnoreCase("END") == 0) {
                            this.END = true;
                        } else {
                            return true;
                        }
                    
                    break;

                    case 2:

                        //- PUSH
                        if (array[0].compareToIgnoreCase("PUSH") == 0) {
                            if (array[1].compareToIgnoreCase("A") == 0) {
                                PUSH_PILHA(this.A);
                            } else if (array[1].compareToIgnoreCase("B") == 0) {
                                PUSH_PILHA(this.B);
                            } else if (array[1].compareToIgnoreCase("C") == 0) {
                                PUSH_PILHA(this.C);
                            } else if (array[1].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                PUSH_PILHA(Float.parseFloat(array[1]));
                            } else if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true) {
                                int index = Integer.parseInt(array[1].substring(1));

                                PUSH_PILHA(Float.parseFloat(this.MEMORIA[index]));

                            } else {
                                return true;
                            }

                        } else //- POP
                        if (array[0].compareToIgnoreCase("POP") == 0) {

                            if (array[1].compareToIgnoreCase("A") == 0) {
                                this.A = POP_PILHA();
                            } else if (array[1].compareToIgnoreCase("B") == 0) {
                                this.B = POP_PILHA();
                            } else if (array[1].compareToIgnoreCase("C") == 0) {
                                this.C = POP_PILHA();
                            } else if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true) {
                                int index = Integer.parseInt(array[1].substring(1));

                                this.MEMORIA[index] = "" + POP_PILHA();
                            } else {
                                return true;
                            }

                        } else //- ZERO
                        if (array[0].compareToIgnoreCase("ZERO") == 0) {

                            if (array[1].compareToIgnoreCase("A") == 0) {
                                this.A = 0;
                            } else if (array[1].compareToIgnoreCase("B") == 0) {
                                this.B = 0;
                            } else if (array[1].compareToIgnoreCase("C") == 0) {
                                this.C = 0;
                            } else if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true) {
                                int index = Integer.parseInt(array[1].substring(1));

                                this.MEMORIA[index] = "0";
                            } else {
                                return true;
                            }

                        } else //- JUMP
                        if (array[0].compareToIgnoreCase("JUMP") == 0) {
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true) {
                                int index = Integer.parseInt(array[1].substring(1));
                                this.PC = index;
                            } else {
                                return true;
                            }

                        } else {
                            return true;
                        }

                    break;
                    
                    case 3:

                        //- MOV //- CP
                        if (array[0].compareToIgnoreCase("MOV") == 0 || array[0].compareToIgnoreCase("CP") == 0) {
                            // A -> B
                            if (array[1].compareToIgnoreCase("A") == 0 && array[2].compareToIgnoreCase("B") == 0) {
                                this.B = this.A;

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.A = 0;
                                }
                            } else // A -> C   
                            if (array[1].compareToIgnoreCase("A") == 0 && array[2].compareToIgnoreCase("C") == 0) {
                                this.C = this.A;

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.A = 0;
                                }
                            } else // A -> #M 
                            if (array[1].compareToIgnoreCase("A") == 0 && array[2].charAt(0) == '#') {
                                int index = Integer.parseInt(array[2].substring(1));

                                this.MEMORIA[index] = "" + this.A;

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.A = 0;
                                }

                            } else // B -> A
                            if (array[1].compareToIgnoreCase("B") == 0 && array[2].compareToIgnoreCase("A") == 0) {
                                this.A = this.B;

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.B = 0;
                                }
                            } else // B -> C
                            if (array[1].compareToIgnoreCase("B") == 0 && array[2].compareToIgnoreCase("C") == 0) {
                                this.C = this.B;

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.B = 0;
                                }
                            } else // B -> #M 
                            if (array[1].compareToIgnoreCase("B") == 0 && array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true) {
                                int index = Integer.parseInt(array[2].substring(1));

                                this.MEMORIA[index] = "" + this.B;

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.B = 0;
                                }

                            } else // C -> A
                            if (array[1].compareToIgnoreCase("C") == 0 && array[2].compareToIgnoreCase("A") == 0) {
                                this.A = this.C;

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.C = 0;
                                }
                            } else // C -> B
                            if (array[1].compareToIgnoreCase("C") == 0 && array[2].compareToIgnoreCase("B") == 0) {
                                this.B = this.C;

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.C = 0;
                                }
                            } else // C -> #M 
                            if (array[1].compareToIgnoreCase("C") == 0 && array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true) {
                                int index = Integer.parseInt(array[2].substring(1));

                                this.MEMORIA[index] = "" + this.C;

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.C = 0;
                                }

                            } else // #M1 -> #M2
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true) {
                                int index_0 = Integer.parseInt(array[1].substring(1));
                                int index_1 = Integer.parseInt(array[2].substring(1));

                                this.MEMORIA[index_1] = "" + MEMORIA[index_0];

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.MEMORIA[index_0] = "" + 0;
                                }

                            } else // #M1 -> A
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].compareToIgnoreCase("A") == 0) {
                                int index = Integer.parseInt(array[1].substring(1));

                                this.A = Float.parseFloat(this.MEMORIA[index]);

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.MEMORIA[index] = "" + 0;
                                }

                            } else // #M1 -> B
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].compareToIgnoreCase("B") == 0) {
                                int index = Integer.parseInt(array[1].substring(1));

                                this.B = Float.parseFloat(this.MEMORIA[index]);

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.MEMORIA[index] = "" + 0;
                                }

                            } else // #M1 -> C 
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].compareToIgnoreCase("C") == 0) {
                                int index = Integer.parseInt(array[1].substring(1));

                                this.C = Float.parseFloat(this.MEMORIA[index]);

                                if (array[0].compareToIgnoreCase("MOV") == 0) {
                                    this.MEMORIA[index] = "" + 0;
                                }
                            } else {
                                return true;
                            }

                        } else {
                            return true;
                        }

                    break;

                    case 4:

                        //- EQUAL
                        if (array[0].compareToIgnoreCase("EQUAL") == 0 && array[3].charAt(0) == '#' && array[3].substring(1).matches("[0-9]+") == true) {
                            int index = Integer.parseInt(array[3].substring(1));

                            // A == B
                            if (array[1].compareToIgnoreCase("A") == 0 && array[2].compareToIgnoreCase("B") == 0) {
                                if (this.A == this.B) {
                                    this.PC = index;
                                }
                            } else // A == C   
                            if (array[1].compareToIgnoreCase("A") == 0 && array[2].compareToIgnoreCase("C") == 0) {
                                if (this.A == this.C) {
                                    this.PC = index;
                                }
                            } else // A == #M 
                            if (array[1].compareToIgnoreCase("A") == 0 && array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true) {
                                if (this.A == Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[2].substring(1))))) {
                                    this.PC = index;
                                }
                            } else // A == n 
                            if (array[1].compareToIgnoreCase("A") == 0 && array[2].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (this.A == Float.parseFloat(array[2])) {
                                    this.PC = index;
                                }
                            } else // n == A 
                            if (array[2].compareToIgnoreCase("A") == 0 && array[1].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (this.A == Float.parseFloat(array[1])) {
                                    this.PC = index;
                                }
                            } else // B == A
                            if (array[1].compareToIgnoreCase("B") == 0 && array[2].compareToIgnoreCase("A") == 0) {
                                if (this.B == this.A) {
                                    this.PC = index;
                                }
                            } else // B == C
                            if (array[1].compareToIgnoreCase("B") == 0 && array[2].compareToIgnoreCase("C") == 0) {
                                if (this.B == this.C) {
                                    this.PC = index;
                                }
                            } else // B == #M 
                            if (array[1].compareToIgnoreCase("B") == 0 && array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true) {
                                if (this.B == Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[2].substring(1))))) {
                                    this.PC = index;
                                }
                            } else // B == n 
                            if (array[1].compareToIgnoreCase("B") == 0 && array[2].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (this.B == Float.parseFloat(array[2])) {
                                    this.PC = index;
                                }
                            } else // n == B 
                            if (array[2].compareToIgnoreCase("B") == 0 && array[1].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (this.B == Float.parseFloat(array[1])) {
                                    this.PC = index;
                                }
                            }else // C == A
                            if (array[1].compareToIgnoreCase("C") == 0 && array[2].compareToIgnoreCase("A") == 0) {
                                if (this.C == this.A) {
                                    this.PC = index;
                                }
                            } else // C == B
                            if (array[1].compareToIgnoreCase("C") == 0 && array[2].compareToIgnoreCase("B") == 0) {
                                if (this.C == this.B) {
                                    this.PC = index;
                                }
                            } else // C == #M 
                            if (array[1].compareToIgnoreCase("C") == 0 && array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true) {
                                if (this.C == Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[2].substring(1))))) {
                                    this.PC = index;
                                }
                            } else // C == n 
                            if (array[1].compareToIgnoreCase("C") == 0 && array[2].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (this.C == Float.parseFloat(array[2])) {
                                    this.PC = index;
                                }
                            } else // n == C 
                            if (array[2].compareToIgnoreCase("C") == 0 && array[1].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (this.C == Float.parseFloat(array[1])) {
                                    this.PC = index;
                                }
                            } else // #M1 == #M2
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true) {
                                int index_0 = Integer.parseInt(array[1].substring(1));
                                int index_1 = Integer.parseInt(array[2].substring(1));

                                if (Float.parseFloat(getDADO_MEMORIA(index_0)) == Float.parseFloat(getDADO_MEMORIA(index_1))) {
                                    this.PC = index;
                                }
                            } else // #M1 == A
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].compareToIgnoreCase("A") == 0) {
                                if (this.A == Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[1].substring(1))))) {
                                    this.PC = index;
                                }
                            } else // #M1 == B
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].compareToIgnoreCase("B") == 0) {
                                if (this.B == Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[1].substring(1))))) {
                                    this.PC = index;
                                }
                            } else // #M1 == C 
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].compareToIgnoreCase("C") == 0) {
                                if (this.C == Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[1].substring(1))))) {
                                    this.PC = index;
                                }
                            } else // M == n    
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[1].substring(1)))) == Float.parseFloat(array[2])) {
                                    this.PC = index;
                                }
                            } else // n == M    
                            if (array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true && array[1].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[2].substring(1)))) == Float.parseFloat(array[1])) {
                                    this.PC = index;
                                }
                            } else {
                                return true;
                            }

                        } else //- NEQUAL
                        if (array[0].compareToIgnoreCase("NEQUAL") == 0 && array[3].charAt(0) == '#' && array[3].substring(1).matches("[0-9]+") == true) {
                            int index = Integer.parseInt(array[3].substring(1));

                            // A != B
                            if (array[1].compareToIgnoreCase("A") == 0 && array[2].compareToIgnoreCase("B") == 0) {
                                if (this.A != this.B) {
                                    this.PC = index;
                                }
                            } else // A != C   
                            if (array[1].compareToIgnoreCase("A") == 0 && array[2].compareToIgnoreCase("C") == 0) {
                                if (this.A != this.C) {
                                    this.PC = index;
                                }
                            } else // A != #M 
                            if (array[1].compareToIgnoreCase("A") == 0 && array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true) {
                                if (this.A != Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[2].substring(1))))) {
                                    this.PC = index;
                                }
                            } else // A != n 
                            if (array[1].compareToIgnoreCase("A") == 0 && array[2].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (this.A != Float.parseFloat(array[2])) {
                                    this.PC = index;
                                }
                            } else // n != A 
                            if (array[2].compareToIgnoreCase("A") == 0 && array[1].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (this.A != Float.parseFloat(array[1])) {
                                    this.PC = index;
                                }
                            } else // B != A
                            if (array[1].compareToIgnoreCase("B") == 0 && array[2].compareToIgnoreCase("A") == 0) {
                                if (this.B != this.A) {
                                    this.PC = index;
                                }
                            } else // B != C
                            if (array[1].compareToIgnoreCase("B") == 0 && array[2].compareToIgnoreCase("C") == 0) {
                                if (this.B != this.C) {
                                    this.PC = index;
                                }
                            } else // B != #M 
                            if (array[1].compareToIgnoreCase("B") == 0 && array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true) {
                                if (this.B != Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[2].substring(1))))) {
                                    this.PC = index;
                                }
                            } else // B != n 
                            if (array[1].compareToIgnoreCase("B") == 0 && array[2].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (this.B != Float.parseFloat(array[2])) {
                                    this.PC = index;
                                }
                            } else // n != B
                            if (array[2].compareToIgnoreCase("B") == 0 && array[1].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (this.B != Float.parseFloat(array[1])) {
                                    this.PC = index;
                                }
                            } else // C != A
                            if (array[1].compareToIgnoreCase("C") == 0 && array[2].compareToIgnoreCase("A") == 0) {
                                if (this.C != this.A) {
                                    this.PC = index;
                                }
                            } else // C != B
                            if (array[1].compareToIgnoreCase("C") == 0 && array[2].compareToIgnoreCase("B") == 0) {
                                if (this.C != this.B) {
                                    this.PC = index;
                                }
                            } else // C != #M 
                            if (array[1].compareToIgnoreCase("C") == 0 && array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true) {
                                if (this.C != Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[2].substring(1))))) {
                                    this.PC = index;
                                }
                            } else // C != n 
                            if (array[1].compareToIgnoreCase("C") == 0 && array[2].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (this.C != Float.parseFloat(array[2])) {
                                    this.PC = index;
                                }
                            } else // n != A 
                            if (array[2].compareToIgnoreCase("C") == 0 && array[1].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (this.C != Float.parseFloat(array[1])) {
                                    this.PC = index;
                                }
                            } 
                            else // #M1 != #M2
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true) {
                                int index_0 = Integer.parseInt(array[1].substring(1));
                                int index_1 = Integer.parseInt(array[2].substring(1));

                                if (Float.parseFloat(getDADO_MEMORIA(index_0)) != Float.parseFloat(getDADO_MEMORIA(index_1))) {
                                    this.PC = index;
                                }
                            } else // #M1 != A
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].compareToIgnoreCase("A") == 0) {
                                if (this.A != Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[1].substring(1))))) {
                                    this.PC = index;
                                }
                            } else // #M1 != B
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].compareToIgnoreCase("B") == 0) {
                                if (this.B != Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[1].substring(1))))) {
                                    this.PC = index;
                                }
                            } else // #M1 != C 
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].compareToIgnoreCase("C") == 0) {
                                if (this.C != Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[1].substring(1))))) {
                                    this.PC = index;
                                }
                            } else // M != n    
                            if (array[1].charAt(0) == '#' && array[1].substring(1).matches("[0-9]+") == true && array[2].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[1].substring(1)))) != Float.parseFloat(array[2])) {
                                    this.PC = index;
                                }
                            } else // n != M    
                            if (array[2].charAt(0) == '#' && array[2].substring(1).matches("[0-9]+") == true && array[1].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") == true) {
                                if (Float.parseFloat(getDADO_MEMORIA(Integer.parseInt(array[2].substring(1)))) != Float.parseFloat(array[1])) {
                                    this.PC = index;
                                }
                            } else {
                                return true;
                            }
                        } else {
                            return true;
                        }

                    break;
                    
                    default:
                        return true;

                }
            }

        } catch (Exception e) {
            return true;
        }

        return false;
    }

    public void updateState() {
        //executa manipulaçao dos dados na memoria
        if (END == false) {
            this.MAR = this.PC;
            this.MBR = this.MEMORIA[this.PC];
            updatePC();
            this.IR = this.MBR;
            this.ERRO = interprerador(IR);
        }

    }

}
