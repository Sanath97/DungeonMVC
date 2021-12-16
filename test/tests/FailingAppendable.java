package tests;

import java.io.IOException;

/**
 * A mock class to simulate a failure to write to the Appendable.
 */
public class FailingAppendable implements Appendable {

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException("Fail!");
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException("Fail!");
  }

  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException("Fail!");
  }
}
