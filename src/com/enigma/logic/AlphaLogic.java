package com.enigma.logic;


import java.util.Arrays;
public class AlphaLogic {
	char Mainplayer='X';
	int indexab_i=0; 
    int indexab_j=0; 
	public boolean end_state(char placement[][])
	{
	    int i=0;
	    while (i<5) 
	    	{
	    	int j=0;
	    	while (j<5)
	    		{
	            if (placement[i][j]=='*') 
	                return false;
	                j+=1;
	    		}
	    	 i+=1;
	    	}
	    return true;
	}   		
	
	public boolean isRaid(int x, int y, char[][] placement, char player) {
		if (x - 1 >= 0 && placement[x - 1][y] == player)
			return true;
		if (y - 1 >= 0 && placement[x][y - 1] == player)
			return true;
		if (x + 1 <= 4 && placement[x + 1][y] == player)
			return true;
		if (y + 1 <= 4 && placement[x][y + 1] == player)
			return true;
		return false;
	}
	
	public int getScore(int values[][], char placement[][], char player) {
		int current_X = 0;
		int current_O = 0;
		int i = 0;
		while (i < 5) {
			int j = 0;
			while (j < 5) {
				if (placement[i][j] == 'X')
					current_X += values[i][j];
				if (placement[i][j] == 'O')
					current_O += values[i][j];
				j += 1;
			}
			i += 1;
		}
		if(player=='X')
			return current_X;
		else
			return current_O;
				
	}
	
	public int evaluate(int values[][],char placement[][]) 
	{
		   int current_X=0;
		   int current_O=0;
		   int i=0;
		   while (i<5)
		   {
		       int j=0;
		       while (j<5)
		       {
		            if (placement[i][j]=='X') 
		                current_X += values[i][j];
		            if (placement[i][j]=='O') 
		                current_O +=values[i][j];
		            j+=1;
		       }
		       i+=1;
		   }
		   if (Mainplayer=='X')
		    return current_X-current_O;
		   else 
		    return current_O-current_X;
	}
	public char[][] raid(int x,int y,char player,char[][] placement,char[][] temp_placement)
	{
		char opponent;
	    if (player=='X')
	        opponent='O';
	    else
	    {
	        opponent='X';
	        player='O';
	    }
	    if (x-1>=0 && placement[x-1][y]==opponent) 
	    {
	                        temp_placement[x][y]=player;
	                        temp_placement[x-1][y]=player;
	    }
	    if (y-1>=0 && placement[x][y-1]==opponent)
	    {
	                        temp_placement[x][y]=player;
	                        temp_placement[x][y-1]=player;
	    }
	    if (x+1<=4 && placement[x+1][y]==opponent)
	    {
	                        temp_placement[x][y]=player;
	                        temp_placement[x+1][y]=player;
	    }
	    if (y+1<=4 && placement[x][y+1]==opponent)
	    {
	                        temp_placement[x][y]=player;
	                        temp_placement[x][y+1]=player;
	    }

	    return temp_placement;
	}
	public char[][] insert_player(int x,int y,char[][] placement,char player)
	{
		char opponent;
	    if (player=='X')
	        opponent='O';
	    else
	    {
	        opponent='X';
	        player='O';
	    }
	    if (x-1>=0 && placement[x-1][y]==player)
	                        placement=raid(x,y,player,placement,placement);
	    if (y-1>=0 && placement[x][y-1]==player)
	                         placement=raid(x,y,player,placement,placement);
	    if (x+1<=4 && placement[x+1][y]==player)
	                         placement=raid(x,y,player,placement,placement);
	    if (y+1<=4 && placement[x][y+1]==player)
	                         placement=raid(x,y,player,placement,placement);
	    placement[x][y]=player;
	    return placement;
	}
	
	public char[][] greedy_bfs(int[][] values,char[][] placement,char player)
	{
		   char temp_placement[][] = new char[5][5];
		   char opponent;
		   char Mainplayer='X';
		   int max_i=0;
		   int max_j=0;
		   int max_eval=0;
		   char new_placement[][]=new char[5][5];
		   int eval;

				   if (player=='X')
				        opponent='O';
				   else
				   {
				        opponent='X';
				        player='O';
				   }
				   int i=0;
				   while (i<5)
				   {
				       int j=0;
				       while (j<5)
				       {


				                if (placement[i][j]=='*')
				                {
				                	for(int k = 0; k < placement.length; k++)
				                	{
				                	  temp_placement[k] = new char[placement[k].length];
				                	  for (int l = 0; l < placement[k].length; l++)
				                	  {
				                	    temp_placement[k][l] = placement[k][l];
				                	  }
				                	}
				                    if (i-1>=0 && placement[i-1][j]==Mainplayer)
				                            temp_placement=raid(i,j,player,placement,temp_placement);
				                    if (j-1>=0 && placement[i][j-1]==Mainplayer)
				                            temp_placement=raid(i,j,player,placement,temp_placement);
				                    if (i+1<=4 && placement[i+1][j]==Mainplayer)
				                            temp_placement=raid(i,j,player,placement,temp_placement);
				                    if (j+1<=4 && placement[i][j+1]==Mainplayer)
				                            temp_placement=raid(i,j,player,placement,temp_placement);
				                    temp_placement[i][j]=player;
				                    eval=evaluate(values,temp_placement);
				                    if (eval>max_eval)
				                    {
				                    	max_i=i;
				                    	max_j=j;
				                    	max_eval=eval;
				                    }
				                }

				                j+=1;
				       }
				       i+=1;
				   }
				   if (end_state(placement)==false)
				        new_placement=insert_player(max_i,max_j,placement,player);
				   for(int z=0;z<new_placement.length;z++)
					   System.out.println(Arrays.toString(new_placement[z]));
				   return new_placement;
	}
	public int secondhighest(int[] arrayofnumbers) 
    { 
        int highest=Integer.MIN_VALUE; 
        int secondhighest=Integer.MIN_VALUE; 
        for (int i = 0; i < arrayofnumbers.length; i++) { 
 
            if (arrayofnumbers[i] > highest) { 
 
                // ...shift the current highest number to second highest 
                secondhighest = highest; 
 
                // ...and set the new highest. 
                highest = arrayofnumbers[i]; 
            } else if (arrayofnumbers[i] > secondhighest && arrayofnumbers[i] < highest) 
                // Just replace the second highest 
                secondhighest = arrayofnumbers[i]; 
            } 
        return secondhighest; 
        } 
    public int minimum(int[] arrayofnumbers) 
    { 
        int minimum=Integer.MAX_VALUE; 
        for (int i = 0; i < arrayofnumbers.length; i++) { 
            if (arrayofnumbers[i] < minimum) { 
 
                minimum = arrayofnumbers[i]; 
            }  
        } 
         
        return minimum; 
    } 
	public int alphabeta(int[][] values,char[][] placement,int depth,char player,int alpha,int beta) 
    { 
         
        int eval; 
        int h=0; 
        char[][] temp_placement = new char[5][5]; 
        int depth_counter; 
        depth_counter=depth; 
        char next_player; 
        char new_placement[][]=new char[5][5]; 
        int a=0; 
        int b=0; 
        int abreturn = 0; 
        boolean flag=true; 
        int check[]=new int[25]; 
        for (int k=0;k<check.length;k++) 
        { 
            check[k]=Integer.MAX_VALUE; 
        } 
        int check1[]=new int[25]; 
        for (int k=0;k<check1.length;k++) 
        { 
            check1[k]=Integer.MAX_VALUE; 
        } 
        int index=0; 
        if (end_state(placement)==true) 
            return evaluate(values,placement); 
        if (player=='X') 
            next_player='O'; 
        else  
        { 
            next_player='X'; 
            player='O'; 
        } 
        if (depth_counter==0) 
        { 
            return evaluate(values,placement); 
        } 
        for (int i=0;i<5;i++) 
        { 
             
            for (int j=0;j<5;j++) 
            { 
                if (placement[i][j]=='X' || placement[i][j]=='O') 
                   { 
                    if (player== Mainplayer) 
                        check[i*5+j]=Integer.MAX_VALUE; 
                    else 
                        check1[i*5+j]=Integer.MAX_VALUE; 
                   } 
                if (placement[i][j]=='*') 
                { 
                    for(int k = 0; k < placement.length; k++) 
                    { 
                      temp_placement[k] = new char[placement[k].length]; 
                      for (int l = 0; l < placement[k].length; l++) 
                      { 
                        temp_placement[k][l] = placement[k][l]; 
                      } 
                    } 
                    if (i-1>=0 && placement[i-1][j]==player) 
                            temp_placement=raid(i,j,player,placement,temp_placement); 
                    if (j-1>=0 && placement[i][j-1]==player) 
                            temp_placement=raid(i,j,player,placement,temp_placement); 
                    if (i+1<=4 && placement[i+1][j]==player) 
                            temp_placement=raid(i,j,player,placement,temp_placement); 
                    if (j+1<=4 && placement[i][j+1]==player) 
                            temp_placement=raid(i,j,player,placement,temp_placement); 
                    temp_placement[i][j]=player; 
                    if (player==Mainplayer) 
                    { 
                    a=alphabeta(values,temp_placement,depth_counter-1,next_player,alpha,beta); 
                    
                    check[i*5+j]=a; 
                    h=secondhighest(check); 
                        if (h>=beta) 
                        { 
                            indexab_i=i; 
                            indexab_j=j; 
                            return h; 
                        } 
                        if (h>alpha) 
                        { 
                            alpha=h; 
                        } 
                        for (int k=0;k<check.length;k++) 
                        { 
                            if (check[k]==h) 
                            { 
                                index=k; 
                                break; 
                            } 
                        } 
                         
                        indexab_i=index/5; 
                        indexab_j=index%5; 
                         
                                                
                    } 
                    else 
                    { 
                        b=alphabeta(values,temp_placement,depth_counter-1,next_player,alpha,beta); 
                        check1[i*5+j]=b; 
                        h=minimum(check1); 
                        if (h<=alpha) 
                            return h; 
                        if (h<beta) 
                        { 
                            beta=h; 
                        } 
                         
                    }//else 
                }  //if main 
                 
            } //for 
            
        }    //for 
         
         
        return h; 
    }//def 
	public char[][] CallAlpha(int[][] values,char[][] placement,int depth,char player,int alpha,int beta){
		char newplacement[][]=new char[5][5];
		int h=0;
		h= alphabeta(values,placement,depth,player,alpha,beta);
		
		newplacement= insert_player(indexab_i,indexab_j,placement,player);
		
		return newplacement;
		
		
		
	}
	
	
	
	

}


//class Main{
//	public static void main (String args[])
//	{
//		char Mainplayer='X';
//		AlphaLogic gbfs=new AlphaLogic();
//		char newplacement[][]=new char[5][5];
//		int values[][] = new int[][]{
//				{20,16,1,32,30},{20,12,2,11,8},{28,48,9,1,1},{20,12,10,6,2},{25,30,23,21,60}
//		};
//		int i=0;
//		int j=0;
//		char Piece_Placement[][]=new char[][]{{'*','*','X','X','*'},{'*','*','X','O','X'},{'*','O','*','O','*'},
//				{'*','*','O','O','*'},{'*','*','*','*','*'}};
//		//System.out.print(gbfs.greedy_bfs(values,Piece_Placement,Mainplayer));
//		newplacement=gbfs.CallAlpha(values, Piece_Placement, 2, Mainplayer, -10000, 10000);
//		 for(int z=0;z<newplacement.length;z++) 
//             System.out.println(Arrays.toString(newplacement[z]));
//		
//		
//		
//		
//		
//	}
//}
