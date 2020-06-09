package uy.udelar.fing.dras;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import uy.udelar.fing.dras.runner.HeuDFVSRunner;
import uy.udelar.fing.dras.utils.Objectives;
import uy.udelar.fing.dras.utils.ObjectivesVector;
import uy.udelar.fing.dras.utils.Utils;

public class DatacenterController {

	private final static String instance_name = "ins";

	public static String instance_folder = "instances/random";
	public static Integer clients_number;
	public static Integer D;
	public static double alpha;
	public static double p ;



	public static void main(String[] args) {
		if (args.length != 4 ) {
			System.out.println("Need parameter: instance D motor p !!!!!!!!!!!!!!!!!!!");
			System.exit(1);

		
		}	
		else {
			instance_folder = args[0];
			D = Integer.valueOf(args[1]);
			alpha = Double.valueOf(args[2]);
			p = Double.valueOf(args[3]);
		
		try {
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	public static void run() throws IOException {

		
		
		
		
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(instance_folder + "/" + instance_name));
		String line = reader.readLine();
		clients_number = Integer.valueOf(line.split(" ")[0]);
		reader.close();
		

		System.out.println("There are " + clients_number + " clients");
		System.out.println("D " + D );
		System.out.println("motor" + alpha );
		System.out.println("p" + p );


		int N = clients_number;

		//default local heuristic (penalty)
		int local_heuristic_id = 1;

		
		ObjectivesVector [] function = new ObjectivesVector[clients_number];;
		for (int c = 0;c<clients_number;c++) {
			function[c]  = HeuDFVSRunner.getObjetctivesVector(Integer.valueOf(c), instance_folder,local_heuristic_id);
		}
		
		
		//int D = 3000;
		double[] a1 = new double[N];
		// init a1
		for (int i = 0;i<N-1; a1[i]= Math.random(),i++); 		
		a1[N-1] = -0.4;
		double a2 = 0;
		double[] b = new double[N];
		double[] c = new double[N];
		for (int i = 0;i<N; b[i]= 0,i++);
		double[] s_alm = new double[N];
		for (int i = 0;i<N; s_alm[i]= 0,i++); 	
		double[] b_alm = new double[N];
		for (int i = 0;i<N; b_alm[i]= 0,i++); 	
		double y=0;
		double[] s = new double[N];
		for (int i = 0;i<N; s[i]= D - b[i] / p,i++);

		double err=1;
		int iter=1;
		
		File fout = new File("output/out_negociation_" + instance_folder.split("/")[1]);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		Double suma = 0.0;
		
		int iteration = 0;
		
		while (err>=0.0050 && iteration < 1000) {
			iteration++;
			
			//double cost = 0.0;
			for (int i = 0; i < N; i++) {
				//[s(i),b(i)] = Cost_s(p,a1(i),a2);
				//Objectives objectives = HeuDFVSFirstFitIdRunner.getAlpha(p, i, instance_folder);
				//System.out.println("alpha=" +  objectives.alpha);
			    
				s[i] = poderar(function[i],p);
				c[i] = poderarC(function[i],p);
				
//				s[i] = objectives.alpha;
//				c[i] = objectives.loss;
				
				// parametro auxiliar suplir function
				b[i]= (D-s[i])*p;
			}
			
		double dcCost  = (Utils.sumarized(s) * p) + ((D - Utils.sumarized(s)) * alpha);

		double socialCost = (Utils.sumarized(c)) + ((D - Utils.sumarized(s)) * alpha);

		
	    y = Math.sqrt(Utils.sumarized(b)*N*D/alpha) - (N-1)*D;
	    System.out.println("y=" + y);
	    
	    if (y <0) {
	    	y=0;
	    }
	    p = Utils.sumarized(b)/((N-1)*D+y);
	    err = Math.abs((y+Utils.sumarized(s)-D)/D);
	    suma = Utils.sumarized(s);
	    bw.write(iteration + " " + p + " " + Utils.sumarized(s) + " " + y +" " + err + " " + dcCost + " " + socialCost);
        bw.newLine();
		
        Double sum = 0.0;
        System.out.println("p=" +  p);
        for (int c2 = 0; c2<N;c2++) {
        	System.out.print("," +  s[c2]);
        	sum += s[c2];
        }
        System.out.println("------- " + sum);

		}
		 bw.close();
	}
	
	
	
	public static double poderar(ObjectivesVector function, double price)  {
		
		Double maxProfit = -1 * Double.valueOf( Double.MAX_VALUE);
		int index = 0;
		Double bestReduction = 0.0; 
		Double bestLoss = 0.0; 
		Double bestNonCompleteTasks = 0.0; 

		for (int j = 0;j<function.reductionVector.length;j++){
			
			
			
			Double profit = function.reductionVector[j] * Double.valueOf(p) -  function.lossVector[j] 
					+  function.lossVector[function.reductionVector.length-1];
			//System.out.println("maxProfit" + maxProfit);
			//System.out.println("r " + objectives.reductionVector[j]);
			
			if (maxProfit<= profit){
				maxProfit = profit;
				index = j;
				bestReduction = function.reductionVector[j]; 
				bestLoss = function.lossVector[j]; 
				bestNonCompleteTasks =  function.nonCompleteTasks[j]; 
				
			}
		}
		
		return bestReduction;
		
		
	}

	public static double poderarC(ObjectivesVector function, double price)  {
		
		Double maxProfit = -1 * Double.valueOf( Double.MAX_VALUE);
		int index = 0;
		Double bestReduction = 0.0; 
		Double bestLoss = 0.0; 
		Double bestNonCompleteTasks = 0.0; 

		for (int j = 0;j<function.reductionVector.length;j++){
			
			
			
			Double profit = function.reductionVector[j] * Double.valueOf(p) -  function.lossVector[j] 
					+  function.lossVector[function.reductionVector.length-1];
			//System.out.println("maxProfit" + maxProfit);
			//System.out.println("r " + objectives.reductionVector[j]);
			
			if (maxProfit<= profit){
				maxProfit = profit;
				index = j;
				bestReduction = function.reductionVector[j]; 
				bestLoss = function.lossVector[j]; 
				bestNonCompleteTasks =  function.nonCompleteTasks[j]; 
				
			}
		}
		
		return bestLoss;
		
		
	}
	
	public static void run2() throws IOException {

		System.out.println("Reading clients number...");
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(instance_folder + "/" + instance_name));
		String line = reader.readLine();
		clients_number = Integer.valueOf(line.split(" ")[0]);
		reader.close();

		System.out.println("There are " + clients_number + " clients");
		System.out.println("Start algorithm...");

		
		File fout = new File("output/out_" + instance_name);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	
			for (int i = 0; i < 200; i = i+2) {
				Objectives objectives = HeuDFVSRunner.getAlpha(i * 1.0, 0, instance_folder);
				System.out.println(i + "," + objectives.alpha );
	
				bw.write(i + "," + objectives.alpha );
	            bw.newLine();
				
				
			}
	     bw.close();

}

	
	
}
