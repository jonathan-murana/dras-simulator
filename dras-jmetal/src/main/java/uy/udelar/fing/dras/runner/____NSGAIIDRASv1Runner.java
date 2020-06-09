package uy.udelar.fing.dras.runner;


import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import uy.udelar.fing.dras.problem.DRASv1;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Class for configuring and running the NSGA-II algorithm (integer encoding)
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class ____NSGAIIDRASv1Runner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws org.uma.jmetal.util.JMetalException
   * @throws java.io.IOException
   * @throws SecurityException
   * @throws ClassNotFoundException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.NSGAIIIntegerRunner problemName [referenceFront]
   */
	
	
	public static String instance_name = "instances/v1/ins.txt";
	public static String workload_name = "instances/v1/w.txt";
	public static String referece_name = "instances/v1/ref.txt";
 	public static String instance_folder  = "instances/example1";

	
  public static void main(String[] args) throws FileNotFoundException {
    
	  if (args.length==3) {
			instance_name = args[0];
			workload_name = args[1];
			referece_name = args[2];
	}  
	  
	  
	Problem<IntegerSolution> problem;
    Algorithm<List<IntegerSolution>> algorithm;
    CrossoverOperator<IntegerSolution> crossover;
    MutationOperator<IntegerSolution> mutation;
    SelectionOperator<List<IntegerSolution>, IntegerSolution> selection;
    String problemName ;
    String referenceParetoFront = "" ;
    problem = new DRASv1(3.0,instance_name,workload_name,referece_name);
    double crossoverProbability = 0.9 ;
    double crossoverDistributionIndex = 20.0 ;
    crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex) ;
    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
    double mutationDistributionIndex = 20.0 ;
    mutation = new IntegerPolynomialMutation(mutationProbability, mutationDistributionIndex) ;
    selection = new BinaryTournamentSelection<IntegerSolution>() ;
    int populationSize = 500 ;
    algorithm = new NSGAIIBuilder<IntegerSolution>(problem, crossover, mutation, populationSize)
            .setSelectionOperator(selection)
            .setMaxEvaluations(50000)
            .build() ;
    
    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

    List<IntegerSolution> population = algorithm.getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;
 
    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront) ;
    }
  }
  
  public static void printFinalSolutionSet(List<? extends Solution<?>> population) {

	    new SolutionListOutput(population)
	        .setSeparator("\t")
	        .setVarFileOutputContext(new DefaultFileOutputContext("NSGAIIDRASv1_VAR"))
	        .setFunFileOutputContext(new DefaultFileOutputContext("NSGAIIDRASv1_FUN.tsv"))
	        .print();

	    JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
	    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
	    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
	  }
  
  
//  
//  public static Double getAlpha(Integer price, Integer clientId, String instanceFolder) {
//		 
//		
//		
//		 DRASv1 problem = new DRASv1(price,
//				 instance_folder+"/"+instance_name,
//				 instance_folder+"/"+workload_name+clientId,
//				 instance_folder+"/"+referece_name);
//	    	int R = problem.getR();
//	    	int K = problem.getK();
//	    	int W = problem.getW();
//	    	
//	    	// infinite power
//	    	List<Integer> variables = Utils.random(R*K, Integer.MAX_VALUE , Integer.MAX_VALUE ,654654);
//	    	
//	    	//FF_ID
//		    int [][] s_cpu = Utils.zeros_matrix(K, problem.getRN()[clientId]);
//		    int [][] s_mem = Utils.zeros_matrix(K, problem.getRN()[clientId]);
//		    
//		    int [] assigned_task = Utils.zeros_vector(W);		    
//		    
//		    Algorithm<List<IntegerSolution>> algorithm;
//		    CrossoverOperator<IntegerSolution> crossover;
//		    MutationOperator<IntegerSolution> mutation;
//		    SelectionOperator<List<IntegerSolution>, IntegerSolution> selection;
//		    String problemName ;
//		    String referenceParetoFront = "" ;
//		    problem = new DRASv1(3,instance_name,workload_name,referece_name);
//		    double crossoverProbability = 0.9 ;
//		    double crossoverDistributionIndex = 20.0 ;
//		    crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex) ;
//		    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
//		    double mutationDistributionIndex = 20.0 ;
//		    mutation = new IntegerPolynomialMutation(mutationProbability, mutationDistributionIndex) ;
//		    selection = new BinaryTournamentSelection<IntegerSolution>() ;
//		    int populationSize = 500 ;
//		    algorithm = new NSGAIIBuilder<IntegerSolution>(problem, crossover, mutation, populationSize)
//		            .setSelectionOperator(selection)
//		            .setMaxEvaluations(50000)
//		            .build() ;
//		    
//		    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
//		            .execute() ;
//
//		    List<IntegerSolution> population = algorithm.getResult() ;
//		    long computingTime = algorithmRunner.getComputingTime() ;
//		 
//		    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
//		    
//		    
//		    
//		    //int [] F =  HEU_FF_ID.schedule(problem,variables,s_cpu,s_mem,assigned_task);	    	
//			//Objectives objectives = TestUtils.evaluate(clientId,problem, F, s_cpu, s_mem);
//			
////			if (TestUtils.testLevel() > 2) {
////				System.out.println("schedule ===============>>>>>>>>>");
////				for (int r = 0;r<problem.getRN()[clientId];r++ ){
////					 for (int k = 0;k<K;k++ ) {
////						 System.out.print((s_cpu[k][r]+s_mem[k][r]) +", ");
////					 }
////					 System.out.println();
////				}
////			}	
//			
//			Objectives objectives = new Objectives();
//			return objectives.profit > 0.0? objectives.alpha:0;
//			
//	 }
	
  
  
  
  
}
