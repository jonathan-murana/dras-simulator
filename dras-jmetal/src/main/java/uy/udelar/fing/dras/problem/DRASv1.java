package uy.udelar.fing.dras.problem;

import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import uy.udelar.fing.dras.heuristics.____HEU_FF_AT;
import uy.udelar.fing.dras.utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio J. Nebro on 03/07/14.
 * Bi-objective problem for testing integer encoding.
 * Objective 1: minimizing the distance to value N
 * Objective 2: minimizing the distance to value M
 */
@SuppressWarnings("serial")
public class DRASv1 extends AbstractIntegerProblem {
	
	public String getInstanceFile() {
		return instance_file;
	}
	public String getWorkloadFile() {
		return workload_file;
	}
	public String getReferenceFile() {
		return reference_file;
	}

	public int getR() {
		return R;
	}
	public int getK() {
		return K;
	}
	public int getW() {
		return W;
	}
	
	public int[] getP() {
		return P;
	}

	public int[] getD() {
		return D;
	}
	public int[] getAT() {
		return AT;
	}
	public int[] getLoss() {
		return loss;
	}
	public double[] getTolerance() {
		return tolerance;
	}

public int getScheduling_duration() {
		return scheduling_duration;
	}

public int[] getMIPS() {
		return MIPS;
	}




public int[] getRN() {
	return RN;
}
public void setRN(int[] rN) {
	RN = rN;
}
public int[] getOwner() {
	return owner;
}
public void setOwner(int[] owner) {
	this.owner = owner;
}
public int[][] getCT() {
	return CT;
}





public int[] getType() {
	return type;
}





static int server_power_lower_limit = 116; 
static int server_power_upper_limit = 250; 
	
  private int C, W, 
  R // total server number
  ;
 
  // clients
  double [] 	PAND 	= null;
  double [] 	PAD 	= null;
  int	 [] 	CRND  	= null;
  int 	 [] 	CRD 	= null;
  int 	 [] 	RN 	= null; //server by client

  int 	 [] 	owner 	= null; //servers owner (which clients)

  //tasks
  int [][] CT = null;
  int [] DND = null;
  int [] D = null;
  private int [] MIPS = null;
  int [] AT = null; // arrive time
  int [] loss = null; // loss time
  int [] type = null; 
  double [] tolerance 	= null;

  int scheduling_duration, 
  K // timesteps 
  ;
  int [] P = null;
  
  private String instance_file ;
  private String workload_file ;
  private String reference_file ;

  private Double price;
  
  
  public Double getPrice() {
	return price;
}

public DRASv1(Double price, String instanceFile, String workloadFile, String referenceFile) {
	  
	this.price=price;
	this.instance_file 	= instanceFile;
	  this.workload_file 	= workloadFile;
	  this.reference_file 	= referenceFile;
	  
	try {
		readProblem(instanceFile);		
		if (Utils.testLevel() > 1) {
			printProblem();
		}		
		readWorkload(workloadFile);
		if (Utils.testLevel() > 1) {
			 printWorkload();
		}
		/*
		readReference(referenceFile);
		if (TestUtils.testLevel() > 1) {
			 printReference();
		}*/
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	
    setNumberOfVariables(K * R);    
    setNumberOfObjectives(2);
    setName("DRASv1");
    
    P = new int[K];

    List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
    List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables()) ;
    
    
    for (int i = 0; i < getNumberOfVariables(); i++) {
      lowerLimit.add(server_power_lower_limit);
      upperLimit.add(server_power_upper_limit);
    }

    setLowerLimit(lowerLimit);
    setUpperLimit(upperLimit);
	
  }
 
  public int [] schedule(List<Integer> power_assignation_by_steps) {
	  int [][] server_usage_by_steps_cpu = Utils.zeros_matrix(K, W);
	  int [][] server_usage_by_steps_mem = Utils.zeros_matrix(K, W);

	  int [] assigned_task = Utils.zeros_vector(W);
	  return ____HEU_FF_AT.schedule(this,power_assignation_by_steps,
			  server_usage_by_steps_cpu,
			  server_usage_by_steps_mem,
			  assigned_task);
	  }
  
 
  
  /** Evaluate() method */
  @Override
  public void evaluate(IntegerSolution solution) {
	  
	 int [] VT = Utils.zeros_vector(W);
	 int [] F = schedule(solution.getVariables());
	 for (int i = 0;i<W;i++ ){
		 VT[i] = F[i] > AT[i] +  D[i]?1:0;
	 }
	 
	//**********  Z ALFA ************/
	  int[] P_techo = Utils.zeros_vector(K);
	  for (int t=0;t<K;t++ ){
		  for(int r=0; r<R; r++) {
			  P_techo[t] += solution.getVariables().get(R * t + r);
		  }
	  }

	  int Z_alfa =Utils.min_abs(Utils.subtract(P, P_techo));
	  //int Zp = Utils.sumarize(P_techo);
	  
	 
	//**********  Z LOSS ************/
	  
	  double Z_loss = 0;

	  for (int i = 0;i<W;i++ ){
			   if ( F[i] == -1 || (F[i] > (AT[i] + D[i])) )	{
				   Z_loss += loss[i];
			   }
	  }
	  

	 
    solution.setObjective(0, -1 * Z_alfa);
    solution.setObjective(1, -1 * (Z_alfa*price - Z_loss) );
  
  }
  
  /** READ AND WRITE METHODS**/
  
  
  private void readProblem(String file) throws IOException {

	  	//ancillary variables 	  
	  	int j = 0; //clients
	  	int server_index = 0; //clients

	  	
	  	String [] splittedLine = null;
	  
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					file));
			String line = reader.readLine();
			int numeroLinea = 0;
			while (line != null) {
				numeroLinea++;
				splittedLine = line.split(" ");
				if (numeroLinea == 1) {
					 C = Integer.valueOf(splittedLine[0]);
					 R = Integer.valueOf(splittedLine[1]);
					 
					 RN 	= new int[C];
					 PAND 	= new double[C];
					 PAD 	= new double[C];
					 CRND  	= new int[C];
					 CRD 	= new int[C];
					 owner		= new int[R]; 
					 
					 
				}else if (numeroLinea >=2 && numeroLinea <= 1 + C) {					
				
					 RN[j] 	= Integer.valueOf(splittedLine[0]);
					 PAND[j]  	= Double.valueOf(splittedLine[1]);
					 PAD[j]  	= Double.valueOf(splittedLine[2]);
					 CRND[j]  	= Integer.valueOf(splittedLine[3]);
					 CRD[j] 	= Integer.valueOf(splittedLine[4]);

					 
					 
					 //owner assign
					 for (int s = 0;s < RN[j];s++) {
						 owner[server_index++] = j;
					 }
					 
					j++;
					
				} else if (numeroLinea == 2 + C) {
					  scheduling_duration = Integer.valueOf(splittedLine[0]);
					  K = Integer.valueOf(splittedLine[1]);
					  //dolar_per_kw = Integer.valueOf(splittedLine[2]);
				} 
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
  
  private void printProblem(){

	  System.out.println("PRINT DRAS PROBLEM. Instance: "+ this.instance_file);
	  System.out.println("clients_number= "+ this.C);
	  System.out.println("servers_number= "+ this.R);
	  System.out.print("RN = ");
	  for (int i = 0;i<this.C;i++,System.out.print(" ")) {
		  System.out.print(RN[i]);
	  }
	  System.out.println();
	  System.out.print("PAND = ");
	  for (int i = 0;i<this.C;i++,System.out.print(" ")) {
		  System.out.print(PAND[i]);
	  }
	  System.out.println();
	  System.out.print("PAD = ");
	  for (int i = 0;i<this.C;i++,System.out.print(" ")) {
		  System.out.print(PAD[i]);
	  }
	  System.out.println();
	  System.out.print("CRND = ");
	  for (int i = 0;i<this.C;i++,System.out.print(" ")) {
		  System.out.print(CRND[i]);
	  }
	  System.out.println();
	  System.out.print("CRD = ");
	  for (int i = 0;i<this.C;i++,System.out.print(" ")) {
		  System.out.print(CRD[i]);
	  }

	  System.out.println();
	  System.out.println("duration= "+ this.scheduling_duration);
	  System.out.println("intervals_number= "+ this.K);
	  //System.out.println("dolar_per_kw= "+ this.dolar_per_kw);
	  
//	  System.out.print("P = ");
//	  for (int i = 0;i<this.K;i++,System.out.print(" ")) {
//		  System.out.print(P[i]);
//	  }
	  
	  
	  System.out.println();
	  System.out.println("====================================");
  
	  
	  }
  
  private void readWorkload(String file) throws IOException {

	  	//auliliar variables 
	  
	  	int i = 0; //task id 
	  
	  	String [] splittedLine = null;
	  
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					file));
			String line = reader.readLine();
			int numeroLinea = 0;
			while (line != null) {
				numeroLinea++;
				splittedLine = line.split(" ");				
				
				if (numeroLinea == 1) {
					
					 W = Integer.valueOf(splittedLine[0]);					 
					 CT 	= Utils.zeros_matrix(W, C);
					 D 		= new int[W];
					 DND 	= Utils.zeros_vector(W);
					 MIPS 	= Utils.zeros_vector(W);
					 AT 	= Utils.zeros_vector(W);
					 loss 	= Utils.zeros_vector(W);
					 type 	= Utils.zeros_vector(W);
					 tolerance 	= Utils.double_zeros_vector(W);

					
				}else if (numeroLinea >=2 && numeroLinea <= 1 + W) {				
				
					int j= Integer.valueOf(splittedLine[0]);
					MIPS[i] = Integer.valueOf(splittedLine[1]); 
					AT[i] = Integer.valueOf(splittedLine[2]); 
					CT[i][j]  = 1;
					D[i] = Integer.valueOf(splittedLine[3]); 
					DND[i] = Integer.valueOf(splittedLine[4]); 
					loss[i] = Integer.valueOf(splittedLine[5]); 
					type[i] = Integer.valueOf(splittedLine[6]); 
					tolerance[i] 	= Double.valueOf(splittedLine[7]);

					i++;
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }

  private void printWorkload(){

	  System.out.println("PRINT DRAS WORKLOAD. Instance: "+ this.workload_file);
	  System.out.print("D = ");
	  for (int i = 0;i<this.W;i++,System.out.print(" ")) {
		  System.out.print(this.D[i]+", ");
	  }
	  System.out.println();
	  System.out.print("MIPS = ");
	  for (int i = 0;i<this.W;i++,System.out.print(" ")) {
		  System.out.print(this.MIPS[i]+", ");
	  }
	  System.out.println();
	  System.out.println("CT = ");
		 for (int i_aux = 0;i_aux<W;i_aux++ ) {
			 for (int j_aux = 0;j_aux<C;j_aux++ ){
				 System.out.print(CT[i_aux][j_aux]+", ");
			 }
			 System.out.println();
		 }
	  System.out.println();
	  System.out.print("DND = ");
	  for (int i = 0;i<this.W;i++,System.out.print(" ")) {
		  System.out.print(this.DND[i]+", ");
	  }
	  System.out.println();
	  System.out.print("loss = ");
	  for (int i = 0;i<this.W;i++,System.out.print(" ")) {
		  System.out.print(this.loss[i]+", ");
	  }
	  System.out.println();
	  System.out.print("type = ");
	  for (int i = 0;i<this.W;i++,System.out.print(" ")) {
		  System.out.print(this.type[i]+", ");
	  }
	  System.out.println();
	  System.out.println("====================================");
 	  }
  
  
  /*
  private void readReference(String file) throws IOException {
	  	
	  	String [] splittedLine = null;
	  
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					file));
			String line = reader.readLine();
			splittedLine = line.split(" ");
			P = new int[K];
			for (int i = 0;i<K;i++) {
				P[i] = Integer.valueOf(splittedLine[i]);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }

private void printReference(){

	  System.out.println("PRINT DRAS REFERENCE. Instance: "+ this.reference_file);

	  System.out.print("P = ");
	  for (int i = 0;i<this.K;i++,System.out.print(" ")) {
		  System.out.print(P[i]);
	  }
	  System.out.println();
	  System.out.println("====================================");
	  }

*/
}
