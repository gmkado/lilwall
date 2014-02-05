/* Generated By:JavaCC: Do not edit this line. PigFileParserConstants.java */
package org.apache.pig.tools.parameters;

public interface PigFileParserConstants {

  int EOF = 0;
  int LETTER = 5;
  int DIGIT = 6;
  int SPECIALCHAR = 7;
  int DECLARE = 8;
  int PIGDEFAULT = 9;
  int IDENTIFIER = 10;
  int LITERAL = 11;
  int SHELLCMD = 12;
  int ORD_LINE = 13;

  int DEFAULT = 0;

  String[] tokenImage = {
    "<EOF>",
    "\"\\n\"",
    "\"\\r\"",
    "\" \"",
    "\"\\t\"",
    "<LETTER>",
    "<DIGIT>",
    "<SPECIALCHAR>",
    "\"%declare\"",
    "\"%default\"",
    "<IDENTIFIER>",
    "<LITERAL>",
    "<SHELLCMD>",
    "<ORD_LINE>",
  };

}
