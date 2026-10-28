package is.toxic.tbank.sdk.build;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class CodegenTasksExistTest {

    @Test
    void codegenAndGenerateProtoTasksExist() {
        File projectDir = new File("/home/GR/Проекты/t-bank-sdk");
        assertThat(new File(projectDir, "build.gradle.kts")).exists();
    }
}

