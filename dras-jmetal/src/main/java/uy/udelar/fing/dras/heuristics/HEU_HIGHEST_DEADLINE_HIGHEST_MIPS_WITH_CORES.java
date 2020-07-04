package uy.udelar.fing.dras.heuristics;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import uy.udelar.fing.dras.problem.Constants;
import uy.udelar.fing.dras.problem.DRASv1;
import uy.udelar.fing.dras.utils.Utils;

public class HEU_HIGHEST_DEADLINE_HIGHEST_MIPS_WITH_CORES extends HEU {
	// ORDER BY ID
	public static int [] schedule(
			Integer allowCoresPerStep,
			Integer clientId,
			DRASv1 problem,
			List<Integer> power_assignation_by_steps, int [][] server_usage_by_steps_cpu, 
			  int [][] server_usage_by_steps_mem, int [] assigned_task) {	  
				
		  int [] task_sorted_by_at = Utils.sort_by_max(problem.getD(),problem.getMIPS());
		
		  int scheduling_step_duration = problem.getScheduling_duration() / problem.getK();
		  int task_duration,task_steps,currect_execution_time = -1;
		  // task completed time
		  int [] F = Utils.not_ones_vector(problem.getW());
		  // for each steps
		  for (int k = 0 ; k < problem.getK() ;k++ ) {
			  
			  
			  int allowCoresPerStepAll =  allowCoresPerStep;

				//less remainings tasks
				for (int r = 0 ; r < problem.getRN()[clientId] ; r++ ) {
					allowCoresPerStepAll = allowCoresPerStepAll - server_usage_by_steps_cpu[k][r] - server_usage_by_steps_mem[k][r];
				}

			  currect_execution_time = k * scheduling_step_duration;
			  //for (int i = 0; i < problem.getW() ; i++) {
				  for (int i :Arrays.stream(task_sorted_by_at).boxed().collect(Collectors.toList())) {
	


				// for each servers
				  for (int r = 0 ; r < problem.getRN()[clientId] ; r++ ) {
					  
					  
						  if  (isSuitable(problem,i,r,currect_execution_time) && assigned_task[i] == 0) {
							  task_duration = problem.getMIPS()[i] / Constants.server_mips;
							  task_steps = (task_duration / scheduling_step_duration) +1;
							  
							  
							  if (allowCoresPerStepAll> 0 &&
									  !isFullForTask(problem,i, r, k, task_duration, task_steps, server_usage_by_steps_cpu, 
									  server_usage_by_steps_mem, power_assignation_by_steps
									  )) {		
								  
								  asignTaskToServer(problem,i, r, k, task_duration, task_steps, server_usage_by_steps_cpu, 
										  server_usage_by_steps_mem, power_assignation_by_steps, F, assigned_task, 
										  currect_execution_time, scheduling_step_duration);
								  
								  //task already assigned
								  allowCoresPerStepAll--;
								  break;
							  }
						  }
					  }
			  	}
			  }	 
		  return F;
		}
	
	
	
	

}
