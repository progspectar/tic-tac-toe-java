package pa6elz;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        game.printGame();
        boolean isEndOfGame = false;
        String x, y;
        Scanner scan = new Scanner(System.in);

        do {
            getCoordinates(scan, game);
            game.makeMove();
            game.changeCurrentPlayer();
        } while (!game.IsItEndOfGame());
        System.out.println(game.getGameState());
    }

    public static void getCoordinates(Scanner scan, Game game) {
        String inputString;
        do {
            System.out.println("Current player is: "+game.getCurrentPlayer());
            System.out.print("Enter the comma delimited coordinates (x, y):");
            inputString = scan.next();

        } while (game.isUserInputHasError(inputString));
    }
}

class Game {
    public final static int[][] LINES_TO_CHECK = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    public GameState gameState;
    public int currentIndexOfGrid;
    private Player currentPlayer;
    private char[] gameGrid;

    Game() {
        currentPlayer = Player.X;
        gameState = GameState.GameNotFinished;
        gameGrid = "_________".toCharArray();
    }

    public static int calcIndex(int x, int y) {
        return x + 3 * (3 - y) - 1;
    }

    public void changeCurrentPlayer() {
            currentPlayer = (currentPlayer == Player.X) ? Player.O : Player.X;
       }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isUserInputHasError(String inputSting) {

        int intX;
        int intY;

        String[] tokens = inputSting.split(",");
       if (tokens.length != 2)  {
           System.out.println("Wrong input!");
           return true;
       }

        try {
            intX = Integer.valueOf(tokens[0]);
            intY = Integer.valueOf(tokens[1]);
        } catch (Exception e) {
            System.out.println("You should enter numbers!");
            return true;
        }
        int index = calcIndex(intX, intY);

        if (intX == 0 || intX > 3 || intY == 0 || intY > 3) {
            System.out.println("Coordinates should be from 1 to 3!");
            return true;
        } else if (gameGrid[index] != '_' && gameGrid[index] != ' ') {
            System.out.println("This cell is occupied! Choose another one!");
            return true;
        }
        currentIndexOfGrid = index;
        return false;
    }

    public boolean IsItEndOfGame() {
        return (gameState == GameState.OWins || gameState == GameState.XWins || gameState == GameState.Draw);
    }

    public void makeMove() {

        gameGrid[currentIndexOfGrid] = getCurrentPlayer().value.charAt(0);
        gameState = getGameState();
        printGame();
    }

    public GameState getGameState() {
        boolean isXWin = false;
        boolean isOWin = false;
        boolean isEmpty = false;
        int sumX = 0;
        int sumO = 0;
        GameState result = gameState;

        for (char ch : gameGrid) {
            if (ch == 'X') {
                sumX++;
            }
            if (ch == 'O') {
                sumO++;
            }
        }
        for (int[] row : LINES_TO_CHECK) {
            int countX = 0;
            int countO = 0;
            int countE = 0;

            for (int val : row) {
                countX += (gameGrid[val] == 'X') ? 1 : 0;
                countO += (gameGrid[val] == 'O') ? 1 : 0;
                countE += (gameGrid[val] == '_' | gameGrid[val] == ' ') ? 1 : 0;
            }
            if (countX == 3) {
                isXWin = true;
            }
            if (countO == 3) {
                isOWin = true;
            }
            if (countE > 0) {
                isEmpty = true;
            }
        }
        int diff = sumO - sumX;
        if (diff <= -2 || diff >= 2) {
            result = GameState.Impossible;
        } else if (isEmpty & !isOWin & !isXWin) {
            result = GameState.GameNotFinished;
        } else if (!isEmpty & !isOWin & !isXWin) {
            result = GameState.Draw;
        } else if (isXWin && isOWin) {
            result = GameState.Impossible;
        } else if (isXWin) {
            result = GameState.XWins;
        } else if (isOWin) {
            result = GameState.OWins;
        }
        return result;
    }

    public void printGame() {
        System.out.println("---------");
        for (int i = 0; i < 9; i += 3)
            System.out.println(String.format("| %c %c %c |", gameGrid[i], gameGrid[i + 1], gameGrid[i + 2]));
        System.out.println("---------");
    }

    public enum GameState {
        GameNotFinished("Game not finished"),
        Draw("Draw"),
        XWins("X wins"),
        OWins("O wins"),
        Impossible("Impossible");

        private String value;

        GameState(String str) {
            this.value = str;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
    public enum Player {
        X("X"),
        O("O");

        private String value;

        Player(String str) {
            this.value = str;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }
}
