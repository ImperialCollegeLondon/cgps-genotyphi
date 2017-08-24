package net.cgps.wgsa.genotyphi.formats;

import net.cgps.wgsa.genotyphi.GenotyphiResult;

import java.util.function.Function;

public interface Formatter extends Function<GenotyphiResult, String> {

  String getFileExtension();
}
