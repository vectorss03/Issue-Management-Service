package test.java;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ApplicationExampleTests {
    private final ApplicationExample applicationExample = new ApplicationExample();

    @Test
    @DisplayName("Hello World Test")
    public void helloWorld() {
        assertThat(applicationExample.helloWorld()).isEqualTo("Hello World!");
    }

    @Test
    @DisplayName("Hello Worlds Test")
    public void helloWorlds() {
        assertThat(applicationExample.helloWorld()).isEqualTo("Hello World!");
    }
}
