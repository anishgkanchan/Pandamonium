package com.example.logic;

import java.util.Arrays;

public class AILogic {
	char Mainplayer = 'X';

	public boolean end_state(char placement[][]) {
		int i = 0;
		while (i < 5) {
			int j = 0;
			while (j < 5) {
				if (placement[i][j] == '*')
					return false;
				j += 1;
			}
			i += 1;
		}
		return true;
	}

	public int evaluate(int values[][], char placement[][]) {
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
		if (Mainplayer == 'X')
			return current_X - current_O;
		else
			return current_O - current_X;
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

	public char[][] insert_player(int x, int y, char[][] placement, char player) {
		char opponent;
		if (player == 'X')
			opponent = 'O';
		else {
			opponent = 'X';
			player = 'O';
		}
		if (x - 1 >= 0 && placement[x - 1][y] == player)
			placement = raid(x, y, player, placement, placement);
		if (y - 1 >= 0 && placement[x][y - 1] == player)
			placement = raid(x, y, player, placement, placement);
		if (x + 1 <= 4 && placement[x + 1][y] == player)
			placement = raid(x, y, player, placement, placement);
		if (y + 1 <= 4 && placement[x][y + 1] == player)
			placement = raid(x, y, player, placement, placement);
		placement[x][y] = player;
		return placement;
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

	public char[][] raid(int x, int y, char player, char[][] placement,
			char[][] temp_placement) {
		char opponent;
		if (player == 'X')
			opponent = 'O';
		else {
			opponent = 'X';
			player = 'O';
		}
		if (x - 1 >= 0 && placement[x - 1][y] == opponent) {
			temp_placement[x][y] = player;
			temp_placement[x - 1][y] = player;
		}
		if (y - 1 >= 0 && placement[x][y - 1] == opponent) {
			temp_placement[x][y] = player;
			temp_placement[x][y - 1] = player;
		}
		if (x + 1 <= 4 && placement[x + 1][y] == opponent) {
			temp_placement[x][y] = player;
			temp_placement[x + 1][y] = player;
		}
		if (y + 1 <= 4 && placement[x][y + 1] == opponent) {
			temp_placement[x][y] = player;
			temp_placement[x][y + 1] = player;
		}

		return temp_placement;
	}

	public char[][] greedy_bfs(int[][] values, char[][] placement, char player) {
		char temp_placement[][] = new char[5][5];
		char opponent;
		char Mainplayer = 'X';
		int max_i = 0;
		int max_j = 0;
		int max_eval = -99999;
		char new_placement[][] = new char[5][5];
		int eval;

		if (player == 'X')
			opponent = 'O';
		else {
			opponent = 'X';
			player = 'O';
		}
		int i = 0;
		while (i < 5) {
			int j = 0;
			while (j < 5) {

				if (placement[i][j] == '*') {
					for (int k = 0; k < placement.length; k++) {
						temp_placement[k] = new char[placement[k].length];
						for (int l = 0; l < placement[k].length; l++) {
							temp_placement[k][l] = placement[k][l];
						}
					}
					if (i - 1 >= 0 && placement[i - 1][j] == Mainplayer)
						temp_placement = raid(i, j, player, placement,
								temp_placement);
					if (j - 1 >= 0 && placement[i][j - 1] == Mainplayer)
						temp_placement = raid(i, j, player, placement,
								temp_placement);
					if (i + 1 <= 4 && placement[i + 1][j] == Mainplayer)
						temp_placement = raid(i, j, player, placement,
								temp_placement);
					if (j + 1 <= 4 && placement[i][j + 1] == Mainplayer)
						temp_placement = raid(i, j, player, placement,
								temp_placement);
					temp_placement[i][j] = player;
					eval = evaluate(values, temp_placement);
					if (eval > max_eval) {
						max_i = i;
						max_j = j;
						max_eval = eval;
					}
				}

				j += 1;
			}
			i += 1;
		}
		if (end_state(placement) == false)
			new_placement = insert_player(max_i, max_j, placement, player);
		for (int z = 0; z < new_placement.length; z++)
			System.out.println(Arrays.toString(new_placement[z]));
		return new_placement;
	}

	public int alphabeta(int[][] values, char[][] placement, int depth,
			char player, int alpha, int beta) {

		int eval;
		int h = 0;
		char[][] temp_placement = new char[5][5];
		int indexab_i = 0;
		int indexab_j = 0;
		int depth_counter;
		depth_counter = depth;
		char next_player;
		char new_placement[][] = new char[5][5];
		int a;
		int b;
		int abreturn = 0;
		if (end_state(placement) == true)
			return evaluate(values, placement);
		if (player == 'X')
			next_player = 'O';
		else {
			next_player = 'X';
			player = 'O';
		}
		if (depth_counter == 0)
			return evaluate(values, placement);
		int i = 0;
		while (i < 5) {
			int j = 0;
			while (j < 5) {
				if (placement[i][j] == '*') {
					for (int k = 0; k < placement.length; k++) {
						temp_placement[k] = new char[placement[k].length];
						for (int l = 0; l < placement[k].length; l++) {
							temp_placement[k][l] = placement[k][l];
						}
					}
					if (i - 1 >= 0 && placement[i - 1][j] == player)
						temp_placement = raid(i, j, player, placement,
								temp_placement);
					if (j - 1 >= 0 && placement[i][j - 1] == player)
						temp_placement = raid(i, j, player, placement,
								temp_placement);
					if (i + 1 <= 4 && placement[i + 1][j] == player)
						temp_placement = raid(i, j, player, placement,
								temp_placement);
					if (j + 1 <= 4 && placement[i][j + 1] == player)
						temp_placement = raid(i, j, player, placement,
								temp_placement);
					temp_placement[i][j] = player;
					eval = evaluate(values, temp_placement);
					if (player == Mainplayer) {
						a = alphabeta(values, temp_placement,
								depth_counter - 1, next_player, alpha, beta);
						if (a > abreturn) {
							abreturn = a;
						}
						if (abreturn >= beta) {
							indexab_i = i;
							indexab_j = j;
							return abreturn;
						}
						if (abreturn > alpha) {
							alpha = abreturn;
						}
						indexab_i = i;
						indexab_j = j;
					} else {
						b = alphabeta(values, temp_placement,
								depth_counter - 1, next_player, alpha, beta);
						if (b < abreturn) {
							abreturn = b;
						}
						if (abreturn <= alpha)
							return abreturn;
						if (abreturn < beta) {
							beta = abreturn;
						}
					}// else
				} // if main

				j += 1;
			} // while
			i += 1;
		} // while
		if (end_state(placement) == false)
			new_placement = insert_player(indexab_i, indexab_j, placement,
					player);
		for (int z = 0; z < new_placement.length; z++)
			System.out.println(Arrays.toString(new_placement[z]));

		return abreturn;
	}// def

}

// class Main{
// public static void main (String args[])
// {
// char Mainplayer='X';
// AILogic gbfs=new AILogic();
// int values[][] = new int[][]{
// {20,16,1,32,30},{20,12,2,11,8},{28,48,9,1,1},{20,12,10,6,2},{25,30,23,21,60}
// };
// int i=0;
// int j=0;
// char Piece_Placement[][]=new
// char[][]{{'*','*','X','X','*'},{'*','*','X','O','X'},{'*','O','*','O','*'},
// {'*','*','O','O','*'},{'*','*','*','*','*'}};
// System.out.print(gbfs.greedy_bfs(values,Piece_Placement,Mainplayer));
// //System.out.println(gbfs.alphabeta(values, Piece_Placement, 2, Mainplayer,
// -10000, 10000));
//
//
//
//
// }
// }
