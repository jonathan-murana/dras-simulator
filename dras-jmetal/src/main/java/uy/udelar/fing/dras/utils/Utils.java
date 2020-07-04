package uy.udelar.fing.dras.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import uy.udelar.fing.dras.problem.Config;
import uy.udelar.fing.dras.problem.Constants;
import uy.udelar.fing.dras.problem.DRASv1;

public class Utils {

	
	// TODO: improve performance
	public static double dist(double x1,double y1,double x2,double y2) {		
		return Math.sqrt((x1 - x2)*(x1 - x2)  + (y1 - y2)*(y1 - y2));
	}
	
	// TODO: improve performance
	public static int sumarize(int[] a) {		
		int sum = 0;
		for (int i = 0; i< a.length;i++) {
			sum += a[i];
		}
		return sum;		
	}
	
	// TODO: improve performance
	public static double sumarized(double[] a) {		
		double sum = 0;
		for (int i = 0; i< a.length;i++) {
			sum += a[i];
		}
		return sum;		
	}
	// TODO: improve performance
	public static Double sumarized(Double[] a) {		
		double sum = 0;
		for (int i = 0; i< a.length;i++) {
			sum += a[i];
		}
		return sum;		
	}
	
	public static int[] subtract(int[] a, int[] b) {		
		int[] c = new int[a.length];;
		for (int i = 0; i< a.length;i++) {
			c[i] = a[i] - b[i];
		}
		return c;		
	}
	
	
	// TODO: improve performance
	public static int max(int[] a) {		
		int max = Integer.MIN_VALUE;
		for (int i = 0; i< a.length;i++) {
			max = a[i] > max? a[i]:max;
		}
		return max;		
	}
	
	// TODO: improve performance
	public static double max(double[] a) {		
		Double max = Double.MIN_VALUE;
		for (int i = 0; i< a.length;i++) {
			max = a[i] > max? a[i]:max;
		}
		return max;		
	}
	
	// TODO: improve performance
	public static double min(double[] a) {		
		Double min = Double.MAX_VALUE;
		for (int i = 0; i< a.length;i++) {
			min = a[i] < min? a[i]:min;
		}
		return min;		
	}
	
	// TODO: improve performance
	public static int min_index(Double[] a) {		
		Double min = Double.MAX_VALUE;
		int min_index = -1;

		for (int i = 0; i< a.length;i++) {
			if (a[i] < min) {
				min = a[i];
				min_index =i;
			}
			
			
		}
		return min_index;		
	}
	
	// TODO: improve performance
	public static int max_abs(int[] a) {		
		int max = Integer.MIN_VALUE;
		for (int i = 0; i< a.length;i++) {
			max = Math.abs(a[i]) > max? a[i]:max;
		}
		return max;		
	}
	
	// TODO: improve performance
	public static int min_abs(int[] a) {		
		int min = Integer.MAX_VALUE;
		for (int i = 0; i< a.length;i++) {
			min = Math.abs(a[i]) < min? a[i] : min;
		}
		return min;		
	}
	
	public static int[] zeros_vector(int n) {		
		
		  int [] a = new int[n];
		  for (int i=0;i<n;i++) {
			  a[i] = 0;
		  }	
		  return a;
		  
	}
	
	public static double[] double_zeros_vector(int n) {		
		
		double [] a = new double[n];
		  for (int i=0;i<n;i++) {
			  a[i] = 0;
		  }	
		  return a;
		  
	}
	
	public static int[] not_ones_vector(int n) {		
		
		  int [] a = new int[n];
		  for (int i=0;i<n;i++) {
			  a[i] = -1;
		  }	
		  return a;
		  
	}
	
	public static int[][] zeros_matrix(int m, int n) {		
		
		  int [][] a = new int[m][n];
		  for (int i=0;i<m;i++) {
			  for (int j=0;j<n;j++) {
			  a[i][j]  = 0;
			  }
		  }	
		  return a;
		  
	}
	
	public static int[][] not_ones_matrix(int m, int n) {		
		
		  int [][] a = new int[m][n];
		  for (int i=0;i<m;i++) {
			  for (int j=0;j<n;j++) {
			  a[i][j]  = -1;
			  }
		  }	
		  return a;
		  
	}
	
	
	

	public static ArrayList<Integer>  random(int n, int min, int max, int seed) {		

		ArrayList<Integer> arrayRandom = new ArrayList<Integer>(n);
	
		Random rand = new Random();
		rand.setSeed(seed);
		for (int i=0; i<n; i++)
		{
		    Integer r = rand.nextInt((max - min) + 1) + min;
		    arrayRandom.add(r);
		}
		return  arrayRandom;
	}

	
	public static int[] sort_by_max(int[] w )  {		
		
		int[] ints = IntStream.range(0, w.length).toArray();
		List<Integer> list = Arrays.stream(ints).boxed().collect(Collectors.toList());
		
		
		List<Integer> sorted = list.stream().sorted( (b,a)-> {  return Integer.valueOf(w[a])
			.compareTo(Integer.valueOf(w[b])); }).collect(Collectors.toList());				
		
		return sorted.stream().mapToInt(i->i).toArray();
	
	}
	
	public static int[] sort_by_max(int[] w ,int[] y )  {		
		
		int[] ints = IntStream.range(0, w.length).toArray();
		List<Integer> list = Arrays.stream(ints).boxed().collect(Collectors.toList());
		
		
		List<Integer> sorted = list.stream().sorted( (b,a)-> {  
			int comp=
			Integer.valueOf(w[a])
			.compareTo(Integer.valueOf(w[b])
					);
			if (comp== 0) {
				return Integer.valueOf(y[a])
						.compareTo(Integer.valueOf(y[b]));
			}else {
				return comp;
			}
			}
		).collect(Collectors.toList());				
		
		return sorted.stream().mapToInt(i->i).toArray();
	
	}
	
	
	public static int[] sort_by_min(int[] w)  {		
		
		int[] ints = IntStream.range(0, w.length).toArray();
		List<Integer> list = Arrays.stream(ints).boxed().collect(Collectors.toList());
		
		
		List<Integer> sorted = list.stream().sorted( (a,b)-> {  return Integer.valueOf(w[a])
			.compareTo(Integer.valueOf(w[b])); }).collect(Collectors.toList());				
		
		return sorted.stream().mapToInt(i->i).toArray();
	
	}
	
	//TODO: mover al problema
	public static Objectives evaluate(Integer alphaRef, Integer clientId,DRASv1 problem, int [] F, int [][] s_cpu, int [][] s_mem) {
 		
		   // ref
		   for (int i = 0;i<problem.getK();i++) {
			   problem.getP()[i] = alphaRef;
		   }
			
		
    	  List<Integer> solution = getPowerSolution(clientId,problem, F, s_cpu, s_mem);
    	
		  int[] P_techo = Utils.zeros_vector(problem.getK());
		  for (int t=0;t<problem.getK();t++ ){

			  for(int r=0; r<problem.getRN()[clientId]; r++) {
			  
				  P_techo[t] += solution.get(problem.getRN()[clientId] * t + r);
			  }
		  }
		  
		  int Z_alfa =Utils.min_abs(Utils.subtract(problem.getP(), P_techo));
		  
		  double Z_loss = 0;
		  double non_complete_tasks = 0;
		  double violatedTime = 0;

		  for (int i = 0;i<problem.getW();i++ ){
				   if ( F[i] == -1 || (F[i] > (problem.getAT()[i] + problem.getD()[i])) )	{
					   Z_loss += (problem.getLoss()[i] * problem.getTolerance()[i]);
					   non_complete_tasks += 1;
					   if (F[i] == -1 ) {
						   
						   double fin = problem.getAT()[i] + problem.getD()[i];
						   
						   // si el deadline era en T
						   if (fin < problem.getScheduling_duration()) {
							   violatedTime += problem.getScheduling_duration() - fin +
									    problem.getMIPS()[i]/ Constants.server_mips;
									   
									   							   
						   }else {
							// si el deadline NO era en T
							   // ?
						   }
						  						   
					   }else {
						   
						   // si el F[i] era en T
						   double absDeadline = problem.getAT()[i] + problem.getD()[i];
							   violatedTime += (F[i] - absDeadline);						   
					   }
					   
				   }
		  }
		  
		  Objectives objectives = new Objectives();
		  objectives.alpha = Integer.valueOf(Z_alfa).doubleValue();
		  objectives.profit = (Z_alfa * problem.getPrice() - Z_loss);
		  objectives.loss = Z_loss;
		  objectives.payment = Z_alfa * problem.getPrice();
		  objectives.nonCompleteTasks  =non_complete_tasks;
		  objectives.violatedTime = violatedTime;


		return objectives;
    }
 
     public static void print(Integer clientId, DRASv1 problem, int [] F, int [][] s_cpu, int [][] s_mem) {
  		 		
     	List<Integer> solution = getPowerSolution(clientId, problem, F, s_cpu, s_mem);

    	System.out.println("power function  (server x interval) =======");
    	 
    	for (int r = 0;r<problem.getR();r++ ){
 			System.out.print("s" + r +" -> ");
    		for (int k = 0;k<problem.getK();k++ ) {
    			System.out.print(solution.get(k * problem.getR() + r)+", ");
    	 	}
    	 	System.out.println();
    	}
    	System.out.println();
    	 
    	System.out.println("completion time (task1, task2 ... taskW) =======");
 		for (int i = 0;i<problem.getW();i++ ){
 			 System.out.print(F[i]+", ");
 		}
 		System.out.println();
 		System.out.println();
 		
     	System.out.println("number of active tasks (server x interval)  =======");
 		for (int r = 0;r<problem.getR();r++ ){
 			System.out.print("s" + r +" -> ");
 			 for (int k = 0;k<problem.getK();k++ ) {
 				 if (testLevel() < 2) {
 					System.out.print((s_cpu[k][r]+s_mem[k][r]) +", ");
 				 }else { 				 
 					System.out.print((s_cpu[k][r]+s_mem[k][r]) + "("+ s_cpu[k][r] + "," + s_mem[k][r]+"), ");
 				 }
 			 }
 			 System.out.println();
 		}
     }
     
     
     
     
     
     public static List<Integer> getPowerSolution(Integer clientId, DRASv1 problem, int [] F, int [][] s_cpu, int [][] s_mem) {
  		
     	List<Integer> solution = new ArrayList<>();
     	for (int k = 0;k<problem.getK();k++ ) {
     	for (int r = 0;r<problem.getRN()[clientId];r++ ){
 			 
 				 // TODO: double to int!!
 				 solution.add((int)Utils.power(s_cpu[k][r], s_mem[k][r], Constants.server_prcs_number));
 			 }
 		} 		
 		return solution;

     }
     
     public static void startPrintingResults(DRASv1 problem, String name) {
   		
    	 System.out.println("========================================================");
    	 System.out.println("======RESULTS for "+ name +"");
    	 System.out.println("========================================================");
		 System.out.println("workload file = "+ problem.getWorkloadFile());
		 System.out.println("number of servers = "+ problem.getR());
		 System.out.println("number of tasks = "+ problem.getW());		  
		  
		 System.out.println();

      }
     
     
     
     
     public static long testLevel() {    		
    	 return Config.verbose.chars().filter(ch -> ch == 'v').count();

      }

     
     
     public static void printResultToFile(String filename, String content) {
    	 PrintWriter out = null;
		try {
			out = new PrintWriter(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         out.println(content);
         out.close();
      }
     
     public static double power(int ul_cpu, int ul_mem, int prcs_number) {		
 		
 		double x = (Double.valueOf(ul_cpu) / Double.valueOf(prcs_number)) * 100;
 		double y = (Double.valueOf(ul_mem) / Double.valueOf(prcs_number)) * 100;
 		
 		if (x==0 && y==0) {
 			return 57.0;
 		} else {
 			double aux = 116.09724 + 0.73349 * x + 1.72632 * y + 0.00786 * x * y + 0.00043 * x * x - 0.00388 * y * y;
		
 			return aux;
 		}
 	}
	
}
