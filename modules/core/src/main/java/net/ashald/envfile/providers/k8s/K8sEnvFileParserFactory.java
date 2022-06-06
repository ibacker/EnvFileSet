package net.ashald.envfile.providers.k8s;

import net.ashald.envfile.EnvVarsProvider;
import net.ashald.envfile.EnvVarsProviderFactory;
import org.jetbrains.annotations.NotNull;

public class K8sEnvFileParserFactory implements EnvVarsProviderFactory {

    @Override
    public @NotNull
    EnvVarsProvider createProvider(boolean shouldSubstituteEnvVar) {
        return new K8sEnvFileParser(shouldSubstituteEnvVar);
    }

    @NotNull
    public String getTitle() {
        return "K8s/YAML";
    }

}
