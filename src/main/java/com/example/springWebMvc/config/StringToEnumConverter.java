package com.example.springWebMvc.config;

import com.example.springWebMvc.persistent.OrderStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, OrderStatus> {
    @Override
    public OrderStatus convert(String source) {
        return OrderStatus.valueOf(source);
    }

}
