package com.niat.cms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gtament
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User can't change own role")
public class UserChangedOwnRole extends RuntimeException {
}