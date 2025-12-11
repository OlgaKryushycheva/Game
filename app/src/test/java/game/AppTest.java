package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void roundStateRevealsLetters() {
        RoundState state = new RoundState("ТЕСТ");
        assertFalse(state.solved());
        assertEquals(1, state.revealLetter('Т'));
        assertEquals(1, state.revealLetter('С'));
        assertEquals(2, state.revealLetter('Е'));
        assertTrue(state.solved());
    }

    @Test
    void scoreboardAccumulates() {
        Scoreboard scoreboard = new Scoreboard(new java.io.File("build/test-scoreboard.txt").toPath());
        scoreboard.recordScore("Player", 100);
        scoreboard.recordScore("Player", 50);
        assertEquals(150, scoreboard.top(1).get(0).score());
    }

    @Test
    void roundStateStoresHint() {
        RoundState state = new RoundState(new WordEntry("ТЕСТ", "підказка"));
        assertEquals("підказка", state.hint());
        assertEquals("ТЕСТ", state.targetWord());
    }
}
