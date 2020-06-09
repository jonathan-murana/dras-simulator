package tests;

import org.junit.Test;
import uy.udelar.fing.dras.runner.HeuDFVSRunner;
import uy.udelar.fing.dras.utils.Objectives;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TestDRAS {

//    public static String instance_folder = "instances/random";
//	private final static String instance_name = "ins";
//	public static Integer clients_number;
//
//    @Test
//    public void testAlpha() throws IOException{
// 
//		System.out.println("Reading clients number...");
//		BufferedReader reader;
//			reader = new BufferedReader(new FileReader(instance_folder + "/" + instance_name));
//		String line;
//			line = reader.readLine();
//
//		clients_number = Integer.valueOf(line.split(" ")[0]);
//		reader.close();
//		
//		
//    	//Objectives o = HeuDFVSFirstFitIdRunner.getAlpha(3.0, 0, "instances/example1/");
//    	for (int c = 0; c <1;  c++) {
//    		
//    		File fout = new File("output/" + instance_folder.split("/")[1] + "_out_client" + c);
//    		FileOutputStream fos = new FileOutputStream(fout);
//    		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
//    		
//	        for (int i = 0; i <= 100; i++) {
//	        	
//	        	
//	            //[s(i),b(i)] = Cost_s(p,a1(i),a2);
//	            Objectives objectives = HeuDFVSFirstFitIdRunner.getAlpha(i * 1.0, c, instance_folder);
//	            bw.write( i * 1.0 + " " +  objectives.alpha + " " +
//	                    objectives.profit + " " +  objectives.loss + " " +  objectives.payment  + " " +  objectives.reductionPercentage
//	                    + " " +  objectives.nonCompleteTasks
//	            );
//	            bw.newLine();
//	            System.out.println(i + "cores " + objectives.reductionPercentage);
//	        }
//	        
//	        bw.close();
//	        
//    	}
//
//	    //assertEquals(o.alpha, (Double)837.0);
//	    
//    }
    

 
}