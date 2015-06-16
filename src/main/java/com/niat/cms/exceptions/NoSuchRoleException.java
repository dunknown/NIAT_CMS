package com.niat.cms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author dunknown
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Такой роли не существует")
public class NoSuchRoleException extends RuntimeException {
}
