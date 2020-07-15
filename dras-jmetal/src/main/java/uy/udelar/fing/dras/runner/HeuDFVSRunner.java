package uy.udelar.fing.dras.runner;


import java.util.List;

import uy.udelar.fing.dras.heuristics.HEU_HIGHEST_DEADLINE_BAR_MIPS_WITH_CORES;
import uy.udelar.fing.dras.heuristics.HEU_HIGHEST_DEADLINE_FIRST_WITH_CORES;
import uy.udelar.fing.dras.heuristics.HEU_HIGHEST_DEADLINE_HIGHEST_MIPS_WITH_CORES;
import uy.udelar.fing.dras.heuristics.HEU_HIGHEST_MIPS_FIRST_WITH_CORES;
import uy.udelar.fing.dras.heuristics.HEU_HIGHEST_PENALTY_BAR_DEADLINE_WITH_CORES;
import uy.udelar.fing.dras.heuristics.HEU_HIGHEST_PENALTY_BAR_MIPS_WITH_CORES;
import uy.udelar.fing.dras.heuristics.HEU_HIGHEST_PENALTY_FIRST_WITH_CORES;
import uy.udelar.fing.dras.heuristics.HEU_HIGHEST_PENALTY_HIGHEST_DEADLINE_WITH_CORES;
import uy.udelar.fing.dras.heuristics.HEU_HIGHEST_PENALTY_HIGHEST_MIPS_WITH_CORES;
import uy.udelar.fing.dras.heuristics.HEU_LOWEST_DEADLINE_FIRST_WITH_CORES;
import uy.udelar.fing.dras.heuristics.HEU_LOWEST_ID_FIRST_WITH_CORES;
import uy.udelar.fing.dras.heuristics.HEU_LOWEST_MIPS_FIRST_WITH_CORES;
import uy.udelar.fing.dras.heuristics.HEU_LOWEST_PENALTY_FIRST_WITH_CORES;
import uy.udelar.fing.dras.problem.Constants;
import uy.udelar.fing.dras.problem.DRASv1;
import uy.udelar.fing.dras.utils.Objectives;
import uy.udelar.fing.dras.utils.ObjectivesVector;
import uy.udelar.fing.dras.utils.Utils;



/**
 * Class for configuring and running the NSGA-II algorithm (integer encoding)
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class HeuDFVSRunner {
 
	

	public static final String problem_name = "DRASv1";

	private final static String instance_name = "ins";
	private final static String workload_name = "w";
	private final static String referece_name = "ref";

 	public static String instance_folder  = "instances/example1";
	
 	public static int steps_reduction= 1;
	
 	
	public static ObjectivesVector getObjetctivesVector(Integer clientId, String instanceFolder,int localHeuristic) {

		
		
		
		
		 DRASv1 problem = new DRASv1(0.0,
				 instanceFolder+"/"+instance_name,
				 instanceFolder+"/"+workload_name+clientId,
				 instanceFolder+"/"+referece_name);
	    	int R = problem.getR();
	    	int K = problem.getK();
	    	int W = problem.getW();

			steps_reduction= problem.getRN()[clientId] * Constants.server_prcs_number;
			
			
		    ObjectivesVector objectivesVector = new ObjectivesVector();
			   
		    objectivesVector.reductionVector = new Double[steps_reduction + 1];
		    objectivesVector.lossVector = new Double[steps_reduction + 1];
		    objectivesVector.nonCompleteTasks = new Double[steps_reduction + 1];
		    objectivesVector.violatedTime = new Double[steps_reduction + 1];
		    objectivesVector.price = new Double[steps_reduction + 1];
		    objectivesVector.penalty = new Double[steps_reduction + 1];

			
	    	//steps_reduction=  100  ;
			//System.out.println("steps_reduction: " + steps_reduction);
			Objectives objectivesResult = new Objectives();
			objectivesResult.alpha = 0.0;
			objectivesResult.profit = 0.0;
			objectivesResult.loss = 0.0;
			objectivesResult.payment = 0.0;
			objectivesResult.reductionPercentage = 0.0;
			objectivesResult.nonCompleteTasks = 0.0;
			objectivesResult.penalty = 0.0;


			// obtaining reference
	    	List<Integer> variables = Utils.random(R*K, Integer.MAX_VALUE , Integer.MAX_VALUE ,654654);
		    int [][] s_cpu = Utils.zeros_matrix(K, problem.getRN()[clientId]);
		    int [][] s_mem = Utils.zeros_matrix(K, problem.getRN()[clientId]);
		    int [] assigned_task = Utils.zeros_vector(W);		    
		    
		    int [] F = null;
		    
		    //siempre es bau
		    F =  HEU_LOWEST_ID_FIRST_WITH_CORES.schedule(Integer.MAX_VALUE,clientId,problem,variables,s_cpu,s_mem,assigned_task);	   
//		    if (localHeuristic==1) {
//			    F =  HEU_HIGHEST_PENALTY_FIRST_WITH_CORES.schedule(Integer.MAX_VALUE,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
//		    }
//		    if (localHeuristic==2) {
//			    F =  HEU_LOWEST_ID_FIRST_WITH_CORES.schedule(Integer.MAX_VALUE,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
//		    }
//		    if (localHeuristic==3) {
//			    F =  HEU_HIGHEST_DEADLINE_FIRST_WITH_CORES.schedule(Integer.MAX_VALUE,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
//		    }
//		    if (localHeuristic==4) {
//			    F =  HEU_LOWEST_PENALTY_FIRST_WITH_CORES.schedule(Integer.MAX_VALUE,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
//		    }
//		    if (localHeuristic==5) {
//			    F =  HEU_LOWEST_DEADLINE_FIRST_WITH_CORES.schedule(Integer.MAX_VALUE,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
//		    }
//		    if (localHeuristic==6) {
//			    F =  HEU_LOWEST_MIPS_FIRST_WITH_CORES.schedule(Integer.MAX_VALUE,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
//		    }
//		    if (localHeuristic==7) {
//			    F =  HEU_HIGHEST_MIPS_FIRST_WITH_CORES.schedule(Integer.MAX_VALUE,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
//		    }
		    //abajo tambien cabiar!!!
		    
		    List<Integer> solution = Utils.getPowerSolution(clientId,problem, F, s_cpu, s_mem);
			int[] P_techo = Utils.zeros_vector(problem.getK());			
			for (int t=0;t<problem.getK();t++ ){
				 for(int r=0; r<problem.getRN()[clientId]; r++) {
					 P_techo[t] += solution.get(problem.getRN()[clientId] * t + r);
					
				 }
				 //System.out.print(" " +  P_techo[t]);
			 }
			//System.out.println(" ");
			
			
			Integer alpha_ref = Utils.max_abs(P_techo);	    
			//if (Utils.testLevel() > 2) {
			//	System.out.println("alpha_ref: " + alpha_ref);
			//}
		    //System.out.println("alpha_ref: " + alpha_ref);
			//if (Utils.testLevel() > 1) {
//				System.out.println("schedule ===============>>>>>>>>>");
//				for (int r = 0;r<problem.getRN()[clientId];r++ ){
//					 for (int k = 0;k<K;k++ ) {
//						 System.out.print((s_cpu[k][r]+s_mem[k][r]) +", ");
//					 }
//					 System.out.println();
//				}
			//}
			//System.exit(0);
			//Objectives objectivesInitial = Utils.evaluate(alpha_ref,clientId,problem, F, s_cpu, s_mem);
			
		    //Double initialLoss = objectivesInitial.loss;
		    Double initialLoss = 0.0;

			
			
	    	for (int i = steps_reduction;i >=0;i--) {
				//for (int i = 1;i<2+1;i++) {
	    		
	    		//double reductionPercentage = Double.valueOf(i)   / steps_reduction ;
	    		//int coresPerStep = Double.valueOf(Constants.server_prcs_number * problem.getRN()[clientId] * reductionPercentage).intValue();
				
	    		int coresPerStep = i;
	    		
	    		//System.out.println("reductionPercentage: " + reductionPercentage);
				//System.out.println("coresPerStep: " + coresPerStep);

	    		
	    		//if (Utils.testLevel() > 2) {
				//	System.out.println("coresPerStep: " + coresPerStep);
				//}	
	    		
		    	// infinite power
		    	variables = Utils.random(R*K, Integer.MAX_VALUE , Integer.MAX_VALUE ,654654);
		    	
		    	//FF_ID
			    s_cpu = Utils.zeros_matrix(K, problem.getRN()[clientId]);
			    s_mem = Utils.zeros_matrix(K, problem.getRN()[clientId]);
			    
			    assigned_task = Utils.zeros_vector(W);		    
			    
			    //init = System.currentTimeMillis();
			    
			    
			    if (localHeuristic==1) {
				    F =  HEU_HIGHEST_PENALTY_FIRST_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    if (localHeuristic==2) {
				    F =  HEU_LOWEST_ID_FIRST_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    if (localHeuristic==3) {
				    F =  HEU_HIGHEST_DEADLINE_FIRST_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    if (localHeuristic==4) {
				    F =  HEU_LOWEST_PENALTY_FIRST_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    if (localHeuristic==5) {
				    F =  HEU_LOWEST_DEADLINE_FIRST_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    if (localHeuristic==6) {
				    F =  HEU_LOWEST_MIPS_FIRST_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    if (localHeuristic==7) {
				    F =  HEU_HIGHEST_MIPS_FIRST_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    if (localHeuristic==8) {
				    F =  HEU_HIGHEST_PENALTY_HIGHEST_MIPS_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    if (localHeuristic==9) {
				    F =  HEU_HIGHEST_PENALTY_HIGHEST_DEADLINE_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    if (localHeuristic==10) {
				    F =  HEU_HIGHEST_DEADLINE_HIGHEST_MIPS_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    if (localHeuristic==11) {
				    F =  HEU_HIGHEST_PENALTY_BAR_MIPS_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    if (localHeuristic==12) {
				    F =  HEU_HIGHEST_PENALTY_BAR_DEADLINE_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    if (localHeuristic==13) {
				    F =  HEU_HIGHEST_DEADLINE_BAR_MIPS_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    }
			    
			    
			    //System.out.println("demora schedule: " + (System.currentTimeMillis() - init));
			    
			    
			    Objectives objectives = Utils.evaluate(alpha_ref,clientId,problem, F, s_cpu, s_mem);
				
			    //add initial loss to profit
			    objectives.profit = objectives.profit  + initialLoss;
			    
			    
			    //System.out.println("alpha_ref: " + coresPerStep);
			    
			    
				if (Utils.testLevel() > 2) {
					System.out.println("schedule ===============>>>>>>>>>");
					for (int r = 0;r<problem.getRN()[clientId];r++ ){
						 for (int k = 0;k<K;k++ ) {
							 System.out.print((s_cpu[k][r]+s_mem[k][r]) +", ");
						 }
						 System.out.println();
					}
				}
				
				//alpha must be less than alpha ref
				//System.out.println("profit=" +  objectives.profit);
				//System.out.println("alpha=" +  objectives.alpha);
				//System.out.println("loss=" +  objectives.loss);
				//System.out.println("--------");
				
				//se poda por cambio de direccion
//				Double profitDirection = null;
//				if (lastProfitDirection != null) {
//					profitDirection = lastProfit - objectives.profit;					
//					if (profitDirection * lastProfitDirection < 1 ) {
//						break;
//					}
//				}
//				lastProfitDirection = profitDirection;
//				lastProfit = objectives.profit;
				
//					System.out.println(
//							",coresPerStep=" + coresPerStep 
//						+",loss=" +  objectives.loss
//						+",alpha=" +  objectives.alpha
//						+",rel=" + objectives.alpha / objectives.loss
//						+",nonCompleteTasks=" + objectives.nonCompleteTasks
//						);

				
			//	if (lastProfit != null && lastProfit > objectives.profit) {
			 //   	System.out.println("break=");

			//		break;
			//	}
				
				 // TODO: asumo que la tolerancia del cliente es la de la primea tarea, pasar esto como atributo del cliente
				 objectivesVector.clientTolerance = problem.getTolerance()[0];
				 objectivesVector.reductionVector[i] = objectives.alpha;
				 objectivesVector.lossVector[i] = objectives.loss;
				 objectivesVector.nonCompleteTasks[i] = objectives.nonCompleteTasks;
				 objectivesVector.penalty[i] = objectives.loss /objectivesVector.clientTolerance;				 
				 objectivesVector.violatedTime[i] = objectives.violatedTime;
				 objectivesVector.price[i] = objectives.payment;

				 
				 //System.out.println("nonCompleteTasks=" +  objectives.nonCompleteTasks);
				 //System.out.println("penalty=" +  objectives.loss /objectivesVector.clientTolerance);
				 
				if (objectives.profit > objectivesResult.profit ) {
					objectivesResult.alpha = objectives.alpha;
					objectivesResult.profit = objectives.profit;
					objectivesResult.loss =  objectives.loss;
					objectivesResult.payment =  objectives.payment;
					objectivesResult.reductionPercentage = coresPerStep*1.0;
					objectivesResult.nonCompleteTasks = objectives.nonCompleteTasks;
					objectivesResult.penalty = objectives.loss/objectivesVector.clientTolerance;
					
				
					//System.out.println("nonCompleteTasks=" +  objectivesResult.nonCompleteTasks);
					//System.out.println("penalty=" +  objectivesResult.penalty);

					
					
				}

	    	}

			
	    	//if (Utils.testLevel() >= 1) {
	    	//System.out.println("alpha=" +  objectivesResult.alpha);
	    	//System.out.println("profit=" +  objectivesResult.profit);
	    	//System.out.println("loss=" +  objectivesResult.loss);
	    	//System.out.println("payment=" +  objectivesResult.payment);
	    	//	System.out.println("reductionPercentage=" +  objectivesResult.reductionPercentage);
	    	//	System.out.println("nonCompleteTasks=" +  objectivesResult.nonCompleteTasks);
				//System.out.println("penalty=" +  objectivesResult.penalty);

	    	//}

			//System.out.println(objectivesVector.clientTolerance);
	    	
		return objectivesVector;
			
	 }
	
	

	public static Objectives getAlpha(Double price, Integer clientId, String instanceFolder) {

		 DRASv1 problem = new DRASv1(price,
				 instanceFolder+"/"+instance_name,
				 instanceFolder+"/"+workload_name+clientId,
				 instanceFolder+"/"+referece_name);
	    	int R = problem.getR();
	    	int K = problem.getK();
	    	int W = problem.getW();

			steps_reduction= problem.getR() * Constants.server_prcs_number ;
	    	//steps_reduction=  100  ;
			//System.out.println("steps_reduction: " + steps_reduction);
			Objectives objectivesResult = new Objectives();
			objectivesResult.alpha = 0.0;
			objectivesResult.profit = 0.0;
			objectivesResult.loss = 0.0;
			objectivesResult.payment = 0.0;
			objectivesResult.reductionPercentage = 0.0;
			objectivesResult.nonCompleteTasks = 0.0;

			// obtaining reference
	    	List<Integer> variables = Utils.random(R*K, Integer.MAX_VALUE , Integer.MAX_VALUE ,654654);
		    int [][] s_cpu = Utils.zeros_matrix(K, problem.getRN()[clientId]);
		    int [][] s_mem = Utils.zeros_matrix(K, problem.getRN()[clientId]);
		    int [] assigned_task = Utils.zeros_vector(W);		    
		    int [] F =  HEU_HIGHEST_PENALTY_FIRST_WITH_CORES.schedule(Integer.MAX_VALUE,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
		    List<Integer> solution = Utils.getPowerSolution(clientId,problem, F, s_cpu, s_mem);
			int[] P_techo = Utils.zeros_vector(problem.getK());			
			for (int t=0;t<problem.getK();t++ ){
				 for(int r=0; r<problem.getRN()[clientId]; r++) {
					 P_techo[t] += solution.get(problem.getRN()[clientId] * t + r);
				 }
			 }
			Integer alpha_ref = Utils.max_abs(P_techo);	    
			if (Utils.testLevel() > 2) {
				System.out.println("alpha_ref: " + alpha_ref);
			}
		    //System.out.println("alpha_ref: " + alpha_ref);
			if (Utils.testLevel() > 1) {
				System.out.println("schedule ===============>>>>>>>>>");
				for (int r = 0;r<problem.getRN()[clientId];r++ ){
					 for (int k = 0;k<K;k++ ) {
						 System.out.print((s_cpu[k][r]+s_mem[k][r]) +", ");
					 }
					 System.out.println();
				}
			}
			//System.exit(0);
			Objectives objectivesInitial = Utils.evaluate(alpha_ref,clientId,problem, F, s_cpu, s_mem);

		    Double initialLoss = objectivesInitial.loss;
	
		    //System.out.println("initialLoss ===============>>>>>>>>>" + initialLoss);
		    
	    	for (int i = steps_reduction;i >= 0;i--) {
				//for (int i = 1;i<2+1;i++) {
	    		
	    		//double reductionPercentage = Double.valueOf(i)   / steps_reduction ;
	    		//int coresPerStep = Double.valueOf(Constants.server_prcs_number * problem.getRN()[clientId] * reductionPercentage).intValue();
				
	    		int coresPerStep = i;
	    		
	    		//System.out.println("reductionPercentage: " + reductionPercentage);
				//System.out.println("coresPerStep: " + coresPerStep);

	    		
	    		//if (Utils.testLevel() > 2) {
				//	System.out.println("coresPerStep: " + coresPerStep);
				//}	
	    		
		    	// infinite power
		    	variables = Utils.random(R*K, Integer.MAX_VALUE , Integer.MAX_VALUE ,654654);
		    	
		    	//FF_ID
			    s_cpu = Utils.zeros_matrix(K, problem.getRN()[clientId]);
			    s_mem = Utils.zeros_matrix(K, problem.getRN()[clientId]);
			    
			    assigned_task = Utils.zeros_vector(W);		    
			    F =  HEU_HIGHEST_PENALTY_FIRST_WITH_CORES.schedule(coresPerStep,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			    Objectives objectives = Utils.evaluate(alpha_ref,clientId,problem, F, s_cpu, s_mem);
				
			    //add initial loss to profit
			    objectives.profit = objectives.profit  + initialLoss;
			    
			    
			    
			    
			    //System.out.println("alpha_ref: " + coresPerStep);
			    
			    
				if (Utils.testLevel() > 2) {
					System.out.println("schedule ===============>>>>>>>>>");
					for (int r = 0;r<problem.getRN()[clientId];r++ ){
						 for (int k = 0;k<K;k++ ) {
							 System.out.print((s_cpu[k][r]+s_mem[k][r]) +", ");
						 }
						 System.out.println();
					}
				}
				
				//alpha must be less than alpha ref
				//System.out.println("profit=" +  objectives.profit);
				//System.out.println("alpha=" +  objectives.alpha);
				//System.out.println("loss=" +  objectives.loss);
				//System.out.println("--------");
				
				//se poda por cambio de direccion
//				Double profitDirection = null;
//				if (lastProfitDirection != null) {
//					profitDirection = lastProfit - objectives.profit;					
//					if (profitDirection * lastProfitDirection < 1 ) {
//						break;
//					}
//				}
//				lastProfitDirection = profitDirection;
//				lastProfit = objectives.profit;
				
//					System.out.println(
//							",coresPerStep=" + coresPerStep 
//						+",profit=" +  objectives.profit
//						+",profit 2=" +  (objectives.alpha * price - objectives.loss)
//						+",loss=" +  objectives.loss
//						+",alpha=" +  objectives.alpha
//						+",rel=" + objectives.alpha / objectives.loss
//						);

				
			//	if (lastProfit != null && lastProfit > objectives.profit) {
			 //   	System.out.println("break=");

			//		break;
			//	}
				
				
				
				if (objectives.profit > objectivesResult.profit ) {
					objectivesResult.alpha = objectives.alpha;
					objectivesResult.profit = objectives.profit;
					objectivesResult.loss =  objectives.loss;
					objectivesResult.payment =  objectives.payment;
					objectivesResult.reductionPercentage = coresPerStep*1.0;
					objectivesResult.nonCompleteTasks = objectives.nonCompleteTasks;
				}

	    	}

			
	    	if (Utils.testLevel() >= 1) {
		      	System.out.println("alpha=" +  objectivesResult.alpha);
		    	System.out.println("profit=" +  objectivesResult.profit);
				System.out.println("loss=" +  objectivesResult.loss);
				System.out.println("payment=" +  objectivesResult.payment);
				System.out.println("reductionPercentage=" +  objectivesResult.reductionPercentage);
				System.out.println("nonCompleteTasks=" +  objectivesResult.nonCompleteTasks);
	    	}

		return objectivesResult;
			
	 }

}
