package com.github.ittyflow;

public interface TransitionAwareException
{
  public void setTransitionFailure(TransitionFailedException description);
}

