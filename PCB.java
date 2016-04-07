
import java.awt.List;
import java.util.ArrayList;
/*
 * class PCB
 * holds the ID, arrivalTime, noOfBursts, bursts, ioTime, programCounter, and job state for current job 
 */
public class PCB implements Comparable<PCB>{
	private String ID;
	private int arrivalTime;
	private int noOfBursts;
	private ArrayList<Integer> bursts = new ArrayList<Integer>();
	private int currentBurst = 0;
	private int ioTime = 10;
	private boolean running = false;
	private boolean waiting = false;
	private boolean ready= false;
	private int burstTotal = 0;
	private int programCounter=0;

	/**
	 * Initializes PCB parameters.
	 * @param ID
	 * @param ArrivalTime
	 * @param noOfBursts
	 * @param bursts
	 */
	public PCB(String ID, String ArrivalTime, String noOfBursts , ArrayList<Integer> bursts){
		if(bursts == null){
			System.out.println("error");
		}//if

		this.ID = ID;
		this.arrivalTime = Integer.parseInt(ArrivalTime);
		this.noOfBursts = Integer.parseInt(noOfBursts);
		this.bursts = bursts;

		for (int i: bursts){
			burstTotal += i;
		}// enhanced for
	}//PCB constructor


	public int getCurrentBurst(){
		if(!bursts.isEmpty()){
			return currentBurst = bursts.get(0);
		}//if
		else{
			return currentBurst;
		}//else		
	}//getCurrentBurst

	public int getNumberofBursts(){
		return noOfBursts;
	}//getNumberofBursts

	public void isRunning(){
		running = true;
		waiting = false;
		ready = false;
	}//isRunning

	public void isWaiting(){
		waiting = true;
		running = false;
		ready = false;
	}//isWaiting

	public void isReady(){
		ready = true;
		running = false;
		waiting = false;
	}//isReady

	//current job is finished if burst arraylist is empty
	public boolean isFinished(){
		return(bursts.isEmpty());
	}//isFinished

	public String toString(){
		String s = String.format("Job id %s arrived at time %s with number of "
				+ "bursts %d and has completed ", ID, arrivalTime, noOfBursts);
		return s;
	}//toString

	public String getID(){
		return ID;
	}//getID

	public void removeCurrentBurst(){
		bursts.remove(0);
	}//removeCurrentBurst

	public int getArrivalTime(){
		return arrivalTime;
	}//getArrivalTime

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * override compareTo used exclusively for natural ordering of SJF
	 */
	@Override
	public int compareTo(PCB o) {
		if(this.getCurrentBurst() > o.getCurrentBurst())
			return 1;
		else if((this.getCurrentBurst() < o.getCurrentBurst()))
			return -1;
		else
			return 0;
	}//compareTo

	/*
	 * returns bursts+ total ioTime used
	 */
	public int totalProcessingTime(){
		return ((noOfBursts - 1) * 10) + burstTotal;
	}//totalProcessingTime

	/*
	 * returns burst lengths all together
	 */
	public int bursttotal(){
		return burstTotal;
	}//bursttotal

	/*
	 * used for RR, puts an interger at the head of arraylist of PCB
	 */
	public void putInt(int i){
		bursts.set(0, i);
	}//putInt

	/*
	 * return current io status to check if we can place the PCB into the ready queue
	 */
	public boolean ioIsFinished(){
		if(ioTime == 0){
			ioTime = 10;
			return true;
		}//if
		else{ 
			ioTime--;	
		}//else
		return false;	
	}//ioisFinished

	public int getIoTime(){	
		return ioTime;
	}//getIoTime

	public void incProgramCounter(){
		programCounter++;
	}//incProgramCounter

}//PCB
