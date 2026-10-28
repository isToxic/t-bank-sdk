package is.toxic.tbank.sdk.build;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

class SourceSetsTest {

    @Test
    void buildGradleHasGeneratedSourceSets() throws IOException {
        File gradleFile = new File("/home/GR/Проекты/t-bank-sdk/build.gradle.kts");
        String content = Files.readString(gradleFile.toPath());
        assertThat(content).contains("generated/sources/open-api");
        assertThat(content).contains("generated/sources/proto");
        assertThat(content).contains("src/main/java");
    }
}
