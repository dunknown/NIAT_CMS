package com.niat.cms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gtament
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "You can't edit this material")
public class UnauthorisedMEditException extends RuntimeException {
}