package de.dagere.peass.ci.clean.callables;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jenkinsci.remoting.RoleChecker;

import de.dagere.peass.folders.ResultsFolders;
import hudson.FilePath.FileCallable;
import hudson.remoting.VirtualChannel;

public class CleanRTSCallable implements FileCallable<Boolean> {

   @Override
   public void checkRoles(final RoleChecker checker) throws SecurityException {

   }

   @Override
   public Boolean invoke(final File potentialSlaveWorkspace, final VirtualChannel channel) throws IOException, InterruptedException {
      try {
         String projectName = potentialSlaveWorkspace.getName();
         File folder = new File(potentialSlaveWorkspace.getParentFile(), projectName + "_fullPeass");
         ResultsFolders resultsFolders = new ResultsFolders(folder, projectName);

         deleteResultFiles(resultsFolders);
         deleteLogFolders(resultsFolders);

         CleanUtil.cleanProjectFolder(folder, projectName);

         return true;
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
   }

   private void deleteResultFiles(final ResultsFolders resultsFolders) throws IOException {
      System.out.println("Deleting " + resultsFolders.getDependencyFile());
      FileUtils.delete(resultsFolders.getDependencyFile());
      System.out.println("Deleting " + resultsFolders.getExecutionFile());
      FileUtils.delete(resultsFolders.getExecutionFile());
      System.out.println("Deleting " + resultsFolders.getCoverageSelectionFile());
      FileUtils.delete(resultsFolders.getCoverageSelectionFile());
      System.out.println("Deleting " + resultsFolders.getCoverageInfoFile());
      FileUtils.delete(resultsFolders.getCoverageInfoFile());
      
      System.out.println("Deleting " + resultsFolders.getViewFolder());
      FileUtils.deleteDirectory(resultsFolders.getViewFolder());
      System.out.println("Deleting " + resultsFolders.getPropertiesFolder());
      FileUtils.deleteDirectory(resultsFolders.getPropertiesFolder());
      
   }

   private void deleteLogFolders(final ResultsFolders resultsFolders) throws IOException {
      System.out.println("Deleting " + resultsFolders.getRtsLogFolder());
      FileUtils.deleteDirectory(resultsFolders.getRtsLogFolder());
      System.out.println("Deleting " + resultsFolders.getSourceReadLogFolder());
      FileUtils.deleteDirectory(resultsFolders.getSourceReadLogFolder());
   }

}
