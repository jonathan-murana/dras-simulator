package uy.udelar.fing.dras.runner;

import java.util.List;

import uy.udelar.fing.dras.heuristics.HEU_HIGHEST_PENALTY_FIRST_WITH_CORES;
import uy.udelar.fing.dras.problem.DRASv1;
import uy.udelar.fing.dras.utils.Objectives;
import uy.udelar.fing.dras.utils.Utils;



/**
 * Class for configuring and running the NSGA-II algorithm (integer encoding)
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class ____HeuAERunner {
 
	

	public static final String problem_name = "DRASv1";

	private final static String instance_name = "ins";
	private final static String workload_name = "w";
	private final static String referece_name = "ref";

 	public static String instance_folder  = "instances/example1";
	
	public static Double getAlpha(Double price, Integer clientId, String instanceFolder) {
		 
		
		
		 DRASv1 problem = new DRASv1(price,
				 instance_folder+"/"+instance_name,
				 instance_folder+"/"+workload_name+clientId,
				 instance_folder+"/"+referece_name);
	    	int R = problem.getR();
	    	int K = problem.getK();
	    	int W = problem.getW();
	    	
	    	// infinite power
	    	List<Integer> variables = Utils.random(R*K, Integer.MAX_VALUE , Integer.MAX_VALUE ,654654);
	    	
	    	//FF_ID
		    int [][] s_cpu = Utils.zeros_matrix(K, problem.getRN()[clientId]);
		    int [][] s_mem = Utils.zeros_matrix(K, problem.getRN()[clientId]);
		    
		    int [] assigned_task = Utils.zeros_vector(W);		    
		    int [] F =  HEU_HIGHEST_PENALTY_FIRST_WITH_CORES.schedule(Integer.MAX_VALUE,clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
			Objectives objectives = Utils.evaluate(0,clientId,problem, F, s_cpu, s_mem);
			
			if (Utils.testLevel() > 2) {
				System.out.println("schedule ===============>>>>>>>>>");
				for (int r = 0;r<problem.getRN()[clientId];r++ ){
					 for (int k = 0;k<K;k++ ) {
						 System.out.print((s_cpu[k][r]+s_mem[k][r]) +", ");
					 }
					 System.out.println();
				}
			}	
			
			
 			return objectives.profit > 0.0? objectives.alpha:0;
			
	 }
	
}
