package br.eti.archanjo.webcron.api;

import br.eti.archanjo.webcron.dtos.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

abstract class GenericResource {
    private final Logger logger = LoggerFactory.getLogger(GenericResource.class);

    UserDTO getClient() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal == null) {
            return null;
        }

        try {
            return (UserDTO) principal;
        } catch (Exception e) {
            logger.error("GenericResource{getClient}", e);
        }
        return null;
    }
}