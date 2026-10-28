package is.toxic.tbank.sdk.build;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

class BootJarDisabledTest {

    @Test
    void bootJarDisabled() throws IOException {
        File gradleFile = new File("/home/GR/Проекты/t-bank-sdk/build.gradle.kts");
        String content = Files.readString(gradleFile.toPath());
        assertThat(content).contains("bootJar");
        assertThat(content).contains("enabled = false");
    }
}
