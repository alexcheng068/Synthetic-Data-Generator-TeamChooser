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
		String  temp0, temp1,temp2;
		int index;
		Random random = new Random();
		for (int i = array.length - 1; i > 0; i--)
		{
			index = random.nextInt(i + 1);
			temp0 = array[index][0];
			temp1 = array[index][1];
			temp2 = array[index][2];
			array[index][0] = array[i][0];
			array[index][1] = array[i][1];
			array[index][2] = array[i][2];   
			array[i][0] = temp0;
			array[i][1] =temp1;
			array[i][2] =temp2;
		}
	}


	private  String[][] splitTeam(String[][] array,int players_per_team,boolean odd)
	{

		//array[0] name,
		//array[1] rating
		//array[2] def off
		String[][] array_with_number= new String [players_per_team*2][3];
		String middlename="";// when odd
		String middlerating="";// when odd
		String middlePos="";// when odd
		int middle=players_per_team-1;
		for (int i=0; i<players_per_team*2;i++){
			array_with_number[i][0]=array[i][0];
			array_with_number[i][1]=array[i][1];
			array_with_number[i][2]=array[i][2];
		}
		if(odd){
			array_with_number[players_per_team*2-1][0]="";
			array_with_number[players_per_team*2-1][1]="";
			array_with_number[players_per_team*2-1][2]="";
		}
		array_with_number=sort(array_with_number,players_per_team);
		if (odd){
			middlename=array_with_number[middle][0];
			middlerating=array_with_number[middle][1];
			middlePos=array_with_number[middle][2];
			shiftToMiddle(array_with_number,players_per_team);		
		}


		String[][] game= new String [players_per_team+1][6];
		//game [0] name A, [1] def off ,[2] rating, [3] name B, [4] def off, [5] rating

		//fill game
		int pcount=0;
		for (int j=0;j<players_per_team*2;j++){
			
			if (j%2==0 && pcount%2==0){// team A, odd , 1st
				game[pcount][0]=array_with_number[j][0];//name
				game[pcount][1]=array_with_number[j][2];//off def
				game[pcount][2]=array_with_number[j][1];//rating
			}
			else if (j%2==1 && pcount%2==0){// team B odd, 2nd
				game[pcount][3]=array_with_number[j][0];//name
				game[pcount][4]=array_with_number[j][2];//off def
				game[pcount][5]=array_with_number[j][1];//rating
				pcount++;
			}
			else if (j%2==0 && pcount%2==1){// team B even, 3rd
				game[pcount][3]=array_with_number[j][0];//name
				game[pcount][4]=array_with_number[j][2];//off def
				game[pcount][5]=array_with_number[j][1];//rating
			}
			else//  team A, even , 4th
			{
				game[pcount][0]=array_with_number[j][0];//name
				game[pcount][1]=array_with_number[j][2];//off def
				game[pcount][2]=array_with_number[j][1];//rating
				pcount++;
			}


		}



		return game;
	}

	private String[][] sort(String[][] array, int players_per_team){//bubble sort lol
		int n = players_per_team*2;
		String temp0="";
		String temp1="";
		String temp2="";
		for (int i = 0; i < n; i++) {
			for (int j = 1; j < (n - i); j++) {
				if (Double.parseDouble(array[j - 1][1]) < Double.parseDouble(array[j][1])) {
					temp0 = array[j - 1][0];
					temp1 = array[j - 1][1];
					temp2 = array[j - 1][2];
					array[j - 1][0] = array[j][0];
					array[j - 1][1] = array[j][1];
					array[j - 1][2] = array[j][2];
					array[j][0] = temp0;
					array[j][1] = temp1;
					array[j][2] = temp2;
				}

			}
		}
		return array;
	}

	private void shiftToMiddle(String[][] array, int players_per_team){//in case of odd number
		for (int i=players_per_team; i<(players_per_team*2);i++){
			array[i - 1][0]=array[i][0];
			array[i - 1][1]=array[i][1];
			array[i - 1][2]=array[i][2];
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

			String[][] name_rating= new String[count][3];
			int i=0;
			while ((line = br.readLine()) != null){		
				// use comma as separator
				String[] tmp = line.split(cvsSplitBy);
				name_rating[i][0]=tmp[1];
				name_rating[i][1]=tmp[3];
				name_rating[i][2]=tmp[5];
				//System.out.println( name_rating[i][1]+"         "+ name_rating[i][0]);
				i++;
			}

			String[][] name_rating_tmp= new String[count-1][3];
			for(i=1;i<count;i++){
				name_rating_tmp[i-1][0]=name_rating[i][0];//name
				name_rating_tmp[i-1][1]=name_rating[i][1];//rating
				name_rating_tmp[i-1][2]=name_rating[i][2];//off def
				//System.out.println(name_rating_tmp[i-1][0]+"  "+name_rating_tmp[i-1][1]);
			}

			int [] score_count= new int [15];
			for (i=0;i<15;i++){//initualize  
				score_count[i]=0;
			}

			int max_players_per_team=8;
			int min_players_per_team=4;
			int num_of_game=2000;
			int players_per_team;
			int totalscore=0;
			double avgscore=0;

			//output file set up
			String csv = "game_instances.csv";
			String dbcsv= "game_instances_db.csv";
			CSVWriter writer = new CSVWriter(new FileWriter(csv));
			CSVWriter writerDB = new CSVWriter(new FileWriter(dbcsv));
			players_per_team=rand.nextInt((max_players_per_team-min_players_per_team)+1)+min_players_per_team;
			boolean odd=false;
			//fill game	
			for (int k=0;k<num_of_game;k++){//each game
				System.out.println("game num is "+(k+1));
				boolean offdefBalance=false;
				String off="Off";
				String def="Def";


				//generate set game instance per set of play
				if (k%2==0){
					//generate number of players for each game	
					//Min + (int)(Math.random() * ((Max - Min) + 1))
					players_per_team=rand.nextInt((max_players_per_team-min_players_per_team)+1)+min_players_per_team;
					String[][] splitarray= new String[players_per_team][3];

					//off and def balance

					//					while ( offdefBalance==false){
					//						int offA=0, offB=0,defA=0,defB=0;
					//						offdefBalance=true;
					//						sa.ShuffleArray(name_rating_tmp);
					//						for (int countoff=0;countoff<players_per_team;countoff++){				
					//							if(off.equals(name_rating_tmp[countoff][2]))
					//								offA++;
					//							else if (def.equals(name_rating_tmp[countoff][2]))
					//								defA++;
					//						}
					//						if (Math.abs(offA-defA)>1)
					//							offdefBalance=false;
					//						for (int countoff=players_per_team;countoff<players_per_team*2;countoff++){				
					//							if(off.equals(name_rating_tmp[countoff][2]))
					//								offB++;
					//							else if (def.equals(name_rating_tmp[countoff][2]))
					//								defB++;
					//						}
					//						if (Math.abs(offB-defB)>1){
					//
					//							offdefBalance=false;
					//						}
					//						System.out.println("off diff B " +offB+" def B " +defB);
					//						System.out.println("off diff A " +offA+" def A " +defA);
					//
					//					}



					//decide whether odd numbers of players
					//probability is 0.25 
					odd=false;
					if(players_per_team!=min_players_per_team){
						int odd_rand=rand.nextInt(4)+1;//from 1-4
						System.out.println("oddrand is "+odd_rand);
						if(odd_rand==1){
						//	odd=true;
						}
					}





				}
				sa.ShuffleArray(name_rating_tmp);
				double sumA=0;
				double sumB=0;	

				System.out.println("player per team"+players_per_team);
				String[][] game= new String [players_per_team+1][6];//players_per_team+1 th row is score

				game=sa.splitTeam(name_rating_tmp,players_per_team,odd);


				//fill game
				System.out.println("odd is "+odd);
//				for (int p_count=0;p_count<players_per_team;p_count++){
//					game[p_count][0]=name_rating_tmp[p_count][0];//name
//					game[p_count][1]=name_rating_tmp[p_count][2];//off def
//					game[p_count][2]=name_rating_tmp[p_count][1];//rating
//
//					if(p_count==players_per_team-1 && odd==true){
//						game[p_count][3]="";
//						game[p_count][4]="";
//						game[p_count][5]="";
//					}
//					else{
//						game[p_count][3]=name_rating_tmp[p_count+players_per_team][0];
//						game[p_count][4]=name_rating_tmp[p_count+players_per_team][2];
//						game[p_count][5]=name_rating_tmp[p_count+players_per_team][1];	
//						sumB=sumB+ Double.parseDouble(game[p_count][5]);
//					}
//					sumA=sumA+ Double.parseDouble(game[p_count][2]);
//
//				}
				
				
				
				
				
				
				
				for (int counter=0;counter<players_per_team;counter++){
					sumA=sumA+Double.parseDouble(game[counter][2]);
					sumB=sumB+Double.parseDouble(game[counter][5]);
				}
				

				
				double avgA=sumA/players_per_team;
				double avgB=sumB/players_per_team;
				System.out.println("avg a is "+avgA+" avg b is "+avgB);
				double diff_offset=0;
				if (k%2==0){
					diff_offset=rand.nextDouble()*2-1;
					avgA=avgA+diff_offset;
					System.out.println("diff_offset  a is "+avgA);
				}
				System.out.println("ajusted avg a is "+avgA+" avg b is "+avgB);
				double diffAB=Math.abs(avgA-avgB);
				if (avgA==avgB || diffAB<0.27){
					game[players_per_team][5]="0";
					game[players_per_team][2]="0";
					score_count[0]=score_count[0]+1;

				}
				else if (avgA>avgB){
					double diff=(avgA-avgB)*2;
					if (1.5 <diff && diff <1.75){
						diff=1.4;
					}
					Long L = Math.round(diff);
					int score = Integer.valueOf(L.intValue());				
					game[players_per_team][2]=String.valueOf(score);
					game[players_per_team][5]="0";
					totalscore+=score;
					score_count[score]=score_count[score]+1;
					System.out.println("score scount is "+score_count[score]);
				}
				else{
					double diff=(avgB-avgA)*2;
					if (1.5 <diff && diff <1.75){
						diff=1.4;
					}
					Long L = Math.round(diff);
					int score = Integer.valueOf(L.intValue());				
					game[players_per_team][5]=String.valueOf(score);
					game[players_per_team][2]="0";
					totalscore+=score;
					score_count[score]=score_count[score]+1;
				}
				game[players_per_team][0]="Score";


				//output to file

				String gameid= "Game ID: ,"+String.valueOf(k+1);
				String [] gameidString = gameid.split(",");
				writer.writeNext(gameidString);
				String [] gameTitle = new String[6];
				gameTitle[0]="player id";
				gameTitle[1]="position";
				gameTitle[2]="Rating";
				gameTitle[3]="player id";
				gameTitle[4]="position";
				gameTitle[5]="Rating";
				writer.writeNext(gameTitle);
				for ( int m=0; m < players_per_team+1; m++) {




					String [] gameString = new String[6];
					for (int j=0;j<6;j++){
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
						if (gameStringDB[5]=="")
							gameStringDB[6]="";
						else
							gameStringDB[6]=game[players_per_team][3];
					}
					writerDB.writeNext(gameStringDB);
				}

				System.out.println("");

			}//end of for loop for each game


			avgscore=(double)totalscore/(double)num_of_game;
			System.out.println("totoal score: "+totalscore);
			System.out.println("avgscore: "+avgscore);
			System.out.println("score distribution is :");
			for (i=0;i<15;i++){
				double score_dis= (double)score_count[i]*100/(double)num_of_game;
				System.out.println("score diff "+i+" is "+score_dis +"%");
			}


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



