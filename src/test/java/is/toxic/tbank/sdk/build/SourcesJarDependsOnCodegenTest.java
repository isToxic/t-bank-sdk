package is.toxic.tbank.sdk.build;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

class SourcesJarDependsOnCodegenTest {

    @Test
    void sourcesJarDependsOnCodegen() throws IOException {
        File gradleFile = new File("/home/GR/Проекты/t-bank-sdk/build.gradle.kts");
        String content = Files.readString(gradleFile.toPath());
        assertThat(content).contains("sourcesJar");
        assertThat(content.contains("openApiGenerate") || content.contains("codegen")).isTrue();
        assertThat(content).contains("generateProto");
    }
}

