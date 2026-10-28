package is.toxic.tbank.sdk.security;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

class NoSecretsInRepoTest {

    @Test
    void gradlePropertiesNotTrackedOrNoRealTokens() throws IOException {
        File gradleProps = new File("/home/GR/Проекты/t-bank-sdk/gradle.properties");
        if (gradleProps.exists()) {
            String content = Files.readString(gradleProps.toPath());
            assertThat(content).doesNotContain("ghp_");
            assertThat(content).doesNotContain("github_pat_");
        }
    }

    @Test
    void gitignoreHasBuild() throws IOException {
        File gitignore = new File("/home/GR/Проекты/t-bank-sdk/.gitignore");
        if (gitignore.exists()) {
            String content = Files.readString(gitignore.toPath());
            assertThat(content).contains("build/");
        }
    }
}
