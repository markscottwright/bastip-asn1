package com.markscottwright.bastipasn1;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;

/**
 * @author wrightm
 *
 */
@BuildParseTree
public class Asn1Parser extends BaseParser<Object> {

    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CASE = LOWER_CASE.toUpperCase();
    private static final String DIGITS = "1234567890";

    /**
     * A field or value - starts with lower case letter
     */
    Rule Identifier() {
        Rule identifierWithoutSpaces = Sequence(AnyOf(LOWER_CASE),
                ZeroOrMore(AnyOf(LOWER_CASE + UPPER_CASE + DIGITS)),
                ZeroOrMore(
                        Sequence('-', AnyOf(LOWER_CASE + UPPER_CASE + DIGITS))),
                ZeroOrMore(AnyOf(LOWER_CASE + UPPER_CASE + DIGITS)));
        return Sequence(identifierWithoutSpaces,
                ZeroOrMore(' ', identifierWithoutSpaces));
    }

    Rule Space() {
        return AnyOf(" \t\n");
    }

    /**
     * A rule that matches --blah blah\n or --blah blah--
     */
    Rule Comment() {
        return FirstOf(Sequence("--", ZeroOrMore(NoneOf("\n")), "\n"),
                Sequence("--",
                        Sequence(ZeroOrMore(NoneOf("\n-")),
                                ZeroOrMore(Sequence('-', NoneOf("\n-"))),
                                ZeroOrMore(NoneOf("\n-"))),
                        "--"));
    }

    /**
     * The name of a type - starts with upper case letter
     */
    Rule TypeReference() {
        Rule typeReferenceWithoutSpaces = Sequence(AnyOf(UPPER_CASE),
                ZeroOrMore(AnyOf(LOWER_CASE + UPPER_CASE + DIGITS)),
                ZeroOrMore(
                        Sequence('-', AnyOf(LOWER_CASE + UPPER_CASE + DIGITS))),
                ZeroOrMore(AnyOf(LOWER_CASE + UPPER_CASE + DIGITS)));
        return Sequence(typeReferenceWithoutSpaces,
                ZeroOrMore(' ', typeReferenceWithoutSpaces));
    }

    /*
      @formatter:off
      PrivateKeyInfo ::= SEQUENCE {
      version Version,
      privateKeyAlgorithm PrivateKeyAlgorithmIdentifier,
      privateKey PrivateKey,
      attributes [0] IMPLICIT Attributes OPTIONAL }
      
        TypeReference '::=' 'SEQUENCE' '{' SequenceMember [',', SequenceMember] '}'
        SequenceMember <- Identifier [tag] [IMPLICIT] TypeReference [OPTIONAL]
                          '...'
      @formatter:off
     */
}
