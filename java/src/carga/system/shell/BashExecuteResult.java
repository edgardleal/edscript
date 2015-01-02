/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package carga.system.shell;

/**
 * 
 * @author Edgard Leal
 */
public class BashExecuteResult {

  public String getOutput() {
    return output;
  }

  public int getExitCode() {
    return exitCode;
  }

  private String output;
  private int exitCode;

  public BashExecuteResult(String output, int exitCode) {
    this.output = output;
    this.exitCode = exitCode;
  }
}
