package com.markscottwright.bastipasn1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.TracingParseRunner;

public class Asn1ParserTest {

    private Asn1Parser parser;

    @Test
    public void testPrimitives() {
        BasicParseRunner<Object> justIdentifierParser = new BasicParseRunner<>(
                parser.Sequence(parser.Identifier(), BaseParser.EOI));
        BasicParseRunner<Object> justTypeReferenceParser = new BasicParseRunner<>(
                parser.Sequence(parser.TypeReference(), BaseParser.EOI));

        assertTrue(justIdentifierParser.run("privateKeyAlgorithm").matched);
        assertFalse(justTypeReferenceParser.run("privateKeyAlgorithm").matched);
        assertTrue(justTypeReferenceParser.run("BIT STRING").matched);
    }

    @Test
    public void testEmbeddedCommentsFail() {
        BasicParseRunner<Object> justIdentifierParser = new BasicParseRunner<>(
                parser.Sequence(parser.Identifier(), BaseParser.EOI));

        assertFalse(justIdentifierParser.run("private--key").matched);
        assertTrue(justIdentifierParser.run("private-key").matched);
    }

    @Test
    public void testCommentParsing() {
        ParseRunner<Object> justCommentParser = new BasicParseRunner<>(
                parser.Sequence(parser.Comment(), BaseParser.EOI));

        assertTrue(justCommentParser.run(
                "-- this is a comment - it has internal dashes\n").matched);
        assertTrue(justCommentParser.run(
                "-- this is a comment - it has internal dashes--").matched);
        assertFalse(justCommentParser.run(
                "-- this is a comment - it has internal dashes-- ").matched);
    }

    @Before
    public void setUp() {
        parser = Parboiled.createParser(Asn1Parser.class);
    }
}
