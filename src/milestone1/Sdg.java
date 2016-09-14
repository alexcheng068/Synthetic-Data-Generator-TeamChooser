package milestone1;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.opencsv.*;

import java.io.FileWriter;
import java.util.Random;
public class Sdg {
	
	private  void ShuffleArray(String[][] array)
	{
	    String  temp0, temp1;
	    int index;
	    Random random = new Random();
	    for (int i = array.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        temp0 = array[index][0];
	        temp1 = array[index][1];
	        array[index][0] = array[i][0];
	        array[index][1] = array[i][1];       
	        array[i][0] = temp0;
	        array[i][1] =temp1;
	    }
	}


	public static void main(String[] args) {
		String csvFile = "rating_list.csv";
		BufferedReader br = null;
		BufferedReader brcount = null;
		String dummyline = "";
		String line = "";
		String cvsSplitBy = ",";
		Random rand=new Random();
		Sdg sa= new Sdg();
		try {

			brcount = new BufferedReader(new FileReader(csvFile));
			br = new BufferedReader(new FileReader(csvFile));
			int count=0;
			while ((dummyline = brcount.readLine()) != null) {count++;}
			
			String[][] name_rating= new String[count][2];
			int i=0;
			while ((line = br.readLine()) != null){		
				// use comma as separator
				String[] tmp = line.split(cvsSplitBy);
				name_rating[i][0]=tmp[0];
				name_rating[i][1]=tmp[1];
				//System.out.println( name_rating[i][1]+"         "+ name_rating[i][0]);
				i++;
			}
			
			String[][] name_rating_tmp= new String[count-1][2];
			for(i=1;i<count;i++){
				name_rating_tmp[i-1][0]=name_rating[i][0];
				name_rating_tmp[i-1][1]=name_rating[i][1];
				//System.out.println(name_rating_tmp[i-1][0]+"  "+name_rating_tmp[i-1][1]);
			}
			
			
			
		
			int num_of_game=10;
			int players_per_team=6;
			String[][][] game= new String [num_of_game][players_per_team+1][4];//6 player per team, 7th row is score
			
			//fill game
			for (int k=0;k<num_of_game;k++){//each game
				sa.ShuffleArray(name_rating_tmp);
				double sumA=0;
				double sumB=0;
				for (int p_count=0;p_count<players_per_team;p_count++){
					game[k][p_count][0]=name_rating_tmp[p_count][0];
					game[k][p_count][1]=name_rating_tmp[p_count][1];
					game[k][p_count][2]=name_rating_tmp[p_count+players_per_team][0];
					game[k][p_count][3]=name_rating_tmp[p_count+players_per_team][1];
					sumA=sumA+ Double.parseDouble(game[k][p_count][1]);
					sumB=sumB+ Double.parseDouble(game[k][p_count][3]);
				}
				double avgA=sumA/players_per_team;
				double avgB=sumB/players_per_team;
				System.out.println("avg a is "+avgA+" avg b is "+avgB);
				if (avgA==avgB){
					game[k][players_per_team][3]="0";
					game[k][players_per_team][1]="0";
				}
				else if (avgA>avgB){
					double diff=(avgA-avgB)*2;
					Long L = Math.round(diff);
					int score = Integer.valueOf(L.intValue());				
					game[k][players_per_team][1]=String.valueOf(score);
					game[k][players_per_team][3]="0";
				}
				else{
					double diff=(avgB-avgA)*2;
					Long L = Math.round(diff);
					int score = Integer.valueOf(L.intValue());				
					game[k][players_per_team][3]=String.valueOf(score);
					game[k][players_per_team][1]="0";						
				}
				game[k][players_per_team][0]="Score";
			}
			
			
			
			
			
			
			 String csv = "data.csv";
		     CSVWriter writer = new CSVWriter(new FileWriter(csv));
		      //Write the record to file
		     for (int k=0;k<num_of_game;k++){//each game
		      for ( int j=0; j < players_per_team+1; j++) {
		    	   writer.writeNext(game[k][j]);
		    	}
		      String [] blank = "".split(",");
		      	writer.writeNext(blank);
		     }
		        
		      //close the writer
		      writer.close();
			
			
			
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}



