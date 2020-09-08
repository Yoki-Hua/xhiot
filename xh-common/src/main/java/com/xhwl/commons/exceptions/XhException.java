package com.xhwl.commons.exceptions;

import com.xhwl.commons.enumPackage.ExceptionEnum;
import lombok.Data;

@Data
public class XhException extends RuntimeException {
    private int status;

    public XhException(ExceptionEnum em) {
        super(em.getMessage());
        this.status = em.getStatus();
    }

    public XhException(ExceptionEnum em, Throwable cause) {
        super(em.getMessage(), cause);
        this.status = em.getStatus();
    }
}
