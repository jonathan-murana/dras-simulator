package uy.udelar.fing.dras;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ArrayUtils;
import org.paukov.combinatorics3.Generator;


import uy.udelar.fing.dras.problem.Constants;
import uy.udelar.fing.dras.runner.HeuDFVSRunner;
import uy.udelar.fing.dras.utils.Objectives;
import uy.udelar.fing.dras.utils.ObjectivesVector;
import uy.udelar.fing.dras.utils.Utils;

public class DatacenterControllerDiscrete2 {

	private final static String instance_name = "ins";

	public static String ro_instance_folder = "instances/random";
	public static Integer ro_clients_number;
	public static Integer ro_servers_number;
	public static Integer ro_D;
	public static double ro_gasoil;
	public static double ro_p ;
	public static String ro_local_heuristic;
	public static Integer ro_discrete_points_number;
	public static double ro_step_size;
	public static Integer ro_area;
	public static Integer ro_COP;



	public static void main(String[] args) {
		if (args.length != 7 ) {
			System.out.println("Need parameter: instance D motor p local_heuristic global_heuristic discrete_points_number step_size !!!!!!!!!!!!!!!!!!!");
			System.exit(1);
		}	
		else {
			ro_instance_folder = args[0];
			ro_D = Integer.valueOf(args[1]);
			ro_gasoil = Double.valueOf(args[2]);
			ro_p = Double.valueOf(args[3]);
			ro_local_heuristic = args[4];
			ro_discrete_points_number = Integer.valueOf(args[5]);
			ro_step_size = Double.valueOf(args[6]);
		
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
		reader = new BufferedReader(new FileReader(ro_instance_folder + "/" + instance_name));
		String line = reader.readLine();
		ro_clients_number = Integer.valueOf(line.split(" ")[0]);
		ro_servers_number = Integer.valueOf(line.split(" ")[1]);
		ro_area = Integer.valueOf(line.split(" ")[2]);
		ro_COP = Integer.valueOf(line.split(" ")[3]);
		
		reader.close();
		

		System.out.println("There are " + ro_clients_number + " clients");
		System.out.println("D=" + ro_D );
		System.out.println("motor=" + ro_gasoil );
		System.out.println("p=" + ro_p );
		System.out.println("local_heuristic=" + ro_local_heuristic );
		System.out.println("servers=" + ro_servers_number );
		System.out.println("ro_area (R)=" + ro_area );
		System.out.println("COP=" + ro_COP);

		
		int local_heuristic_id = 1;
		if ("HPF".equals(ro_local_heuristic)){
			local_heuristic_id = 1;			
		} 
		if ("lowest-id-first".equals(ro_local_heuristic)){
			local_heuristic_id = 2;			
		} 
		if ("highest-deadline-first".equals(ro_local_heuristic)){
			local_heuristic_id = 3;			
		} 
		if ("lowest-penalty-first".equals(ro_local_heuristic)){
			local_heuristic_id = 4;			
		} 
		if ("lowest-deadline-first".equals(ro_local_heuristic)){
			local_heuristic_id = 5;			
		} 
		if ("lowest-mips-first".equals(ro_local_heuristic)){
			local_heuristic_id = 6;			
		} 
		if ("highest-mips-first".equals(ro_local_heuristic)){
			local_heuristic_id = 7;			
		} 
		
		//priority-length 
		//priority-deadline
		//deadline-length? 
		
		if ("highest-priority-highest-mips".equals(ro_local_heuristic)){
			local_heuristic_id = 8;			
		} 
		if ("highest-priority-highest-deadline".equals(ro_local_heuristic)){
			local_heuristic_id = 9;			
		} 
		if ("highest-deadline-highest-mips".equals(ro_local_heuristic)){
			local_heuristic_id = 10;			
		}

		if ("PL".equals(ro_local_heuristic)){
			local_heuristic_id = 11;			
		} 
		if ("PD".equals(ro_local_heuristic)){
			local_heuristic_id = 12;			
		} 
		if ("DL".equals(ro_local_heuristic)){
			local_heuristic_id = 13;			
		} 
		
		//obtiene als funciones de los clientes
		ObjectivesVector [] client_functions = new ObjectivesVector[ro_clients_number];;
		for (int c = 0;c<ro_clients_number;c++) {
			client_functions[c]  = HeuDFVSRunner.getObjetctivesVector(Integer.valueOf(c), ro_instance_folder,local_heuristic_id);
		}
		
		//calcula e imprime pareto
		Objectives [] pareto = calcularParetoGlobal(client_functions,ro_discrete_points_number,ro_step_size);		
		//calcular e imprime heuristicas
		// calcularBau(pareto);

		//calcularIdeal(pareto);
		// calcularBTOg(pareto);
		calcularNash(pareto, client_functions);
		//calcularBTOd(pareto, client_functions);
		//calcularParetoDistribuido(pareto,client_functions);
		//calcularPMP(pareto, client_functions);


	}
	
	public static Objectives [] calcularParetoGlobal(ObjectivesVector[] client_functions, int iterations, double step) throws IOException{
		double[] b = new double[ro_clients_number];
		double[] c = new double[ro_clients_number];
		double[] deltaP = new double[ro_clients_number];
		double[] nct = new double[ro_clients_number];
		double[] vtt = new double[ro_clients_number];

		
		File fout = new File("output/pareto-global_" + ro_local_heuristic +"_" + ro_instance_folder.split("/")[1]+".FUN");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		int iteration = 0;
		Objectives [] pareto = new Objectives[iterations];;
		double paux= ro_p;
		
		while (iteration < iterations) {
			//double cost = 0.0;
			for (int i = 0; i < ro_clients_number; i++) {
				Objectives obj = poderar(client_functions[i],paux);
				deltaP[i] = obj.alpha;
				c[i] = obj.loss;
				nct[i] = obj.nonCompleteTasks;
				vtt[i] = obj.violatedTime;				
				b[i]= (ro_D-deltaP[i])*paux;
			}
			
		double socialCost = (Utils.sumarized(c));// + ((D - Utils.sumarized(s)) * alpha);
	    
		Objectives item = new Objectives();
	    item.alpha = Utils.sumarized(deltaP);
	    item.payment = paux;
	    item.socialCost = socialCost;
	    item.nonCompleteTasks = Utils.sumarized(nct);
	    item.violatedTime = Utils.sumarized(vtt);
	    item.paidToTenants = (Utils.sumarized(deltaP) * paux);
	    
	    // at 70%
		double cruising_power_watts = ro_servers_number* Utils.power(Constants.server_prcs_number/3, Constants.server_prcs_number/3, Constants.server_prcs_number);
	    
	    item.coolingPowerConsumption = HVAC.coolingPowerConsumption(cruising_power_watts,
	    		cruising_power_watts
	    		- item.alpha,
	    		ro_area,
	    		ro_COP
	    		); 
	    
	    if (item.coolingPowerConsumption<0) {
	    	item.coolingPowerConsumption = 0.0;
	    }
	    
	    double delta_h = getDeltaH(deltaP);
	    
	    item.coolingCost = delta_h * ro_gasoil / 10;
	    		
	    item.onsiteGenerationCost = (ro_D -  Utils.sumarized(deltaP) - delta_h) * ro_gasoil;
	    if (item.onsiteGenerationCost<0) {
	    	item.onsiteGenerationCost = 0.0;
	    }
	    item.onsiteGeneration = ro_D - item.alpha - delta_h;
	    if (item.onsiteGeneration< 0) {
	    	item.onsiteGeneration = 0.0;
	    }
	    
		double dcCost  = (Utils.sumarized(deltaP) * paux) + item.onsiteGenerationCost - item.coolingCost;
	    item.dcCost = dcCost;

	    
	    pareto[iteration]= item;		
	    bw.write(iteration + " " + paux + " " + item.alpha + " " + item.onsiteGeneration +" " + "0" + " " 
	    + dcCost + " " + socialCost + " " + item.nonCompleteTasks  +" " + item.violatedTime
	    +" " + item.paidToTenants+" " + item.onsiteGenerationCost+" " + item.coolingCost +" " +item.coolingPowerConsumption
	    		);

	    bw.newLine();
        paux=paux+step;
        iteration++;
		}
		 bw.close();
		 return pareto;
	
	}
	
	
	public static void calcularBau(Objectives[] pareto) throws IOException  {
		String global_heuristic = "bau";
		Objectives bau = pareto[0];
		String filename = "output/heu_" + ro_local_heuristic +"_" + global_heuristic + "-"+ ro_instance_folder.split("/")[1];
		
		// SOL
		double [] sol1=DoubleStream.iterate(0, n -> n).limit(ro_clients_number).toArray();
		bau.sol = ArrayUtils.toObject(sol1);	
		printUpperLevelHeuristics(bau, filename);
	}
	
	public static void calcularIdeal(Objectives[] pareto) throws IOException  {
		String global_heuristic = "ideal";
		Objectives ideal = new Objectives();
		ideal.dcCost  = pareto[0].dcCost;
		ideal.alpha  = pareto[pareto.length-1].alpha;
		String filename = "output/heu_" + ro_local_heuristic +"_" + global_heuristic + "-"+ ro_instance_folder.split("/")[1];
		
		// SOL
		double [] sol1=DoubleStream.iterate(0, n -> n).limit(ro_clients_number).toArray();
		ideal.sol = ArrayUtils.toObject(sol1);	
		
		
		printUpperLevelHeuristics(ideal, filename);
	}

	public static void calcularBTOg(Objectives[] pareto) throws IOException  {
		// TODO, CALCULAR BIEN
		Objectives ideal = new Objectives();
		ideal.dcCost  = pareto[0].dcCost;
		ideal.alpha  = pareto[pareto.length-1].alpha;
		String global_heuristic = "BTO-g";
	    Double scale = pareto[pareto.length-1].alpha/pareto[pareto.length-1].dcCost;;
	    String filename = "output/heu_" + ro_local_heuristic +"_" + global_heuristic + "_"+ ro_instance_folder.split("/")[1];
		Double[] aux = Arrays.stream(pareto).map( point  -> { 
			return Utils.dist(point.dcCost*scale, point.alpha, ideal.dcCost*scale, ideal.alpha);
		} ).toArray(Double[]::new);
		Objectives result = pareto[Utils.min_index(aux)];
		
		// SOL
		double [] sol1=DoubleStream.iterate(result.payment, n -> n).limit(ro_clients_number).toArray();
		result.sol = ArrayUtils.toObject(sol1);	
	
		
		printUpperLevelHeuristics(result, filename);

	}
	
	public static void calcularNash(Objectives[] pareto,ObjectivesVector [] client_functions ) throws IOException  {
		String global_heuristic = "nash";
		List<Objectives> nash = calcularNashEquilibriumAux(client_functions);
		String filename = "output/heu_" + ro_local_heuristic +"_" + global_heuristic + "-"+ ro_instance_folder.split("/")[1];
		printPareto(nash, filename);
	}
	
	public static void calcularBTOd(Objectives[] pareto,ObjectivesVector [] client_functions ) throws IOException  {
		String global_heuristic = "BTO-d";
		Objectives btod = calcularBTOdAux(pareto,client_functions);
		String filename = "output/heu_" + ro_local_heuristic +"_" + global_heuristic + "-"+ ro_instance_folder.split("/")[1];
		printUpperLevelHeuristics(btod, filename);
	}	
	
	
	public static Objectives calcularBTOdAux(Objectives[] pareto,ObjectivesVector[] client_functions){
		Objectives result = new Objectives();
		
		int iterations = pareto.length;
		
		Objectives[] btoByClients  = new Objectives[ro_clients_number];
		
		for (int i = 0; i < ro_clients_number; i++) {
				Objectives[] clientPareto = new Objectives[iterations];
				for (int j=0;j<iterations;j++) {
					Objectives item = new Objectives();
					Objectives obj = poderar(client_functions[i],pareto[j].payment);
					item.alpha = obj.alpha;
					item.dcCost = obj.payment * item.alpha;
					item.payment = obj.payment;
					item.nonCompleteTasks = obj.nonCompleteTasks;
					item.violatedTime = obj.violatedTime;
					item.socialCost = obj.loss;


					clientPareto[j] = item;
				}
				Objectives ideal = new Objectives();
				ideal.dcCost  = clientPareto[0].dcCost;
				ideal.alpha  = clientPareto[clientPareto.length-1].alpha;
			    Double scale = clientPareto[clientPareto.length-1].alpha/clientPareto[clientPareto.length-1].dcCost;;
				Double[] aux = Arrays.stream(clientPareto).map( point  -> { 
					return Utils.dist(point.dcCost*scale, point.alpha, ideal.dcCost*scale, ideal.alpha);
				} ).toArray(Double[]::new);
				btoByClients[i] = clientPareto[Utils.min_index(aux)];
		}
		
		Arrays.stream(btoByClients).forEach(e-> {
			System.out.println(e.payment);
			System.out.println(e.nonCompleteTasks);

		});
		
		result.dcCost = Utils.sumarized(Arrays.stream(btoByClients).map(e-> {return e.dcCost;}).toArray(Double[]::new));
		result.alpha = Utils.sumarized(Arrays.stream(btoByClients).map(e-> {return e.alpha;}).toArray(Double[]::new));
		result.nonCompleteTasks = Utils.sumarized(Arrays.stream(btoByClients).map(e-> {return e.nonCompleteTasks;}).toArray(Double[]::new));
		result.violatedTime = Utils.sumarized(Arrays.stream(btoByClients).map(e-> {return e.violatedTime;}).toArray(Double[]::new));
		//result.socialCost = Utils.sumarized(Arrays.stream(btoByClients).map(e-> {return e.socialCost;}).toArray(Double[]::new));


		// SOL
		result.sol = Arrays.stream(btoByClients).map(e-> {return e.payment;}).toArray(Double[]::new);	
		result.payment = -1.0;
		return result;
	}
	
	
	public static void calcularPMP(Objectives[] pareto,ObjectivesVector [] client_functions ) throws IOException  {
		String global_heuristic = "PMP";
		Objectives btod = calcularPMPAux(pareto,client_functions);
		String filename = "output/heu_" + ro_local_heuristic +"_" + global_heuristic + "-"+ ro_instance_folder.split("/")[1];
		printUpperLevelHeuristics(btod, filename);
	}
	
	
	
	public static Objectives calcularPMPAux(Objectives[] pareto,ObjectivesVector[] client_functions){
		Objectives[] pmpByClients  = new Objectives[ro_clients_number];
		//tamaño de la torta
		Double total = Utils.sumarized(Arrays.stream(client_functions).map(e-> {return 2 - e.clientTolerance;}).toArray(Double[]::new));
		
		//arreglo de porciones de cada cliente
		Double[] clientPortion = Arrays.stream(client_functions).map(e-> {return e.clientTolerance/total;}).toArray(Double[]::new);
		
		Objectives result = new Objectives();
		int iterations = pareto.length;
		Objectives[][] clientParetos = new Objectives[ro_clients_number][iterations];
		
		//calculo matrix de clientes por precios
		for (int i = 0; i < ro_clients_number; i++) {
			for (int j=0;j<iterations;j++) {
						Objectives item = new Objectives();
						Objectives obj = poderar(client_functions[i],pareto[j].payment);
						item.alpha = obj.alpha;
						item.dcCost = obj.payment * item.alpha;
						item.payment = obj.payment;
						item.nonCompleteTasks = obj.nonCompleteTasks;
						item.violatedTime = obj.violatedTime;
						item.socialCost = obj.loss;

						clientParetos[i][j]=item;
			}
		}		
		// recorro los clientes y me quedo
		for (int i = 0; i < ro_clients_number; i++) {
			//asumo que es creciente, entonces el maximo es el ultimo
			
			
			Objectives maxClientReduction = clientParetos[i][iterations-1];	
			//esto es lo que le voy a solicitar el cliente
			double askedlientReduction = maxClientReduction.alpha / clientPortion[i] ;
			
			//busco el punto mas cercano a la reduccion, y lo guardo
			Double[] aux = Arrays.stream(clientParetos[i]).map( point  -> { 
				return Utils.dist(point.dcCost, point.alpha, 0.0, askedlientReduction);
			} ).toArray(Double[]::new);
			
			
			pmpByClients[i] = clientParetos[i][Utils.min_index(aux)];
			
		}
		//sumo costo del datacenter y reduccion, es el punto global que voy a dibujar
		result.dcCost = Utils.sumarized(Arrays.stream(pmpByClients).map(e-> {return e.dcCost;}).toArray(Double[]::new));
		result.alpha = Utils.sumarized(Arrays.stream(pmpByClients).map(e-> {return e.alpha;}).toArray(Double[]::new));
		result.nonCompleteTasks = Utils.sumarized(Arrays.stream(pmpByClients).map(e-> {return e.nonCompleteTasks;}).toArray(Double[]::new));
		result.violatedTime = Utils.sumarized(Arrays.stream(pmpByClients).map(e-> {return e.violatedTime;}).toArray(Double[]::new));
		// SOL
		result.sol = Arrays.stream(pmpByClients).map(e-> {return e.payment;}).toArray(Double[]::new);	
		result.payment = -1.0;
		return result;
	}
	
	
	
	
	
	public static Objectives[] calcularParetoDistribuido(Objectives[] pareto,ObjectivesVector[] client_functions) throws IOException{
		Objectives result = new Objectives();
		
		int iterations = pareto.length;
		
		File fout = new File("output/pareto-dist_" + ro_local_heuristic +"_" + ro_instance_folder.split("/")[1]+".FUN");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	
		Objectives[][] clientParetos = new Objectives[ro_clients_number][iterations];
		
			for (int i = 0; i < ro_clients_number; i++) {
				for (int j=0;j<iterations;j++) {
						Objectives item = new Objectives();
						Objectives obj = poderar(client_functions[i],pareto[j].payment);
						item.alpha = obj.alpha;
						item.dcCost = pareto[j].payment * item.alpha;
						item.payment = pareto[j].payment;
						clientParetos[i][j]=item;
				}

		}
			
			List<Integer> range = IntStream.rangeClosed(0, iterations-1)
				    .boxed().collect(Collectors.toList());
			int  list = Generator.combination(range)
			  .multi(ro_clients_number).stream().toArray().length;
			
			Objectives[] paretoDist = new Objectives[ list];

			

			int index = 0;
			Generator.combination(range)
			  .multi(ro_clients_number)
			  .stream()
			  .forEach(e -> {
				  Objectives item = new Objectives();
				  item.alpha = 0.0;
				  item.dcCost = 0.0;

				  for (int k=0;k<ro_clients_number;k++) {
					  item.alpha += clientParetos[k][e.get(k)].alpha;
					  item.dcCost += clientParetos[k][e.get(k)].dcCost;
				  }
				    try {
						bw.write(0 + " " + result.payment  + " " + item.alpha + " " + "y" +" " + "err" + " " + item.dcCost + " " + "socialCost"
				    + " " + item.nonCompleteTasks  +" " + item.violatedTime);
					    
						bw.newLine();
				    } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			       
			  
				  
			
				  
			  });
			
			 bw.close();
		
			return paretoDist;
	}

	
	public static double getDeltaH(double[] deltaP) {
		// at 70%
		double cruising_power_watts = ro_servers_number* Utils.power(Constants.server_prcs_number/3, Constants.server_prcs_number/3, Constants.server_prcs_number);
		System.out.println(cruising_power_watts);

		double coolingPowerConsumption = HVAC.coolingPowerConsumption(cruising_power_watts,
		    		cruising_power_watts
		    		- Utils.sumarized(deltaP),
		    		ro_area,
		    		ro_COP
		    		); 
		    
		    if (coolingPowerConsumption<0) {
		    	coolingPowerConsumption = 0.0;
		    }
		   
		double delta_h = HVAC.coolingPowerConsumption(cruising_power_watts,
		    		cruising_power_watts,ro_area,ro_COP)-
		    HVAC.coolingPowerConsumption(cruising_power_watts,
		    		cruising_power_watts-Utils.sumarized(deltaP)
		    		,
		    		ro_area,
		    		ro_COP); 
		return delta_h;
	}
	
	public static double getH(double[] deltaP) {
		// at 70%
		double cruising_power_watts = ro_servers_number* Utils.power(Constants.server_prcs_number/3, Constants.server_prcs_number/3, Constants.server_prcs_number);
		 	
		
		double coolingPowerConsumption = HVAC.coolingPowerConsumption(cruising_power_watts,
		    		cruising_power_watts
		    		- Utils.sumarized(deltaP),
		    		ro_area,
		    		ro_COP
		    		); 
		    
		    if (coolingPowerConsumption<0) {
		    	coolingPowerConsumption = 0.0;
		    }
		   
		double delta_h = HVAC.coolingPowerConsumption(cruising_power_watts,
		    		cruising_power_watts,ro_area,ro_COP)-
		    HVAC.coolingPowerConsumption(cruising_power_watts,
		    		cruising_power_watts-Utils.sumarized(deltaP)
		    		,
		    		ro_area,
		    		ro_COP); 
		return cruising_power_watts - delta_h;
	}
	
	
	public static List<Objectives> calcularNashEquilibriumAux(ObjectivesVector[] client_functions){
		
		List<Objectives> resulList = new ArrayList<Objectives>();
		
		
		double[] b = new double[ro_clients_number];
		double[] c = new double[ro_clients_number];
		//double y=0;
		double[] deltaP = new double[ro_clients_number];
		
//		double[] nct = new double[ro_clients_number];
//		double[] vtt = new double[ro_clients_number];
		double ρ = ro_D;
		int cardC = ro_clients_number;
		int k = 0;
		double RI= ro_p + 0.1;
		double GP= 0;
		double ϵ = 0.0050;
		double δ=Math.abs((ρ-GP)/ρ);
		double α = ro_gasoil;
		double φ = α / 10;
			
		double deltaH = 0;
		while (δ>=ϵ && k < 5000) {
				k++;
				
//				for (int j = 0; j < cardC; j++) {					
//					Objectives obj = poderar(client_functions[j],RI);
//					deltaP[j] = obj.alpha;
//				    b[j]= (ρ - deltaH -deltaP[j])*RI;
//					c[j] = obj.loss;
//				}
//				deltaH = getDeltaH(deltaP);
				
				for (int j = 0; j < cardC; j++) {					
					Objectives obj = poderar(client_functions[j],RI);
					deltaP[j] = obj.alpha;
				    b[j]= (ρ - deltaH -deltaP[j])*RI;
					c[j] = obj.loss;
				}
		   

				//double coolingCost = coolingPowerConsumption * ro_gasoil / 10;
				//double gasoilCost =GP * α;
				//double H = getH(deltaP);
				deltaH = getDeltaH(deltaP);
				double realGP = ro_D - Utils.sumarized(deltaP) - deltaH;
				if (realGP<0) {
					realGP = 0;
				}	
				//deltaH = 0;
				//System.out.println(realGP);
				
				double coolCost = deltaH * φ;
				if (coolCost<0) {
					coolCost = 0;
				}			
				
				double dcCost  = (Utils.sumarized(deltaP) * RI) - coolCost + realGP * α ;
				deltaH = 0;
				
				Objectives result = new Objectives();
				result.payment = RI;
//			    result.socialCost = socialCost;
			    result.dcCost = dcCost;
			    result.onsiteGeneration = GP;
			
		    
			
			
//			
//			 if (gasoilCost <0) {
//				 gasoilCost=0;
//			    }
			
			//double H = getH(deltaP);
			//double H = 0 ;
			//double socialCost = (Utils.sumarized(c)) + gasoilCost + H * α / 10 ;
			

			//System.out.println(socialCost);
		   
		
		   
		   
		    //result.alpha = Utils.sumarized(deltaP);
		    //System.out.println("result.alpha="+result.alpha);

//		    result.nonCompleteTasks = Utils.sumarized(nct);
//		    result.violatedTime = Utils.sumarized(vtt);
//		    result.coolingCost = coolingCost;
		    //result.coolingPowerConsumption = coolingPowerConsumption;
		    
		    //∆P(Φj(RIj)) + ∆H(Φj(RIj)) =ρ−bj/p
		    
		    
		    //GP  =(( ρ - Utils.sumarized(deltaP) - deltaH ) * ro_gasoil);
			//deltaH = getDeltaH(deltaP);
			//System.out.println("deltaH=" + deltaH);
			//System.out.println("deltaP=" +  Utils.sumarized(deltaP));
			//System.out.println("GP=" + GP);
			//System.out.println("cardC=" + cardC);
			//deltaH = 0;
			GP = Math.sqrt(    Utils.sumarized(b)   *   cardC  *  (ρ - deltaH )  /  α  ) - (cardC-1)*( ρ - deltaH);
			
			if (GP <0) {
				GP=0;
			}
			

		    
		   

			δ = Math.abs( ( ρ -  deltaH - GP -   Utils.sumarized(deltaP)) / (ρ -  deltaH)) ;
			System.out.println(RI+ " " + dcCost + " " + realGP + " " + coolCost  + " " + Utils.sumarized(deltaP) * RI);
			
			
			 
			RI = Utils.sumarized(b)/(( cardC-1 ) * (ρ - deltaH)  + GP);
			
			//System.out.println(deltaH + GP + Utils.sumarized(deltaP) );
			//System.out.println("deltaH=" +deltaH);
			//System.out.println("GP=" + GP);
			//System.out.println("deltaP=" + Utils.sumarized(deltaP));
			//System.out.println("δ="+δ);
		
			 //deltaH = 0;

			
			// SOL
			double [] sol1=DoubleStream.iterate(result.payment, n -> n).limit(ro_clients_number).toArray();
			result.sol = ArrayUtils.toObject(sol1);	
			resulList.add(result);
		}
		
		
		
		return resulList;
	}
	

	public static void printPareto(List<Objectives> results, String filename) throws IOException  {
		File fout = new File(filename+".FUN");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		results.stream().forEach(
				result -> {
					try {
						bw.write(0 + " " + result.payment + " " + result.alpha + " " 
								+ result.onsiteGeneration+" " + 0 + " " + result.dcCost + " " + result.socialCost
								+ " " + result.nonCompleteTasks+ " " + result.violatedTime
								+" " + result.paidToTenants+" " + result.onsiteGenerationCost+" " + result.coolingCost +" " +result.coolingPowerConsumption
										)
								
								;
						bw.newLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				);
	    
	    bw.close();
		//printUpperLevelHeuristicsSolution(result, filename);
	}

	
	
	public static void printUpperLevelHeuristics(Objectives result, String filename) throws IOException  {
		File fout = new File(filename+".FUN");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write(0 + " " + result.payment + " " + result.alpha + " " 
		+ 0+" " + 0 + " " + result.dcCost + " " + result.socialCost
		+ " " + result.nonCompleteTasks+ " " + result.violatedTime
		+" " + result.paidToTenants+" " + result.onsiteGenerationCost+" " + result.coolingCost +" " +result.coolingPowerConsumption
				)
		
		;
	    bw.newLine();
	    bw.close();
		printUpperLevelHeuristicsSolution(result, filename);
	}
	
	
	
	public static void printUpperLevelHeuristicsSolution(Objectives result, String filename) throws IOException  {
		
		
		File fout = new File(filename+".VAR");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		List<String> strList = Arrays.asList(result.sol).stream().map( e  -> { 
			return String.valueOf(e);
		} ).collect(Collectors.toList());
		String joinedString = String.join(" ", strList);
		bw.write(joinedString)
		;
	    bw.newLine();
	    bw.close();

	}

	
	
	public static Objectives poderar(ObjectivesVector function, double price)  {
		
		Double maxProfit = -1 * Double.valueOf( Double.MAX_VALUE);
		int index = 0;

		for (int j = 0;j<function.reductionVector.length;j++){
			Double profit = function.reductionVector[j] * Double.valueOf(price) -  function.lossVector[j] 
					+  function.lossVector[function.reductionVector.length-1];
			if (maxProfit<= profit){
				maxProfit = profit;
				index = j;
			}
		}
		Objectives result = new Objectives();
		result.alpha = function.reductionVector[index];
		result.loss = function.lossVector[index];
		result.violatedTime = function.violatedTime[index];
		result.nonCompleteTasks = function.nonCompleteTasks[index];
		result.payment = price;

		return result;
	
	}
	
	/*
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
	*/

	
	
}
