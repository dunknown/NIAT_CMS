package com.niat.cms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gtament
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Ошибка индекса премещенного материала")
public class BadSortMainIndexException extends RuntimeException {
}
