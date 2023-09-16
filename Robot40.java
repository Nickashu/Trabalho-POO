package trabalho1_POO;

public class Robot40 {
    private String DRE="122153341", nome="Nícolas da Mota Arruda", estado;
    private int id;
    private char direcaoUnit='X';   //Esta é a direção que o robô vai seguir no caso da sala ter alguma dimensão unitária
    private GPS gps;
    private int linhaCentro, colunaCentro, linhaAtual, colunaAtual, linhaAnterior, colunaAnterior, numLinhas=0, numColunas=0, linhaInicialAlt, colunaInicialAlt;
    private boolean achouLimites=false, alternatingCW = false, isAlternating=false, isNaParede=false;

    public Robot40(int id, GPS gps){
        this.id = id;
        this.gps = gps;
        colunaAtual = this.gps.getC(this.id);
        linhaAtual = this.gps.getL(this.id);
        colunaAnterior = 0;
        linhaAnterior = 0;
        this.estado = "STOP";   //Começa com o estado "STOP"
    }

    public void print(){
        System.out.println("Linha: " + this.gps.getL(this.id) + "  Coluna: " + this.gps.getC(this.id));
    }
    
    //Criando os métodos para alterar o estado do robô:
    public void stop() {
    	this.estado = "STOP";
    }
    
    public void clockwise() {
    	this.estado = "CLOCKWISE";
    }

	public void counter_cw() {
		this.estado = "COUNTER_CW";
	}
	
	public void alternating() {
		this.estado = "ALTERNATING";
	}
	
	//public void goCenter() {    //Método para fazer com que o robô procure o centro da sala, de onde quer que ele esteja (está funcionando, mas está comentado por não fazer parte do escopo do exercício)
	//	this.estado = "GO_CENTER";
	//}
   
   
    public Move MOVE(){
    	linhaAtual = gps.getL(this.id);  //Pegando a linha
        colunaAtual = gps.getC(this.id);  //Pegando a coluna
        
        if((linhaAtual == numLinhas || linhaAtual == 1 || colunaAtual == numColunas || colunaAtual == 1) && achouLimites)   //Aqui eu só verifico se o robô está na parede se ele já souber os limites da sala
        	isNaParede = true;
        else
        	isNaParede = false;
        
        if(this.estado.equals("STOP"))    //Parando o robô
            return Move.STOP;
        
        if(this.estado.equals("CLOCKWISE")) {    //Fazendo o robô girar pela sala em sentido horário
        	Move retorno = procurarParedes();
        	System.out.println("Linha atual: " + linhaAtual + "  Coluna atual: " + colunaAtual);
        	
        	if(retorno != null && !isNaParede)    //Se ainda não chegou na parede
        		return retorno;
        	else
        		return(girar(1));
        	
        }
        
        if(this.estado.equals("COUNTER_CW")) {    //Fazendo o robô girar pela sala em sentido anti-horário
        	Move retorno = procurarParedes();
        	System.out.println("Linha atual: " + linhaAtual + "  Coluna atual: " + colunaAtual);
        	
        	if(retorno != null && !isNaParede)    //Se ainda não chegou na parede
        		return retorno;
        	else 
        		return(girar(2));
        }
        
        if(this.estado.equals("ALTERNATING")) {    //Fazendo o robô girar pela sala em sentidos alternados
        	Move retorno = procurarParedes();
        	
        	if(retorno != null && !isNaParede)    //Se ainda não chegou na parede
        		return retorno;
        	else {
        		if(!isAlternating) {    //Pegando a posição inicial do robô na parede:
        			linhaInicialAlt = linhaAtual;
        			colunaInicialAlt = colunaAtual;
        			isAlternating = true;
        		}
        		
        		if(linhaAtual == linhaInicialAlt && colunaAtual == colunaInicialAlt) {    //Se chegou na posição inicial, vou mudar o sentido do giro
        			alternatingCW = !alternatingCW;
        			if(numLinhas == 1 || numColunas == 1) {
        				if(direcaoUnit == 'U')
        					direcaoUnit = 'D';
        				else if(direcaoUnit == 'D')
        					direcaoUnit = 'U';
        				else if(direcaoUnit == 'R')
        					direcaoUnit = 'L';
        				else if(direcaoUnit == 'L')
        					direcaoUnit = 'R';
        			}
        		}
        		
        		System.out.println("Posição Inicial: " + linhaInicialAlt + " " + colunaInicialAlt + "    Linha atual: " + linhaAtual + "  Coluna atual: " + colunaAtual);
        		
        		if(alternatingCW)
        			return(girar(1));
        		return(girar(2));
        	}
        }
        else
        	isAlternating = false;

        if(this.estado.equals("GO_CENTER")){   //Se está procurando o centro
    		Move movimento = procurarParedes();
    		if(movimento != null)
    			return movimento;
            
    		if(linhaAtual < linhaCentro)
    			return Move.DOWN;
    		else if(linhaAtual > linhaCentro)
    			return Move.UP;
    		
    		if(colunaAtual < colunaCentro)
    			return Move.RIGHT;
    		else if(colunaAtual > colunaCentro)
    			return Move.LEFT;
    		
    		//Se passar de todas as condicionais, quer dizer que chegou no centro:
            this.estado = "STOP";
            return Move.STOP;
        }

        return null;
    }
    
    private Move procurarParedes() {   //Método criado para fazer o robô ir até a parede inferior direita
        if(!achouLimites) {
        	if(colunaAnterior != colunaAtual) {  //Se a coluna mudou
                colunaAnterior = colunaAtual;
                return Move.RIGHT;
            }
            else if(linhaAnterior != linhaAtual) {   //Se a linha mudou
                linhaAnterior = linhaAtual;
                return Move.DOWN;
            }
        	
        	numLinhas = linhaAtual;
            numColunas = colunaAtual;
            colunaCentro = numColunas/2 + 1;
            linhaCentro = numLinhas/2 + 1;
            achouLimites = true;
            //System.out.println("linhas: " + numLinhas + "  colunas: " + numColunas);
            
            return null;   //Se chegar aqui, é porque achou os limites da sala e o numColunas e o numLinhas já possuem seus valores.
        }
        else {   //Se já sabe os limites, vai para a parede da direita
        	if(colunaAtual < numColunas && !estado.equals("GO_CENTER"))    //Até chegar na parede da direita
        		return Move.RIGHT;
        	
        	return null;
        }
    }
    
    private Move girar(int direcao) {   //Método criado para fazer o robô girar pela sala
    	if(numLinhas != 1 && numColunas != 1) {    //Se a sala não posssuir nenhuma dimensão de valor unitário
    		switch(direcao) {
	    		case 1:  //Girando no sentido horário:
					if(linhaAtual == numLinhas) {   //Se estiver na parede de baixo
	    				if(colunaAtual != 1)    //Se não estiver na quina da sala
	    					return Move.LEFT;
	    			}
					if(colunaAtual == numColunas) {     //Se estiver na parede da direita
	    				if(linhaAtual != numLinhas)
	    					return Move.DOWN;
	    			}
	    			if(linhaAtual == 1) {    //Se estiver na parede de cima
	    				if(colunaAtual != numColunas)
	    					return Move.RIGHT;
	    			}
	    			if(colunaAtual == 1) {     //Se estiver na parede da esquerda
	    				if(linhaAtual != 1)
	    					return Move.UP;
	    			}
	    			break;
	    			
	    		case 2:   //Girando no sentido anti-horário:
					if(linhaAtual == numLinhas) {   //Se estiver na parede de baixo
	    				if(colunaAtual != numColunas)    //Se não estiver na quina da sala
	    					return Move.RIGHT;
	    			}
	    			if(linhaAtual == 1) {    //Se estiver na parede de cima
	    				if(colunaAtual != 1)
	    					return Move.LEFT;
	    			}
	    			if(colunaAtual == numColunas) {     //Se estiver na parede da direita
	    				if(linhaAtual != 1)
	    					return Move.UP;
	    			}
	    			if(colunaAtual == 1) {     //Se estiver na parede da esquerda
	    				if(linhaAtual != numLinhas)
	    					return Move.DOWN;
	    			}
	    			break;
    		}
    	}
    	else {   //Se a sala possuir alguma dimensão de valor unitário. Neste caso, o robô só fica indo e voltando na linha ou na coluna (se o estado for alternating, ele vai até o fim da linha/coluna, retorna para a posição original e fica repetindo isso)
    		if(numLinhas != 1) {
    			if(direcaoUnit == 'X')
    				direcaoUnit = 'U';
    			
				if(linhaAtual == numLinhas)
					direcaoUnit = 'U';
				else if(linhaAtual == 1)
					direcaoUnit = 'D';
				return movimentoUnitario(direcaoUnit);
				
			}
			else if(numColunas != 1) {
				if(direcaoUnit == 'X')
    				direcaoUnit = 'R';
				
				if(colunaAtual == numColunas) {
					direcaoUnit = 'L';
				}
				else if(colunaAtual == 1)
					direcaoUnit = 'R';
				return movimentoUnitario(direcaoUnit);
			}
    		
			return null;   //Se a sala for 1x1, o robô não anda
    	}
    	
    	return null;
    }
    
    private Move movimentoUnitario(char direcao) {   //Para mover o robô no caso da sala ter uma dimensão de valor unitário
    	switch(direcao) {
			case 'U':
				return Move.UP;
			case 'D':
				return Move.DOWN;
			case 'L':
				return Move.LEFT;
			case 'R':
				return Move.RIGHT;
    	}
    	return null;
    }
    
}