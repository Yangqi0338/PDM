package com.base.sbc.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {


    /**
     * 处理数组类型的null值
     */
    public class NullArrayJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            if (value == null) {
                jgen.writeStartArray();
                jgen.writeEndArray();
            }
        }
    }

    public class NullMapJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            if (value == null) {
                jgen.writeStartObject();
                jgen.writeEndObject();
            }
        }
    }


    /**
     * 处理字符串等类型的null值
     */
    public class NullStringJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString(StringUtils.EMPTY);
        }
    }

    /**
     * 处理字符串等类型的null值
     */
    public class NullNumberJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeNumber(0);
        }
    }

    /**
     * 处理字符串等类型的null值
     */
    public class NullBooleanJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeBoolean(false);
        }
    }


    public class MyBeanSerializerModifier extends BeanSerializerModifier {


        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
            //循环所有的beanPropertyWriter
            for (Object beanProperty : beanProperties) {
                BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
                //判断字段的类型，如果是array，list，set则注册nullSerializer
                if (isArrayType(writer)) {
                    //给writer注册一个自己的nullSerializer
                    writer.assignNullSerializer(new NullArrayJsonSerializer());
                } else if (isMapType(writer)) {
                    writer.assignNullSerializer(new NullMapJsonSerializer());

                } else if (isDateType(writer)) {
                    writer.assignNullSerializer(new NullStringJsonSerializer());
//                    writer.assignSerializer(new JsonSerializer<Object>() {
//                        @Override
//                        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
//                            gen.writeString(DateUtils.formatDateTime((Date)value));
//                        }
//                    });
                }
//                else if (isNumberType(writer)) {
//                    writer.assignNullSerializer(new NullNumberJsonSerializer());
//                }
//                else if (isBooleanType(writer)) {
//                    writer.assignNullSerializer(new NullBooleanJsonSerializer());
//                }
                else if (isStringType(writer)) {
                    writer.assignNullSerializer(new NullStringJsonSerializer());
                }
            }
            return beanProperties;
        }

        /**
         * 是否是数组
         */
        private boolean isArrayType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
        }

        /**
         * 是否是map
         */
        private boolean isMapType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            if (Map.class.isAssignableFrom(clazz)) {
                return true;
            }
            return false;
        }

        /**
         * 是否是 日期格式
         *
         * @param writer
         * @return
         */
        private boolean isDateType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            if (Date.class.isAssignableFrom(clazz)) {
                return true;
            }
            return false;
        }

        /**
         * 是否是string
         */
        private boolean isStringType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
        }


        /**
         * 是否是int
         */
        private boolean isNumberType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return Number.class.isAssignableFrom(clazz);
        }

        /**
         * 是否是boolean
         */
        private boolean isBooleanType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.equals(Boolean.class);
        }

    }

    public JacksonHttpMessageConverter() {
        getObjectMapper().setSerializerFactory(getObjectMapper().getSerializerFactory().withSerializerModifier(new MyBeanSerializerModifier()));
    }
}
