package uy.udelar.fing.dras.runner;

import java.util.List;

import uy.udelar.fing.dras.heuristics.____HEU_FF_ID;
import uy.udelar.fing.dras.problem.DRASv1;
import uy.udelar.fing.dras.utils.Objectives;
import uy.udelar.fing.dras.utils.Utils;

public class ____HighPenaltyFirstRunner {

	public static final String problem_name = "DRASv1";

	private final static String instance_name = "ins";
	private final static String workload_name = "w";
	private final static String referece_name = "ref";
 	public static String instance_folder  = "instances/example1";
 	public static int steps_reduction= 1;
	

	public static Objectives getObjectives(Double price, Integer clientId, String instanceFolder) {

		 DRASv1 problem = new DRASv1(price,
				 instanceFolder+"/"+instance_name,
				 instanceFolder+"/"+workload_name+clientId,
				 instanceFolder+"/"+referece_name);
	    	int R = problem.getR();
	    	int K = problem.getK();
	    	int W = problem.getW();

	    	List<Integer> variables = Utils.random(R*K, Integer.MAX_VALUE , Integer.MAX_VALUE ,654654);
		    int [][] s_cpu = Utils.zeros_matrix(K, problem.getRN()[clientId]);
		    int [][] s_mem = Utils.zeros_matrix(K, problem.getRN()[clientId]);
		    int [] assigned_task = Utils.zeros_vector(W);		
	    	int [] F = ____HEU_FF_ID.schedule(
	    			clientId,problem,variables,s_cpu,s_mem,assigned_task);	    	
		    Objectives objectives = Utils.evaluate(0,clientId,problem, F, s_cpu, s_mem);
			
		return objectives;
			
	 }

}
