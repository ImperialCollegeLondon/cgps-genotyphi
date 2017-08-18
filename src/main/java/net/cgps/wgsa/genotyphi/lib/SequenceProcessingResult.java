package net.cgps.wgsa.genotyphi.lib;

import java.util.Collection;

/**
 * Created by cyeats on 28/04/15.
 */
public class SequenceProcessingResult {

  private final Collection<Mutation> mutations;

  public SequenceProcessingResult(final Collection<Mutation> mutations) {


    this.mutations = mutations;
  }

  public Collection<Mutation> getMutations() {

    return this.mutations;
  }
}
