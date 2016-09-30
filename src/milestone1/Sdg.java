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
				name_rating[i][0]=tmp[1];
				name_rating[i][1]=tmp[3];
				//System.out.println( name_rating[i][1]+"         "+ name_rating[i][0]);
				i++;
			}

			String[][] name_rating_tmp= new String[count-1][2];
			for(i=1;i<count;i++){
				name_rating_tmp[i-1][0]=name_rating[i][0];
				name_rating_tmp[i-1][1]=name_rating[i][1];
				//System.out.println(name_rating_tmp[i-1][0]+"  "+name_rating_tmp[i-1][1]);
			}



			int max_players_per_team=8;
			int min_players_per_team=4;
			int num_of_game=2000;
			int players_per_team;

			//output file set up
			String csv = "game_instances.csv";
			String dbcsv= "game_instances_db.csv";
			CSVWriter writer = new CSVWriter(new FileWriter(csv));
			CSVWriter writerDB = new CSVWriter(new FileWriter(dbcsv));

			//fill game
			for (int k=0;k<num_of_game;k++){//each game
				sa.ShuffleArray(name_rating_tmp);
				double sumA=0;
				double sumB=0;	
				//generate number of players for each game	
				//Min + (int)(Math.random() * ((Max - Min) + 1))
				players_per_team=rand.nextInt((max_players_per_team-min_players_per_team)+1)+min_players_per_team;
				System.out.println("player per team"+players_per_team);
				String[][] game= new String [players_per_team+1][4];//6 player per team, 7th row is score
				
				for (int p_count=0;p_count<players_per_team;p_count++){
					game[p_count][0]=name_rating_tmp[p_count][0];
					game[p_count][1]=name_rating_tmp[p_count][1];
					game[p_count][2]=name_rating_tmp[p_count+players_per_team][0];
					game[p_count][3]=name_rating_tmp[p_count+players_per_team][1];
					sumA=sumA+ Double.parseDouble(game[p_count][1]);
					sumB=sumB+ Double.parseDouble(game[p_count][3]);
				}
				double avgA=sumA/players_per_team;
				double avgB=sumB/players_per_team;
				System.out.println("avg a is "+avgA+" avg b is "+avgB);
				if (avgA==avgB){
					game[players_per_team][3]="0";
					game[players_per_team][1]="0";
				}
				else if (avgA>avgB){
					double diff=(avgA-avgB)*2;
					Long L = Math.round(diff);
					int score = Integer.valueOf(L.intValue());				
					game[players_per_team][1]=String.valueOf(score);
					game[players_per_team][3]="0";
				}
				else{
					double diff=(avgB-avgA)*2;
					Long L = Math.round(diff);
					int score = Integer.valueOf(L.intValue());				
					game[players_per_team][3]=String.valueOf(score);
					game[players_per_team][1]="0";						
				}
				game[players_per_team][0]="Score";


				//output to file

				String gameid= "Game ID: ,"+String.valueOf(k+1);
				String [] gameidString = gameid.split(",");
				writer.writeNext(gameidString);

				for ( int m=0; m < players_per_team+1; m++) {
					String [] gameString = new String[4];
					for (int j=0;j<4;j++){
						gameString[j]=game[m][j];
					}
					writer.writeNext(gameString);
				}
				String [] blank = "".split(",");
				writer.writeNext(blank);
				
				//output to DB file
				
				for ( int m=0; m < players_per_team; m++) {
					String [] gameStringDB = new String[7];
					for (int j=0;j<4;j++){
						gameStringDB[0]=String.valueOf(k+1);
						gameStringDB[1]=game[m][0];
						gameStringDB[2]=game[m][1];
						gameStringDB[3]=game[players_per_team][1];
						gameStringDB[4]=game[m][2];
						gameStringDB[5]=game[m][3];
						gameStringDB[6]=game[players_per_team][3];
					}
					writerDB.writeNext(gameStringDB);
				}
				
				


			}//end of for loop for each game

			//close the writer
			writer.close();
			writerDB.close();


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



