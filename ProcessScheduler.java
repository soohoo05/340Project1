import java.io.FileNotFoundException;
import java.util.ArrayList;

/*
 * Project 1 340 
 * by Bryan Charlie and Tyler Soo Hoo
 */
public class ProcessScheduler {
	/*
	 * main class takes in argument, puts into FileHandler class depending on amount of arguments inputted
	 * sends outputted job list and quantum time (if applicable) to correct algorithm
	 */
	public static void main(String[] args) throws FileNotFoundException{

		int quantumTime=0;
		String algorithm;
		ArrayList<PCB> Jobs = new ArrayList<PCB>();
		FileHandler fh;

		if(args.length == 3)
			fh = new FileHandler(args[2]);
		else
			fh = new FileHandler(args[1]);


		Jobs = fh.getJobs();

		if(args[0].equals("SJF")){
			SJF sjfOut = new SJF(Jobs);	
		}//if
		else if(args[0].equals("FCFS")){
			FCFS out = new FCFS(Jobs);
		}//else
		else if(args[0].equals("RR") && Integer.parseInt(args[1]) > 0){
			RR round= new RR(Jobs, Integer.parseInt(args[1]));				
		}//else
		else{
			System.out.println("algorithm not inputted as SJF, FCFS or RR. Quantum Time also cannot be 0 or negative");
		}//else
	}//main

}//class

