package com.niat.cms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gtament
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Вы не можете редактировать этот материал")
public class UnauthorisedMEditException extends RuntimeException {
}