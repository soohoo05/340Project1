
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class RR{
	private Queue<PCB> ready = new LinkedList<PCB>();
	private Queue<PCB> waiting = new LinkedList<PCB>();
	private Queue<PCB> processWaiting;

	private ArrayList<PCB> jobList;

	private PCB currentPCB;
	private boolean processing = true;
	private int currentBurstTime;
	private int time = 0;
	private int jobsCompleted = 0;
	private double totalProTime;
	private double totalWaitTime;
	private double totalTurnTime;

	private int quantumTime;

	public RR(ArrayList<PCB> jobs, int quantumTime) {
		jobList = jobs;
		this.quantumTime = quantumTime;
		for(PCB i: jobList){
			i.isReady();
		//	System.out.println(i.getID() + " " + i.getCurrentBurst());
		}

		initialFill();
		process();

	}

	private void process() {

		while(processing){

			currentPCB = ready.remove();

			if(!currentPCB.isFinished()){

				currentBurstTime = currentPCB.getCurrentBurst();
				//System.out.println(currentPCB.getID());

				currentPCB.isRunning();
				
				if(currentBurstTime <= quantumTime){
					addToTime(currentBurstTime);
					currentPCB.removeCurrentBurst();
					currentPCB.isWaiting();
					addToWaiting(currentPCB);
				}
				else if(currentBurstTime > quantumTime){
					addToTime(quantumTime);
					currentPCB.putInt(currentBurstTime - quantumTime);
					ready.add(currentPCB);
				}

			}
			else { //we should never reach here unless input is wrong.
				jobsCompleted++;
				time += currentPCB.getCurrentBurst();
				System.out.println(currentPCB.toString() + "at time " + time + "took " +
						currentPCB.totalProcessingTime() + " processing time");
			}
			

			processWaiting();
			fillReadyQueue();
			
			if(processingIsFinished())
				processing = false;
		}
		System.out.println("Algorithm:RR. current CPU clock value:"+time+". average processing time:"
				+avgProcessingTime()+". average waiting time:"+avgWaitingTime()+". average turnaround time:"+avgTurnTime());
	}

	/*
	 * method fillReadyQueue
	 * fills ready queue keeping into account the ready and waiting queue size wont go past 10,
	 * jobList still have at least one job in it		
	 */
	private void fillReadyQueue() {
		if(ready.size() + waiting.size() != 10 && jobList.size() != 0){
			//if nothing is in either queue and the next joblist can;t arrive yet, increments time to next
			//joblist arrivaltime
			if(ready.size() == 0  && waiting.size() == 0 && time < jobList.get(0).getArrivalTime()){
				time = jobList.get(0).getArrivalTime();
				ready.add(jobList.remove(0));
			}//if
			//if there is at least 1 in either 	queue and next job hasnt arrived yet, do nothing
			else if(time < jobList.get(0).getArrivalTime() && ready.size() + waiting.size() >= 1){
				return;
			}//else if
			//if has arrived already, add to queue
			else{
				ready.add(jobList.remove(0));
			}//else
		}//if

	}//fillReadyQueue

	/*
	 * method processingIsFinished
	 * processing is finished if we have no jobs left in either queue
	 */
	private boolean processingIsFinished(){
		if((ready.isEmpty() && waiting.isEmpty()))
			return true;
		return false;
	}

	/*
	 * method processWaiting
	 * if there is nothing in the ready queue, and something in the waiting, remove from the waiting and add
	 * to the ready queue one job
	 */
	private void processWaiting(){
		if(!waiting.isEmpty() && ready.isEmpty()){
			PCB p = waiting.remove();
			addToTime(p.getIoTime());
			ready.add(p);
		}//if
	}//processWaiting
	
	public int waitingTime(){
		return(time - currentPCB.bursttotal()-currentPCB.getArrivalTime());
	}//waitingTime
	
	public double avgProcessingTime(){
		return totalProTime/jobsCompleted;
	}//avgProcessingTime
	
	public double avgWaitingTime(){		
		return totalWaitTime/jobsCompleted;
	}//avgWaitingTime
	
	public double avgTurnTime(){
		return totalTurnTime/jobsCompleted;
	}//avgTurnTime
	
	/*
	 * method addToWaiting
	 * increments jobs completed, prints out time finished, totalProcessingTime, totalWaitingTime,
	 * and TurnaroundTime. adds all these for current PCB for averages that will be printed later on
	 */
	private void addToWaiting(PCB p){
		if(p.isFinished()){
			jobsCompleted++;
			System.out.println(p.toString() + "at time " + time + ". The job took " +
					currentPCB.totalProcessingTime() + " processing time"+". total waiting time:"+waitingTime()
					+". turnaround time:"+(time-currentPCB.getArrivalTime()));
			totalProTime += currentPCB.totalProcessingTime();
			totalWaitTime += (time - currentPCB.bursttotal()- currentPCB.getArrivalTime());		
			totalTurnTime+=(time-currentPCB.getArrivalTime());
			return;
		}//if
		else{
			waiting.add(p);
		}//else
	}//addToWaiting
	

	/**
	 * method addToTime
	 * @param i	Amount of time that will be added to current time processing.		 
	 */
	private void addToTime(int i){
		
		int tempTime = time;
		int size = waiting.size();
		
		//increments time
		while(time != tempTime + i){
			time++;
			currentPCB.incProgramCounter();
			
			//if time/200=0 print out statements like what time it is, how many jobs completed
			//and number of jobs in ready and waiting queue
			if(time%200==0){
			System.out.println ("time is now:"+time);
			System.out.println ("Number of jobs completed is: " + jobsCompleted);
			System.out.println ("Number of jobs in the ready queue:"+ready.size());
			System.out.println ("Number of jobs in the blocked queue:"+waiting.size());
			}				
			int j = 0;
			
			//traversing through waiting queue, calling on ioIsFinished to increment io time completed
			while(j != size && !waiting.isEmpty()){
				j++;
				PCB p = waiting.remove();

				if(p.ioIsFinished()){ //add to ready if io is finished
					ready.add(p);
					size--;
				}//if
				
				else{ //else add to waiting queue
					waiting.add(p);
				
				}//else
			}//while
		}//while
}//addToTime
	/*
	 * method initialFill
	 * checks front of jobList and pulls up to 10 jobs into ready queue pending that they all arrived
	 * at time 0
	 */
	private void initialFill(){		
			while(jobList.size() != 0&& jobList.get(0).getArrivalTime() == 0 && ready.size() != 10 ){
				ready.add(jobList.get(0));
				jobList.remove(0);
			}//while
	}//initialfill
}//RR