package com.xhwl.commons.utils;


import com.xhwl.commons.enumPackage.ExceptionEnum;
import com.xhwl.commons.exceptions.XhException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
public class BeanHelper {

    public static <T> T copyProperties(Object source, Class<T> target){
        try {
            T t = target.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new XhException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
    }

    public static <T> List<T> copyWithCollection(List<?> sourceList, Class<T> target){
        try {
            return sourceList.stream().map(s -> copyProperties(s, target)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new XhException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
    }

    public static <T> Set<T> copyWithCollection(Set<?> sourceList, Class<T> target){
        try {
            return sourceList.stream().map(s -> copyProperties(s, target)).collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new XhException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
    }
}
