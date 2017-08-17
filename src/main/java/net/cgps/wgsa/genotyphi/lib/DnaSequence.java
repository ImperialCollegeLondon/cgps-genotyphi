package net.cgps.wgsa.genotyphi.lib;

import org.apache.commons.lang3.StringUtils;

class DnaSequence {

  static String reverseTranscribe(final String dnaSequence) {

    return complement(new StringBuffer(dnaSequence).reverse().toString());
  }

  static String complement(final String dnaSequence) {

    return StringUtils.replaceChars(dnaSequence, "GTCAgtca", "CAGTcagt");
  }

}

