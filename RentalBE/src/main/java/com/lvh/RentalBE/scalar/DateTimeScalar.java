package com.lvh.RentalBE.scalar;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeScalar {

    public static GraphQLScalarType DateTime = GraphQLScalarType.newScalar()
            .name("DateTime")
            .description("Custom DateTime scalar for LocalDateTime")
            .coercing(new Coercing<LocalDateTime, String>() {
                @Override
                public String serialize(Object dataFetcherResult) {
                    // Chuyển từ LocalDateTime thành String khi gửi ra ngoài
                    return ((LocalDateTime) dataFetcherResult).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }

                @Override
                public LocalDateTime parseValue(Object input) {
                    // Chuyển từ String thành LocalDateTime khi nhận dữ liệu
                    return LocalDateTime.parse(input.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }

                @Override
                public LocalDateTime parseLiteral(Object input) {
                    // Chuyển từ chuỗi literal của GraphQL thành LocalDateTime
                    return LocalDateTime.parse(((StringValue) input).getValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }
            }).build();
}
