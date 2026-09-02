package com.roh.jwtApplication.jwtService;

import com.roh.jwtApplication.entities.ApiPermission;
import com.roh.jwtApplication.repository.ApiPermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApiAuthorizationService {

    private final ApiPermissionRepository apiPermissionRepository;

    public ApiAuthorizationService(
            ApiPermissionRepository apiPermissionRepository) {

        this.apiPermissionRepository = apiPermissionRepository;
    }

    @Transactional
    public String getRequiredPermission(
            String httpMethod,
            String requestUri) {

        List<ApiPermission> apiPermissions =
                apiPermissionRepository.findByHttpMethodAndStatus(
                        httpMethod,
                        'Y'
                );

        for (ApiPermission apiPermission : apiPermissions) {

            if (matches(
                    apiPermission.getEndpoint(),
                    requestUri
            )) {

                return apiPermission
                        .getPermission()
                        .getName();
            }
        }

        return null;
    }

    private boolean matches(
            String configuredEndpoint,
            String requestUri) {

        if (configuredEndpoint.endsWith("/**")) {

            String prefix =
                    configuredEndpoint.substring(
                            0,
                            configuredEndpoint.length() - 3
                    );

            return requestUri.startsWith(prefix);
        }

        return configuredEndpoint.equals(requestUri);
    }
}