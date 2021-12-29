/*
 * The MIT License (MIT) Copyright (c) 2020-2021 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.http.rq;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Tests for {@link RqParams}.
 *
 * @since 0.18
 */
public final class RqParamsTest {

    @ParameterizedTest
    @CsvSource({
            ",",
            "'',",
            "some=param,",
            "foo=bar,bar",
            "foo=,''",
            "some=param&foo=123,123",
            "foo=bar&foo=baz,bar",
            "foo=bar%26bobo,bar&bobo"
    })
    void findsParamValue(final String query, final String expected) {
        MatcherAssert.assertThat(
                new RqParams(query).value("foo"),
                new IsEqual<>(Optional.ofNullable(expected))
        );
    }


    static Stream<Arguments> stringQueryAndListOfParamValues() {
        return Stream.of(
                Arguments.arguments("", Collections.emptyList()),
                Arguments.arguments("''", Collections.emptyList()),
                Arguments.arguments("foo=", List.of("")),
                Arguments.arguments("prm=koko", Collections.emptyList()),
                Arguments.arguments("fo=bar&fy=baz", Collections.emptyList()),
                Arguments.arguments("foo=bar&fyi=baz", List.of("bar")),
                Arguments.arguments("foo=bar&foo=baz",List.of("bar","baz")),
                Arguments.arguments("foo=bar&key=ksu&foo=bobo",List.of("bar","bobo")),
                Arguments.arguments("foo=bar%26bobo",List.of("bar&bobo"))
        );
    }

    @ParameterizedTest
    @MethodSource("stringQueryAndListOfParamValues")
    void findsParamValues(final String query, final List<String> expected) {
        MatcherAssert.assertThat(
                new RqParams(query).values("foo"),
                new IsEqual<>(expected)
        );
    }


}
