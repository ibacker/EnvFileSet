package net.ashald.envfile.providers.k8s;

import net.ashald.envfile.AbstractEnvVarsProvider;
import net.ashald.envfile.EnvFileErrorException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class K8sEnvFileParser extends AbstractEnvVarsProvider {

    public K8sEnvFileParser(boolean shouldSubstituteEnvVar) {
        super(shouldSubstituteEnvVar);
    }

    @NotNull
    @Override
    public Map<String, String> getEnvVars(@NotNull Map<String, String> runConfigEnv, @NotNull String path) throws EnvFileErrorException, IOException {
        Map<String, String> result;
        YamlTool tool = new YamlTool();
        try (InputStream input = new FileInputStream(new File(path))) {
            tool.initWithStream(input);
            result = tool.getEnvList();
        } catch (ClassCastException e) {
            throw new EnvFileErrorException(
                    String.format("Cannot read '%s' as YAML dict - not all keys and/or values are strings", path), e);
        } catch (FileNotFoundException e) {
            throw new EnvFileErrorException(e);
        }
        if (result == null) {
            result = new HashMap<>();
        }
        return result;
    }
}
