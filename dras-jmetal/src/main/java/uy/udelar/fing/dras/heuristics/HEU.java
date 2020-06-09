package uy.udelar.fing.dras.heuristics;

import java.util.List;

import uy.udelar.fing.dras.problem.Constants;
import uy.udelar.fing.dras.problem.DRASv1;
import uy.udelar.fing.dras.utils.Utils;

public class HEU {
	
	
	
	/** AULILIAR METHODS*/

	public static Boolean isSuitable(DRASv1 problem,int i, int r, int currect_execution_time) {
		return currect_execution_time >= problem.getAT()[i];// && problem.getCT()[i][problem.getOwner()[r]] == 1;
		
	}

	public static Boolean isFullForTask(
			DRASv1 problem,int i,int r, 
			int k, 
			int task_duration, int task_steps,		
			int [][] server_usage_by_steps_cpu, 
			  int [][] server_usage_by_steps_mem,
			  List<Integer> power_assignation_by_steps
			
			) {

		  boolean isFull= false;						  
		  for (int u = 0;u < task_steps ;u++){
			  if (	  k+u >= problem.getK() || 
					  (server_usage_by_steps_cpu [k+u][r] + server_usage_by_steps_mem [k+u][r]) >= 
							  Constants.server_prcs_number
					  // se comenta porque no hay asignacion
					  ||
					  // TODO: power function
					  power_assignation_by_steps.get((( k + u) * problem.getR()) + r).intValue() 
					  < 
					  Utils.power((server_usage_by_steps_cpu[k+u][r] + (problem.getType()[i]==Constants.CPU_TYPE?1:0)), 
							  (server_usage_by_steps_mem[k+u][r] + (problem.getType()[i]==Constants.MEM_TYPE?1:0)), Constants.server_prcs_number)									  
				  
					  ) {
				  isFull= true;
				  break;
			  }}
		  return isFull;
		
	}
			

	public static void asignTaskToServer(DRASv1 problem,int i,int r, 
			int k, 
			int task_duration, int task_steps,		
			int [][] server_usage_by_steps_cpu, 
			int [][] server_usage_by_steps_mem,
			List<Integer> power_assignation_by_steps,
			int [] F,
			int [] assigned_task,
			int currect_execution_time,
			int  scheduling_step_duration
			
			
			) {
		 
		  for (int u = 0;u < task_steps;u++){
			  if (problem.getType()[i]==Constants.CPU_TYPE){
				  server_usage_by_steps_cpu [k+u][r] ++;
			  }else if (problem.getType()[i]==Constants.MEM_TYPE){
				  server_usage_by_steps_mem [k+u][r] ++;
			  }
			  assigned_task[i] = 1;
		  }
		  F[i] = currect_execution_time +  task_steps * scheduling_step_duration;
		  
		  
		
	}

}
