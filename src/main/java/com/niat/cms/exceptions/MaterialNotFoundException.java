package com.niat.cms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gtament
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Такого материала не существует")
public class MaterialNotFoundException extends RuntimeException {
}
