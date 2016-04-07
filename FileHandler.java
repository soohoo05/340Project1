
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
/*
 * class FileHandler takes input file, converts lines into jobID, ArrivalTime, #ofBursts and
 * Burst Times(put into an arraylist)
 */
public class FileHandler {
	private ArrayList<PCB> Jobs = new ArrayList<PCB>();
	private Scanner scanner = null;

	public FileHandler(String in){

		try {
			scanner = new Scanner(new FileReader(in)); //initialize scanner with file name
		}//try
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}//catch

		readSource();
		scanner.close();
	}//FileHandler

	/*
	 * method readSource
	 * splits the current line, inputs id, arrivalTime, noOfBursts and bursts into appropriate variables for
	 * the jobList
	 */
	private void readSource()  {

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] result = line.split(" ");
			String id = result[0];
			String arrivalTime = result[1];
			String noOfBursts = result[2];
			PCB jobx = null;
			ArrayList<Integer> bursts = new ArrayList<Integer>();

			int i = 2;

			while(i++ != result.length - 1){
				bursts.add(Integer.parseInt(result[i]));
			}//while

			jobx=new PCB(id, arrivalTime, noOfBursts, bursts);
			Jobs.add(jobx);
		}//while

	}//readSource

	public ArrayList<PCB> getJobs(){
		return Jobs;
	}//getJobs
}//FileHandler




