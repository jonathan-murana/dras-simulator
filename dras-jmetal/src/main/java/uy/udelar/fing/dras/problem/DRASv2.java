package uy.udelar.fing.dras.problem;

import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.JMetalException;

import uy.udelar.fing.dras.utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Antonio J. Nebro on 03/07/14.
 * Bi-objective problem for testing integer encoding.
 * Objective 1: minimizing the distance to value N
 * Objective 2: minimizing the distance to value M
 */
@SuppressWarnings("serial")
public class DRASv2 extends AbstractIntegerProblem {

	

	public int getR() {
		return R;
	}
	public int getK() {
		return K;
	}
	public int getW() {
		return W;
	}
	
//CONSTANTES
	
static int kw_x_server = 1;
static int server_mips = 1000; 
static int server_prcs_number = 4; 
static int prc_power_consumption = 30; //watts

static int server_power_lower_limit = 0; 
static int server_power_upper_limit = 120; 
	
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
  int [] MIPS = null;
  int [] AT = null; // arrive time
  
  //int [] F = null;
  // int [] VT = null;
  

  int scheduling_duration, 
  K, // timesteps 
  dolar_per_kw;
  int [] P = null;
  
  
  
  
  //1 0 1000 0 2000 // id clientId mips arriveTime deadline
  
  private String instance_file ;
  private String workload_file ;

  
  public DRASv2(String instanceFile, String workloadFile) {
	  this.instance_file = instanceFile;
	  this.workload_file = workloadFile;
	  
	try {
		readProblem(instanceFile);
		printProblem();
		readWorkload(workloadFile);
		printWorkload();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	
    setNumberOfVariables(K * R);    
    setNumberOfObjectives(3);
    setName("DRAS");

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
	  int [][] server_usage_by_steps = Utils.zeros_matrix(K, W);
	  int [] assigned_task = Utils.zeros_vector(W);
	  return schedule(power_assignation_by_steps,
			  server_usage_by_steps,
			  assigned_task);
	  }
  
    public int [] schedule(List<Integer> power_assignation_by_steps,
		  int [][] server_usage_by_steps, int [] assigned_task) {	  
	  int scheduling_step_duration = scheduling_duration / K;
	  int task_duration = -1;
	  int task_steps = -1;
	  int currect_execution_time = -1;
	  // task completed time
	  int [] F = Utils.not_ones_vector(W);
	  // for each steps
	  for (int k = 0 ; k < K ;k++ ) {
		  currect_execution_time = k * scheduling_step_duration;
		  // for each servers
		  for (int r = 0 ; r < R ; r++ ) {
				  // looking for the first suitable task
				  for (int i = 0; i < W ; i++) {
					  if  (	assigned_task[i] == 0 && currect_execution_time >= AT[i] && CT[i][owner[r]] == 1 ) {
						  task_duration = MIPS[i] / server_mips;
						  task_steps = (task_duration / scheduling_step_duration) +1;
						  // server must be available for the task
						  boolean isFull= false;						  
						  for (int u = 0;u < task_steps ;u++){
							  if (	  k+u >= K || server_usage_by_steps [k+u][r] >= server_prcs_number ||
									  // TODO: power function
									  power_assignation_by_steps.get((( k + u) * R) + r).intValue() 
									  < (server_usage_by_steps[k+u][r] + 1)  * prc_power_consumption
									  ) {
								  isFull= true;
								  break;
							  }}
						  //assign task
						  if (!isFull) {						  
							  for (int u = 0;u < task_steps;u++){
								  server_usage_by_steps [k+u][r] ++;
								  assigned_task[i] = 1;
								  
							  }
							  F[i] = currect_execution_time +  task_steps * scheduling_step_duration;
						  }}}}}	 
	  return F;
  }
  
  /** Evaluate() method */
  @Override
  public void evaluate(IntegerSolution solution) {
	  
	 int [] VT = Utils.zeros_vector(W);
	 int [] F = schedule(solution.getVariables());
	 for (int i = 0;i<W;i++ ){
		 VT[i] = F[i] > AT[i] +  D[i]?1:0;
	 }
	 
	//**********  OBJETIVO ZP ************/
	  int[] P_techo = Utils.zeros_vector(K);
	  for (int t=0;t<K;t++ ){
		  for(int r=0; r<R; r++) {
			  P_techo[t] += solution.getVariables().get(R * t + r);
		  }
	  }

	  int Zp =Utils.max_abs(Utils.subtract(P, P_techo));
	 
	//**********  OBJETIVO ZB ************/
	  
	  // TODO: esto viene en el escenario?? hay que leerlo
	  int [] M = Utils.zeros_vector(K);
	  int [] B = Utils.zeros_vector(K);
	  
	  double Zb = 0;
	  
	  for (int t = 0;t<K;t++ ) {
		  Zb += M[t] + B[t];
	  }
	  
	  double sum = 0;

	  for (int j = 0;j<C;j++ ) {
		  for (int i = 0;i<W;i++ ){

			  if ( DND[i]==1) {
				  sum = CRND[j] * CT[i][j] * VT[i];
			  }
			  if ( DND[i]==0) {
				  sum = CRD[j] * (F[i] - (AT[i] + D[i])) * CT[i][j] * VT[i] ;
			  }
			  Zb += sum;			  
		  }
	   }
	  
  	 //**********  OBJETIVO ZQ ************/

	  
	  double Zq = 1;
	  double factor = 1;
	  
	  for (int j = 0;j<C;j++ ) {
		  for (int i = 0;i<W;i++ ){
			  			  
			  if ( CT[i][j] * VT[i] == 0) {
				  factor = 1;
			  }
			  if ( CT[i][j] * VT[i] == 1 && DND[i]==1) {
				  factor = PAND[j];
			  }
			  if ( CT[i][j] * VT[i] == 1 && DND[i]==0) {
				  factor = PAD[j] * (F[i] - (AT[i] + D[i]) );
			  }
			  Zq *= factor;
			  
		  }
	   }
	  
    solution.setObjective(0, Zp);
    solution.setObjective(1, Zb );
    solution.setObjective(2, Zq );
    
	  

//    solution.setObjective(0, solution.getVariableValue(0));
//    solution.setObjective(1, solution.getVariableValue(0));
//    solution.setObjective(2, solution.getVariableValue(0));
//    
//    solution.setObjective(0, 3);
//    solution.setObjective(1, 2);
//    solution.setObjective(2, 4);
  
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
					  dolar_per_kw = Integer.valueOf(splittedLine[2]);
				} else if (numeroLinea == 3 + C) {
					P = new int[K];
					for (int i = 0;i<K;i++) {
						P[i] = Integer.valueOf(splittedLine[i]);
					}

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
	  System.out.println("dolar_per_kw= "+ this.dolar_per_kw);
	  
	  System.out.print("P = ");
	  for (int i = 0;i<this.K;i++,System.out.print(" ")) {
		  System.out.print(P[i]);
	  }
	  
	  
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
					
				}else if (numeroLinea >=2 && numeroLinea <= 1 + W) {				
				
					int j= Integer.valueOf(splittedLine[0]);
					MIPS[i] = Integer.valueOf(splittedLine[1]); 
					AT[i] = Integer.valueOf(splittedLine[2]); 
					CT[i][j]  = 1;
					D[i] = Integer.valueOf(splittedLine[3]); 
					DND[i] = Integer.valueOf(splittedLine[4]); 
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
	  System.out.println("====================================");
 	  }
}
