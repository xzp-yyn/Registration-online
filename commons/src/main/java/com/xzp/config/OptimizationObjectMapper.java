package com.xzp.config;

import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.stereotype.Component;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.time.format.DateTimeFormatter.*;
/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/10/28 9:46
 * @Version 1.0
 */
/**
 * 对象映射器：序列化和反序列化时，数据格式指定。
 * 本转换器继承自Jackson中提供的转换核心类ObjectMapper
 * 支持
 *  指定格式的日期时间字符串与LocalDateTime的序列化和反序列化
 *  BigInteger、Long --> String的序列化
 */
    @Component
    public class OptimizationObjectMapper extends ObjectMapper {
        public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
        public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
        public OptimizationObjectMapper() {
            super();
            // 反序列化特性：反序列化时属性不存在的兼容处理，未知属性时不报异常
            this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
            this.getDeserializationConfig().withoutFeatures(FAIL_ON_UNKNOWN_PROPERTIES);

            // 新建功能模块，并添加序列化器和反序列化器
            SimpleModule simpleModule = new SimpleModule()
                    // 添加指定规则的反序列化器
                    .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                    .addDeserializer(LocalDate.class, new LocalDateDeserializer(ofPattern(DEFAULT_DATE_FORMAT)))
                    .addDeserializer(LocalTime.class, new LocalTimeDeserializer(ofPattern(DEFAULT_TIME_FORMAT)))

                    // 添加指定规则的序列化器
                    .addSerializer(BigInteger.class, ToStringSerializer.instance)
                    // Long --> String，可以解决本案中的问题
                    .addSerializer(Long.class, ToStringSerializer.instance)

                    .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern((DEFAULT_DATE_TIME_FORMAT))));
            //注册功能模块
            this.registerModule(simpleModule);
        }
    }
