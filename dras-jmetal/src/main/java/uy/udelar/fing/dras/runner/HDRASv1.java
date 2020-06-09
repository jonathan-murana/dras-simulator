package uy.udelar.fing.dras.runner;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.google.common.base.Joiner;

import uy.udelar.fing.dras.heuristics.____HEU_FF_AT;
import uy.udelar.fing.dras.heuristics.HEU_HIGHEST_PENALTY_FIRST_WITH_CORES;

import uy.udelar.fing.dras.problem.Config;
import uy.udelar.fing.dras.problem.DRASv1;
import uy.udelar.fing.dras.utils.Objectives;
import uy.udelar.fing.dras.utils.Utils;



/**
 * Class for configuring and running the NSGA-II algorithm (integer encoding)
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class HDRASv1 {
 
	

	public static final String problem_name = "DRASv1";

	public static String instance_name = "instances/v1/ins.txt";
	public static String workload_name = "instances/v1/w.txt";
	public static String referece_name = "instances/v1/ref.txt";

	
	public static void main (String [ ] args) {
		if (args.length==3) {
			instance_name = args[0];
			workload_name = args[1];
			referece_name = args[2];
		}

		run();
      }
	
	 public static void run() {
//		 DRASv1 problem = new DRASv1(3,instance_name,workload_name,referece_name);
//	    	int R = problem.getR();
//	    	int K = problem.getK();
//	    	int W = problem.getW();
//	    	
//	    	// infinite power
//	    	List<Integer> variables = Utils.random(R*K, Integer.MAX_VALUE , Integer.MAX_VALUE ,654654);
//	 		
//	    	
//	    	//FF_ID
//		    int [][] s_cpu = Utils.zeros_matrix(K, R);
//		    int [][] s_mem = Utils.zeros_matrix(K, R);
//		    int [] assigned_task = Utils.zeros_vector(W);		    
//		    int [] F =  HEU_FF_ID.schedule(problem,variables,s_cpu,s_mem,assigned_task);
//	    	
//			Objectives objectives = TestUtils.evaluate(problem, F, s_cpu, s_mem);
//			TestUtils.printResultToFile(problem_name + "_FUN_" + "FF_ID", 
//					"" + objectives.profit + Config.sep +objectives.alpha
//					);
//
// 			TestUtils.printResultToFile(problem_name + "_VAR_ENERGY_" + "FF_ID", 
// 					Joiner.on(' ').join(TestUtils.getPowerSolution(problem, F, s_cpu, s_mem))
//					);
//			
//			
//			//FF_AT
//			s_cpu = Utils.zeros_matrix(K, R);
//			s_mem = Utils.zeros_matrix(K, R);
//			assigned_task = Utils.zeros_vector(W);		    
//			F =  HEU_FF_AT.schedule(problem,variables,s_cpu,s_mem,assigned_task);
//			objectives = TestUtils.evaluate(problem, F, s_cpu, s_mem);
//			TestUtils.printResultToFile(problem_name +"_FUN_" + "FF_AT", 
//						"" + objectives.profit + Config.sep +objectives.alpha
//						);
//			
//			TestUtils.printResultToFile(problem_name + "_VAR_ENERGY_" + "FF_AT", 
// 					Joiner.on(' ').join(TestUtils.getPowerSolution(problem, F, s_cpu, s_mem))
//					);
//			
//			//RR_ID
//			s_cpu = Utils.zeros_matrix(K, R);
//			s_mem = Utils.zeros_matrix(K, R);
//			assigned_task = Utils.zeros_vector(W);		    
//			F =  HEU_RR_ID.schedule(problem,variables,s_cpu,s_mem,assigned_task);
//			objectives = TestUtils.evaluate(problem, F, s_cpu, s_mem);
//			TestUtils.printResultToFile(problem_name +"_FUN_" + "RR_ID", 
//						"" + objectives.profit + Config.sep +objectives.alpha
//						);
//			
//			TestUtils.printResultToFile(problem_name + "_VAR_ENERGY_" + "RR_ID", 
// 					Joiner.on(' ').join(TestUtils.getPowerSolution(problem, F, s_cpu, s_mem))
//					);
//			
//			//RR_AT
//			s_cpu = Utils.zeros_matrix(K, R);
//			s_mem = Utils.zeros_matrix(K, R);
//			assigned_task = Utils.zeros_vector(W);		    
//			F =  HEU_RR_AT.schedule(problem,variables,s_cpu,s_mem,assigned_task);
//			objectives = TestUtils.evaluate(problem, F, s_cpu, s_mem);
//			TestUtils.printResultToFile(problem_name +"_FUN_" +  "RR_AT", 
//						"" + objectives.profit + Config.sep +objectives.alpha
//						);
//			
//			TestUtils.printResultToFile(problem_name + "_VAR_ENERGY_" + "RR_AT", 
// 					Joiner.on(' ').join(TestUtils.getPowerSolution(problem, F, s_cpu, s_mem))
//					);
			
			
	 }
	
}
