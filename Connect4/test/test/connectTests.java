package test;
import core.Connect4Logic;
import core.Connect4ComputerPlayer;
import org.junit.jupiter.api.*;



public class connectTests {
    Connect4Logic pvp, pvp1;
    Connect4ComputerPlayer cpu;
    @BeforeEach
    void setUp(){
        pvp = new Connect4Logic();
        pvp1 = new Connect4Logic();
        cpu = new Connect4ComputerPlayer();
    }



    @Test
    @DisplayName("Horizontal win")
    void testHorizontal(){
        for(int i = 0; i < 4; i++){
            pvp.nextMove(i,0);
            pvp1.nextMove(i,1);
        }
        Assertions.assertTrue(pvp.getWin());
        Assertions.assertTrue(pvp1.getWin());
    }

    @Test
    @DisplayName("Vertical win")
    void testVertical(){
        cpu.nextMove(0,0);
        int j = 0;
        for(int i = 0; i < 4; i++){
            pvp.nextMove(j,0);
            pvp1.nextMove(j+1,1);
            cpu.nextMove(0,1);
        }
        Assertions.assertTrue(pvp.getWin());
        Assertions.assertTrue(pvp1.getWin());
        Assertions.assertTrue(cpu.getWin());
    }

    @Test
    @DisplayName("Diagonal Down win")
    void testDiagonalD(){
        pvp.nextMove(3,1);
        pvp.nextMove(2,0);
        pvp.nextMove(2,1);
        pvp.nextMove(1,0);
        pvp.nextMove(1,0);
        pvp.nextMove(1,1);
        pvp.nextMove(0,0);
        pvp.nextMove(0,0);
        pvp.nextMove(0,0);
        pvp.nextMove(0,1);

        Assertions.assertTrue(pvp.getWin());
    }

    @Test
    @DisplayName("Diagonal up win")
    void testDiagonalU(){
        pvp.nextMove(0,0);
        pvp.nextMove(1,1);
        pvp.nextMove(1,0);
        pvp.nextMove(2,1);
        pvp.nextMove(2,1);
        pvp.nextMove(2,0);
        pvp.nextMove(3,1);
        pvp.nextMove(3,1);
        pvp.nextMove(3,1);
        pvp.nextMove(3,0);

        Assertions.assertTrue(pvp.getWin());
    }

    @Test
    @DisplayName("Full column exception + out of bounds exception")
    void fullCol(){
        int t = 0;
        int j = 0;
        for(int i = 0; i < 6; i++){
            if(t == 0)
                t = 1;
            else
                t = 0;

            pvp.nextMove(0,t);
        }
        Assertions.assertThrows(RuntimeException.class,() -> pvp.nextMove(0,0));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> pvp1.nextMove(-1,0));
    }

    @Test
    @DisplayName("Draw board + reset")
    void drawReset(){
        int t = 0;
        int temp = 3;
        for(int i = 0; i < 6; i++){
            if(t == 0)
                t = 1;
            else
                t = 0;
            pvp.nextMove(temp,t);
        }
        t = 0;
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 7; j++){
                if(j == 3)
                    continue;
                pvp.nextMove(j,t);
            }
            if(t == 0)
                t = 1;
            else
                t = 0;
        }

        Assertions.assertTrue(pvp.getDraw());

        pvp.resetBoard();
        Assertions.assertFalse(pvp.getDraw());
    }

    @Test
    @DisplayName("Increased code coverage for cpu")
    void cpuCoverage(){
        int storage = 0;
        cpu.nextMove(0,1);
        char[][] temp = cpu.getBoard();
        for(int i = 0; i < 7; i++){
            if(temp[0][i] == 'O')
                storage = i;
        }
        cpu.nextMove(storage,0);
        cpu.nextMove(0,1);
        cpu.nextMove(0,1);
        cpu.nextMove(0,1);

        Assertions.assertTrue(cpu.getWin());
    }

}
