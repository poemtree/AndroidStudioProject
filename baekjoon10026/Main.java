package baekjoon10026;

/* [문제]
적록색약은 빨간색과 초록색의 차이를 거의 느끼지 못한다. 따라서, 적록색약인 사람이 보는 그림은 아닌 사람이 보는 그림과는 좀 다를 수 있다.

크기가 N×N인 그리드의 각 칸에 R(빨강), G(초록), B(파랑) 중 하나를 색칠한 그림이 있다. 그림은 몇 개의 구역으로 나뉘어져 있는데, 구역은 같은
색으로 이루어져 있다. 또, 같은 색상이 상하좌우로 인접해 있는 경우에 두 글자는 같은 구역에 속한다. (색상의 차이를 거의 느끼지 못하는 경우도 같은 색상이라 한다)

예를 들어, 그림이 아래와 같은 경우에

RRRBB
GGBBB
BBBRR
BBRRR
RRRRR

적록색약이 아닌 사람이 봤을 때 구역의 수는 총 4개이다. (빨강 2, 파랑 1, 초록 1) 하지만, 적록색약인 사람은 구역을 3개 볼 수 있다. (빨강-초록 2, 파랑 1)

그림이 입력으로 주어졌을 때, 적록색약인 사람이 봤을 때와 아닌 사람이 봤을 때 구역의 수를 구하는 프로그램을 작성하시오.
 */

/* [입력]
첫째 줄에 N이 주어진다. (1 ≤ N ≤ 100)

둘째 줄부터 N개 줄에는 그림이 주어진다.
 */

/* [출력]
적록색약이 아닌 사람이 봤을 때의 구역의 개수와 적록색약인 사람이 봤을 때의 구역의 수를 공백으로 구분해 출력한다.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Main {
	public static int n;
	public static char[][] grid;
	public static int[][] visit;
	public static int[] dx = {0, 1, -1, 0};
	public static int[] dy = {-1, 0, 0, 1};
	public static int cntNonBlind;
	public static int cntBlind;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		grid = new char[n][n];
		visit = new int[n][n];
		for(int i=0; i<n; i++) {
			grid[i] = br.readLine().toCharArray();
		}
		for(int i=0; i<n; i++) {
			for(int y=0; y<n; y++) {
				cntNonBlind += dfsNonBlind(grid[i][y], i, y)?1:0;
			}
		}
		System.out.print(cntNonBlind+ " ");
		visit = new int[n][n];
		for(int i=0; i<n; i++) {
			for(int y=0; y<n; y++) {
				cntBlind += dfsBlind(grid[i][y], i, y)?1:0;
			}
		}
		System.out.print(cntBlind);
		br.close();
	}
	public static boolean dfsNonBlind(char c, int x, int y) {
		int nx;
		int ny;
		if(grid[x][y]==c && visit[x][y] == 0 ) {
			visit[x][y]++;
			for(int i=0; i<4; i++){
				nx = x + dx[i];
				ny = y + dy[i];
				if(nx >= 0 && nx < n && ny >= 0 && ny < n) {
					dfsNonBlind(grid[x][y], nx, ny);
				}
			};
			return true;
		} else {
			return false;
		}
	}
	public static boolean dfsBlind(int c, int x, int y) {
		int nx;
		int ny;
		if((grid[x][y] == c || grid[x][y]-c == 11 || grid[x][y]-c == -11)&& visit[x][y] == 0 ) {
			visit[x][y]++;
			for(int i=0; i<4; i++){
				nx = x + dx[i];
				ny = y + dy[i];
				if(nx >= 0 && nx < n && ny >= 0 && ny < n) {
					dfsBlind(grid[x][y], nx, ny);
				}
			}
			return true;
		} else {
			return false;
		}
	}
}