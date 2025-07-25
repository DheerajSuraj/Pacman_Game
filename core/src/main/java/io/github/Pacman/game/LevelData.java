package io.github.Pacman.game;

public class LevelData {
    public static int[][] getLayout(int level) {
        switch (level) {
            case 2: return level2();
            case 3: return level3(); // now with no loops
            case 4: return level4(); // newly added
            case 5: return level5(); // newly added
            default: return level1();
        }
    }

    private static int[][] level1() {
        return new int[][] {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,1,0,0,1,0,0,0,1,0,0,0,0,1},
            {1,0,1,1,1,1,0,1,1,0,1,1,1,0,1,1,1,1,0,1},
            {1,0,1,0,0,1,0,0,1,0,0,0,1,0,0,0,0,1,0,1},
            {1,0,1,0,1,1,1,0,1,1,1,0,1,1,1,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1},
            {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1},
            {1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,1},
            {1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1},
            {1,0,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
    }

    private static int[][] level2() {
        return new int[][] {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,1,1,0,1,0,1,1,1,1,1,1,1,0,1},
            {1,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,1,0,1},
            {1,0,1,1,1,1,0,1,1,0,1,1,0,1,1,1,0,1,0,1},
            {1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1},
            {1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1},
            {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1},
            {1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,1},
            {1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1},
            {1,1,0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
    }

    private static int[][] level3() {
        return new int[][] {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,1,1,1,0,1,0,1,1,1,1,1,1,0,1},
            {1,0,1,0,0,0,0,0,1,0,0,0,1,0,0,0,0,1,0,1},
            {1,0,1,0,1,0,1,1,1,1,1,0,1,1,1,1,0,1,0,1},
            {1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
            {1,1,1,1,1,0,1,1,0,1,0,1,1,0,1,1,1,1,1,1},
            {1,0,0,0,0,0,1,1,0,1,0,1,1,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,1,1,0,1,0,1,1,1,1,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1},
            {1,1,1,1,1,1,0,1,0,1,1,1,1,1,1,1,0,1,0,1},
            {1,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,1,0,1},
            {1,0,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
    }

    private static int[][] level4() { return level1(); }

    private static int[][] level5() { return level2(); }
}
